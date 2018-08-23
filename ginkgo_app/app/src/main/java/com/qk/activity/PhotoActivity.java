package com.qk.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qk.Constant;
import com.qk.GApp;
import com.qk.R;
import com.qk.adapter.PhotoShowAdapter;
import com.qk.module.PhotoItem;
import com.qk.util.BitmapUtil;
import com.qk.util.RecycleViewLisitenter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 照片 Activity
 */
public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_LIST_CODE = 0;
    private static final int REQUEST_CAMERA_CODE = 1;
    private PhotoShowAdapter adapter;
    private List<PhotoItem> list = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private Button deleteImage;
    private Button uploadImage;
    private Button selectImage;
    private ProgressBar progressBar;
    private Context ctx;
    /**
     * 默认false不能删除
     */
    private Boolean delete;
    /**
     * 默认false不能显示
     */
    private Boolean showorhid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        initGlide();
        initView();
    }

    /**
     * 初始化控件
     * */
    public void initView(){
        ctx = this;
        delete = false;
        showorhid = false;
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        uploadImage =  this.findViewById(R.id.uploadImage);
        uploadImage.setOnClickListener(this);
        selectImage =  this.findViewById(R.id.selectImage);
        selectImage.setOnClickListener(this);
        deleteImage =  this.findViewById(R.id.deleteImage);
        deleteImage.setOnClickListener(this);
        progressBar = findViewById(R.id.progress);

    }
    private void ShowDialg(int position) {
    }

    /**
     *初始化图片浏览
    * */
    public void initGlide(){
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
    }

    /**
    * 选择图片的点击事件
    */
    public void multiselect(View view) {
        if( GApp.SHOWORHIDE ){
            GApp.SHOWORHIDE = false;
            adapter.notifyDataSetChanged();
        }

        ISListConfig config = new ISListConfig.Builder()
                //是否多选
                .multiSelect(true)
                // 是否记住上次选中记录
                .rememberSelected(false)
                .needCamera(false)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5")).build();
        ISNav.getInstance().toListActivity(this, config, REQUEST_LIST_CODE);
    }


    /**
     *选择图片完成的回调事件
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            for (String path : pathList) {
                PhotoItem item = new PhotoItem();
                item.setImg(path);
                item.setText("1111");
                item.setCheck(false);
                list.add(item);
            }

//      初始化适配器
            adapter = new PhotoShowAdapter(this,list);
//      设置添加或删除item时的动画，这里使用默认动画
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(adapter);

            //  设置自定义的条目点击事件
            adapter.setOnItemClickLisitenter(new RecycleViewLisitenter.onItemClickLisitenter() {
                @Override
                public void onItemClick(View v, int position) {
                    if(GApp.SHOWORHIDE){
                        if(list.get(position).getGetCheck()){
                            list.get(position).setGetCheck(false);
                            ((CheckBox)v.findViewById(R.id.check_photo)).setChecked(false);
                        }else{
                            list.get(position).setGetCheck(true);
                            ((CheckBox)v.findViewById(R.id.check_photo)).setChecked(true);
                        }
                    }else{

                    }

                    Toast.makeText(ctx,position+"短按", Toast.LENGTH_SHORT).show();
                }
            });
            /*
             * 设置自定义的条目长按监听
             */
            adapter.setOnItemLongClickLisitenter(new RecycleViewLisitenter.onItemLongClickLisitenter() {
                @Override
                public void onItemLongClick(View v, int position) {
                    Toast.makeText(ctx,list.get(position).getText()+"长按", Toast.LENGTH_SHORT).show();

                    for(int i=0;i<list.size();i++){
                        GApp.SHOWORHIDE = true;
                        list.get(i).setGetCheck(false);
                    }
                    adapter.notifyDataSetChanged();

                }
            });
        }
    }

    /**
     * 监听按钮点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uploadImage:
                if (list.size()==0) {
                    Toast.makeText(PhotoActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
                } else {
                    List<File> files = new ArrayList<File>();
                    for (PhotoItem item: list) {
                        files.add(new File(BitmapUtil.compressImage(item.getImg())));
                    }
                    OkGo.<String>post(Constant.UPPHTOT)
                            .tag(1)
                            .addFileParams("file1",files)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    Toast.makeText(PhotoActivity.this, "成功", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onError(Response<String> response) {
                                    Toast.makeText(PhotoActivity.this, "shibai", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void uploadProgress(Progress progress) {
                                    Log.e("progress","progress-------"+progress);
                                    String downloadLength = Formatter.formatFileSize(getApplicationContext(), progress.currentSize);
                                    String totalLength = Formatter.formatFileSize(getApplicationContext(), progress.totalSize);
                                    String speed = Formatter.formatFileSize(getApplicationContext(), progress.speed);
                                    progressBar.setMax(10000);
                                    progressBar.setProgress((int) (progress.fraction * 10000));
                                }
                            });
                }
                break;
            case R.id.deleteImage:
                if(!GApp.SHOWORHIDE){
                    Toast.makeText(ctx, "请先选择图片", Toast.LENGTH_SHORT).show();
                }else{
                    for(int i = 0; i < list.size(); i++){
                        if(list.get(i).getGetCheck()){
                            list.get(i).setGetCheck(false);
                            list.remove(i);
                            i--;
                        }
                    }
                    adapter.notifyDataSetChanged();
                    GApp.SHOWORHIDE = false;
                }
                break;
            case R.id.selectImage:
                if( GApp.SHOWORHIDE ){
                    GApp.SHOWORHIDE = false;
                    adapter.notifyDataSetChanged();
                }

                ISListConfig config = new ISListConfig.Builder()
                        //是否多选
                        .multiSelect(true)
                        // 是否记住上次选中记录
                        .rememberSelected(false)
                        .needCamera(false)
                        // 使用沉浸式状态栏
                        .statusBarColor(Color.parseColor("#3F51B5")).build();
                ISNav.getInstance().toListActivity(this, config, REQUEST_LIST_CODE);
                break;
            default:
                break;
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        GApp.SHOWORHIDE = false;
    }


}
