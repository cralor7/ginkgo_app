package com.qk.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.qk.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 相机Activity
 */
public class CameraActivity2 extends Activity implements View.OnClickListener {
    private ImageView imgageView;
    private Context context;
    private static int Req_1=1;
    private static int Req_2=2;
    private Button button;
    private Button button2;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paizhao);
        initview();
    }
    /**
   * 初始化view
   **/
    private void initview(){
        button=(Button)findViewById(R.id.start);
        button2=(Button)findViewById(R.id.start2);
        imgageView=(ImageView)findViewById(R.id.iv);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        //获得sd卡路径
        path= Environment.getExternalStorageDirectory().getPath();
        path=path+"/"+"temp,png";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start:
//                隐式 打开相机
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
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Req_1);
                }
                break;
            case R.id.start2:
//                if(!picPath.equals("")){//post上传
//                    String actionUrl = "http://192.168.1.155:8886/FruitProject/GreenServlet";
//                    UpLoadImg.uploadImg(actionUrl, picPath);
//                }
                break;
                default:
                    break;
////                隐式 打开相机
//                Intent intent2=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                Uri photoUri=Uri.fromFile(new File(path));
//                intent2.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
//                startActivityForResult(intent2,Req_2);
//                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==RESULT_OK) {
            if (requestCode == Req_1) {
                Bundle budle = data.getExtras();
                Bitmap bitmap = (Bitmap) budle.get("data");
                imgageView.setImageBitmap(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void finish() {
        Log.e("finish","我运行了什么东西");
        Log.e("finish","我运行了什么东西");
        super.finish();
    }

    /**
     * 将bitmap转化为png格式
     * @param mBitmap
     * @return
     */
    public File saveMyBitmap(Bitmap mBitmap){
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File file = null;
        try {
            file = File.createTempFile(
                    generateFileName(),  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            FileOutputStream out=new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  file;
    }


    public static String generateFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        return imageFileName;
    }


}
