package com.qk.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.qk.Constant;
import com.qk.R;
import com.qk.util.BitmapUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 相机Activity
 */
public class CameraActivity extends AppCompatActivity  implements View.OnClickListener  {
    private ImageView imageView;
    private Context context;

    private static final String TAG = "CameraActivity";
    /**
     * 用于存储最终目录，即根目录 / 要操作（存储文件）的文件夹
     */
    private String mImagePath;
    /**
     * 存储程序在外部SD卡上的根目录的名字
     */
    public static final String SD_APP_DIR_NAME = "TestDir";
    /**
     * 存储照片在根目录下的文件夹名字
     */
    public static final String PHOTO_DIR_NAME = "photo";
    /**
     * 存储音频在根目录下的文件夹名字
     */
    public static final String VOICE_DIR_NAME = "voice";
    /**
     * 存储视频在根目录下的文件夹名字
     */
    public static final String VIDEO_DIR_NAME = "video";
    private static int Req_1=1;
    /**
     * 标志符，图片的结果码，判断是哪一个Intent
     */
    public static final int PHOTO_RESULT_CODE = 100;
    /**
     * 标志符，音频的结果码，判断是哪一个Intent
     */
    public static final int VOICE_RESULT_CODE = 101;
    /**
     * 标志符，视频的结果码，判断是哪一个Intent
     */
    public static final int VIDEO_RESULT_CODE = 102;
    /**
     * 指定的uri
     */
    private Uri mImageUri;
    /**
     * 保存的图片的名字
     */
    private String mImageName;
    /**
     * 图片文件
     */
    private File mImageFile;
    private Button uploadImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paizhao);
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        imageView = (ImageView)findViewById(R.id.iv);
        uploadImage = (Button)findViewById(R.id.start2);
        uploadImage.setOnClickListener(this);
        context =this;
        //按钮的点击事件
        Button button = (Button) findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查是否获得写入权限，未获得则向用户请求
                if (ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //未获得，向用户请求
                    ActivityCompat.requestPermissions(CameraActivity.this, new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                } else {
                    //启动照相机
                    startCamera();
                }
            }
        });
    }



    /**
     * 启动相机，创建文件，并要求返回uri
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void startCamera() {

        //隐式 打开相机
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        else {
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(intent, 100);
            Intent intent = new Intent();
            //指定动作，启动相机
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            //创建文件
            createImageFile();
            //添加权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //获取uri
            mImageUri = FileProvider.getUriForFile(this, "com.qk.provider", mImageFile);
            //将uri加入到额外数据
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            //启动相机并要求返回结果
            startActivityForResult(intent, PHOTO_RESULT_CODE);
        }

    }

    /**
     * 创建图片文件
     */
    private void createImageFile(){
        //设置图片文件名（含后缀），以当前时间的毫秒值为名称
        mImageName = Calendar.getInstance().getTimeInMillis() + ".jpg";
        //创建图片文件
        mImageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + SD_APP_DIR_NAME + "/" + PHOTO_DIR_NAME + "/", mImageName);
        //将图片的绝对路径设置给mImagePath，后面会用到
        mImagePath = mImageFile.getAbsolutePath();
        //按设置好的目录层级创建
        mImageFile.getParentFile().mkdirs();
        //不加这句会报Read-only警告。且无法写入SD
        mImageFile.setWritable(true);
    }

    /**
     * 处理返回结果。
     * 1、图片
     * 2、音频
     * 3、视频
     * @param requestCode 请求码
     * @param resultCode  结果码 成功 -1 失败 0
     * @param data        返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PHOTO_RESULT_CODE: {
                    Bitmap bitmap = null;
                    //bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                    // bitmap =BitmapUtil.getBitmapFormUri(this,mImageUri);
                    bitmap=BitmapUtil.getSmallBitmap(mImagePath);
                    imageView.setImageBitmap(bitmap);
                    //bitmap = BitmapUtil.compressImage(bitmap);
                    //将图片保存到SD的指定位置
               //     savePhotoToSD(bitmap);
                    //更新系统图库
                    updateSystemGallery();
                    break;
                }
                case VOICE_RESULT_CODE: {
//                    saveVoiceToSD();
                    break;
                }
                case VIDEO_RESULT_CODE: {
//                    saveVideoTOSD();
                    break;
                }
                default:break;
            }
        }
    }

    /**
     * 保存照片到SD卡的指定位置
     */
    private void savePhotoToSD(Bitmap bitmap) {
        //创建输出流缓冲区
        BufferedOutputStream os = null;
        try {
            //设置输出流
            os = new BufferedOutputStream(new FileOutputStream(mImageFile));
            //压缩图片，100表示不压缩
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    //不管是否出现异常，都要关闭流
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 更新系统图库
     */
    private void updateSystemGallery() {
        //把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(this.getContentResolver(),
                    mImageFile.getAbsolutePath(), mImageName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mImagePath)));
    }

    /**
     * 保存音频到SD卡的指定位置
     */
    private void saveVoiceToSD() {

    }

    /**
     * 保存视频到SD卡的指定位置
     */
    private void saveVideoTOSD() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start2:
                if(mImagePath==null){
                    Toast.makeText(context, "请先照片", Toast.LENGTH_SHORT).show();
                }else{
                    List<File> files = new ArrayList<File>();
                        files.add(new File(BitmapUtil.compressImage(mImagePath)));
                    OkGo.<String>post(Constant.UPPHTOT)
                            .tag(1)
                            .addFileParams("file1",files)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    Toast.makeText(CameraActivity.this, "成功", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onError(Response<String> response) {
                                    Toast.makeText(CameraActivity.this, "shibai", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void uploadProgress(Progress progress) {
                                    Log.e("progress","progress-------"+progress);
//                                    System.out.println("uploadProgress: " + progress);
                                    String downloadLength = Formatter.formatFileSize(getApplicationContext(), progress.currentSize);
                                    String totalLength = Formatter.formatFileSize(getApplicationContext(), progress.totalSize);
                                    String speed = Formatter.formatFileSize(getApplicationContext(), progress.speed);

                                }
                            });

                }
                break;
                default:
                    break;
        }
    }
}
