package com.qk.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.qk.Constant;
import com.qk.R;
import com.qk.util.BitmapUtil;
import com.qk.view.ProcessImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 照片 Activity
 */
public class PhotoAlbumActivity extends Activity implements View.OnClickListener{
    private Tag tag;
    private Ndef ndef;
    private NfcAdapter nfcAdapter;
    private PendingIntent pend;
    private static String requestURL = "http://192.168.1.212:8011/pd/upload/fileUpload.do";
    private Button selectImage, uploadImage;
    private ImageView imageView;

    private String picPath = null;
    ProcessImageView processImageView = null;
    private final int SUCCESS = 0;
    int progress = 0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    Toast.makeText(PhotoAlbumActivity.this, "图片上传完成", Toast.LENGTH_SHORT).show();
                    processImageView.setVisibility(View.GONE);
                    break;
                default:break;
            }
        }
    };
    /**
     *  Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhaoxiangji);
        selectImage =  this.findViewById(R.id.selectImage);
        uploadImage =  this.findViewById(R.id.uploadImage);
        selectImage.setOnClickListener(this);
        uploadImage.setOnClickListener(this);
        imageView =  this.findViewById(R.id.imageView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectImage:
                /*
                 * 这个是调用android内置的intent，来过滤图片文件 ，同时也可以过滤其他的
                 */
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
                break;
            case R.id.uploadImage:
                if (picPath == null) {
                    Toast.makeText(PhotoAlbumActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
                } else {
                    List<File> files = new ArrayList<>();
                    File file = new File(BitmapUtil.compressImage(picPath));
                    files.add(file);
                    files.add(file);
                    Toast.makeText(PhotoAlbumActivity.this, "file"+files, Toast.LENGTH_SHORT).show();
                    OkGo.<String>post(Constant.UPPHTOT)
                            .tag(1)
                            .addFileParams("file1",files)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    Toast.makeText(PhotoAlbumActivity.this, "成功", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onError(Response<String> response) {
                                    Toast.makeText(PhotoAlbumActivity.this, "shibai", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void uploadProgress(Progress progress) {
//                                    System.out.println("uploadProgress: " + progress);
                                }
                            });
                }
                break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            /*
             * 当选择的图片不为空的话，在获取到图片的途径
             */
            Uri uri = data.getData();
            Log.e("111", "uri = " + uri);
            try {
                String[] pojo = { MediaStore.Images.Media.DATA };
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        1);
                Cursor cursor = this.getContentResolver().query(uri, pojo, null, null, null);
//                if (cursor != null) {
//                    ContentResolver cr = this.getContentResolver();
//                    int colunm_index = cursor
//                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                    cursor.moveToFirst();
//                    String path = cursor.getString(colunm_index);
                String path = getImageAbsolutePath(PhotoAlbumActivity.this, uri);

            FileInputStream fis = new FileInputStream(path);
                Log.e("2222", "PATH = " + path);
                    /*
                     * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，
                     * 这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
                     */
                    if (path.endsWith("jpg") || path.endsWith("png")) {
                        picPath = path;
                        Bitmap bitmap = BitmapFactory.decodeStream(fis);
                        imageView.setImageBitmap(bitmap);
                    } else {

                    }
            } catch (Exception e) {
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getImageAbsolutePath(Activity context, Uri imageUri) {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {

            /*
             * 动态赋予权限（读取相册权限）
             */
                 ActivityCompat.requestPermissions(this,
                 new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                 Toast.makeText(context, "权限还没赋予", Toast.LENGTH_SHORT).show();
            //权限还没有授予，需要在这里写申请权限的代码
        }else {
            Toast.makeText(context, "已经赋予了权限", Toast.LENGTH_SHORT).show();
            //权限已经被授予，在这里直接写要执行的相应方法即可
        }
        if (context == null || imageUri == null){
            return null;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(this, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(this, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri)){
                return imageUri.getLastPathSegment();
            }

            return getDataColumn(this, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Log.e("path",""+uri.getPath());
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(column);
                Log.e("path1","1112----"+cursor.getString(index));
                Toast.makeText(context, ""+cursor.getString(index), Toast.LENGTH_LONG).show();
                String path = cursor.getString(index);
                Log.e("path12","1112----"+path);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null){
                cursor.close();
            }

        }
        return null;
    }

/*    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String mImgPath;
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = context.getContentResolver().query(uri,
                filePathColumn, null, null, null);
        if(cursor!=null)
        {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            mImgPath = picturePath;
        }
        else
        {
            mImgPath = uri.getPath();
        }
        return mImgPath;
    }*/



    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    private void alert() {
        Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("您选择的不是有效的图片")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        picPath = null;
                    }
                }).create();
        dialog.show();
    }

    public String getDate(){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(d);
    }



}
