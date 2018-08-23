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
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.qk.R;
import com.qk.util.UploadUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * NFC Activity
 */
public class NfcActivity extends Activity implements View.OnClickListener{
    private Tag tag;
    private Ndef ndef;
    private NfcAdapter nfcAdapter;
    private PendingIntent pend;
    private static String requestURL = "http://192.168.1.212:8011/pd/upload/fileUpload.do";
    private Button selectImage, uploadImage;
    private ImageView imageView;
    private String picPath = null;

    /**
     * Called when the activity is first created.
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
        set_nfc();
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
                    Toast.makeText(NfcActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
                } else {
                    final File file = new File(picPath);

                    String request = UploadUtil.uploadFile(file, requestURL);
                    uploadImage.setText(request);
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
                String path = getImageAbsolutePath(NfcActivity.this, uri);

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
                        alert();
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
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


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
//    {
//
//        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE)
//        {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
//            {
//                takePhoto();
//            } else
//            {
//                // Permission Denied
//                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//
//        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE2)
//        {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
//            {
//                choosePhoto();
//            } else
//            {
//                // Permission Denied
//                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    /**
     * 判断设备是否支持nfc功能
     * */
    public void set_nfc(){

        //获取Nfc适配器,判断设备是否支持nfc
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Log.e("nfc","1111----"+nfcAdapter);
        if(nfcAdapter==null){
            Toast.makeText(this, "该设备不支持nfc", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!nfcAdapter.isEnabled()){
            Toast.makeText(this, "先打开nfc功能", Toast.LENGTH_SHORT).show();
            return;
        }
        pend = PendingIntent.getActivity(this, 0, new Intent(this, getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onNewIntent(Intent intent) {
        // TODO 自动生成的方法存根
        Log.e("onnewinten","tent---"+intent);
        super.onNewIntent(intent);
        //判断发来的intent是否我们设置的过滤机制，不是则退出不做处理
        if (!NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Log.e("NfcAdapter.",""+NfcAdapter.ACTION_TECH_DISCOVERED);
            Log.e("return","return---");
            Log.e("intent.getAction()","return---"+intent.getAction());
            return;
        }
        //获取到Tag标签对象
        tag=intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String tagid= bytesToHexString(tag.getId());
        Log.e("tag","tag---"+intent);
        Log.e("tagid","tagid---"+tagid);
        if(tag!=null){
            //Tag标签中的Ndef内容
            ndef = Ndef.get(tag);
        }else{
            return;
        }
        //获取此标签中支持的技术
        String[] techList=tag.getTechList();
        for (String tech:techList){
            Log.d("TECH",tech);
        }
        //获取卡片类型
         techList = tag.getTechList();
        Log.e("techlist",""+techList);
//NFC卡的类型： Mifare Classic/UltraLight Info/IsoDep
        for (int i = 0; i < techList.length; i++) {
            //根据不同的卡片类型获取各自的Tag
            if (techList[i].equals(NfcA.class.getName())) {
                NfcA nfc = NfcA.get(tag);
                try {
                    if ("".equals(NfcA.class.getName())){
                        Log.d("Type",NfcA.class.getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (techList[i].equals(MifareUltralight.class.getName())) {
                MifareUltralight mifareUlTag = MifareUltralight
                        .get(tag);
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        Log.d("Type","ULTRALIGHT");
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        Log.d("Type","ULTRALIGHT_C");
                        break;
                    default:break;
                }
            }else if(techList[i].equals(IsoDep.class.getName())){
                Log.d("Type",IsoDep.class.getName());
            }
        }
        readIntent();
    }


    /**
     * 开始监听nfc功能
     */
    @Override
    protected void onResume() {
        // TODO 自动生成的方法存根
        super.onResume();
        if (nfcAdapter != null) {
            //开始监听NFC设备是否连接，如果连接就发pend意图,设置当前程序为优先处理nfc的程序
            Log.e("pend","pend---"+pend);
            nfcAdapter.enableForegroundDispatch(this, pend, null, null);
        }
    }

    /**
     * 停止监听nfc
     */
    @Override
    protected void onPause() {
        // TODO 自动生成的方法存根
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    private String bytesToHexString(byte[] bArray) {
        StringBuffer strBuf = new StringBuffer(bArray.length);
        String strTemp;
        for(int i = 0; i < bArray.length; i++) {
            strTemp = Integer.toHexString(0xFF & bArray[i]);
            if(strTemp.length() < 2) {
                strBuf.append(0);
            }
            strBuf.append(strTemp.toUpperCase());
        }
        return strBuf.toString();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void readIntent(){
        if (tag == null) {
            return;
        }
        if(ndef==null){
            return;
        }
        try {
            //打开连接
            ndef.connect();
            //读取标签中的ndef内容
            NdefMessage message = ndef.getNdefMessage();
            Log.e("message",""+message);
            //获取标签中的内容长度
            int length = message.getByteArrayLength();
            Log.e("length",""+length);
            if(message==null){
                Toast.makeText(this, "Null Message", Toast.LENGTH_SHORT).show();
                ndef.close();
                return;
            }
            //一个NdefMessage是有一条或多条的NdefRecord组成。
            NdefRecord[] recoder = message.getRecords();
            for(NdefRecord r:recoder){
                //使用该方法获取该NdefRecord中的实际内容
                byte[] b = r.getPayload();
                //如果直接使用r.toString()获取到的内容还包括了ndef格式中的一些前缀数据
            }
            //将消息转换成字节数组
            //关闭连接
            ndef.close();
        } catch (IOException | FormatException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
    }

}
