package com.qk.db;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.qk.R;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 操作数据库类，要操作数据库操作实例首先得得到数据库操作实例
 */
public class DBOperate {
    private SQLiteHelper dbhelper;
    private Context context;
    public DBOperate(Context context) {
        this.context = context;
        this.dbhelper = SQLiteHelper.getInstance(context);
    }
    public void saveImage(){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("_id", 1);
        //图片转为二进制
        cv.put("avatar", bitmabToBytes(context));
        db.insert("User", null, cv);
        db.close();
    }
    public byte[] readImage(){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cur = db.query("User", new String[]{"_id","avatar"}, null, null, null, null, null);
        byte[] imgData = null;
        if(cur.moveToNext()){
            //将Blob数据转化为字节数组
            imgData = cur.getBlob(cur.getColumnIndex("avatar"));
        }
        return imgData;
    }

    /**
     * 图片转为二进制数据
     * @param context context
     * @return byte数组
     */
    public byte[] bitmabToBytes(Context context){
        //将图片转化为位图
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.yinshi2);
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        //创建一个字节数组输出流,流的大小为size
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
        try {
            //设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            //将字节数组输出流转化为字节数组byte[]
            byte[] imagedata = baos.toByteArray();
            return imagedata;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                bitmap.recycle();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

}