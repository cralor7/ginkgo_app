package com.qk.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.qk.BaseActivity;
import com.qk.R;
import com.qk.db.DBOperate;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * Sqlite Activity
 */
public class SqliteActivity extends BaseActivity implements View.OnClickListener{
    private Button btnLeft;
    private Button btnRight;
    private ImageView img;
    private String url;
    private DBOperate dbOperate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(intiLayout());
        dbOperate=new DBOperate(this);
        initView();
        initData();
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_sqlite;
    }

    @Override
    public void initView() {
    btnLeft = findViewById(R.id.save_photo_btn);
    btnRight = findViewById(R.id.read_photo_btn);
    img = findViewById(R.id.sqlite_img);
    btnLeft.setOnClickListener(this);
    btnRight.setOnClickListener(this);
    }

    @Override
    public void initData() {
    img.setImageResource(R.mipmap.yaogun1);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.save_photo_btn:
               Toast.makeText(this, "存储", Toast.LENGTH_SHORT).show();
                saveImage(url);
               break;
           case R.id.read_photo_btn:
               readImage();
               break;
           default:
               break;
       }
    }

    private void readImage(){
        byte[] imgData = dbOperate.readImage();
        if (imgData != null) {
            //将字节数组转化为位图
            Bitmap imagebitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
            //将位图显示为图片
            img.setImageBitmap(imagebitmap);
        }else {
            img.setBackgroundResource(android.R.drawable.menuitem_background);
        }
    }
    private void saveImage(String url){
        dbOperate.saveImage();
    }
}
