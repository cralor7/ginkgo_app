package com.qk.activity.sys;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.qk.Constant;
import com.qk.R;
import com.qk.activity.MainActivity;
import com.qk.util.DataUtils;

import com.qk.view.NumberProgressBar;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.NumberFormat;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 启动页 Activity
 */
public class SplashActivity extends Activity implements View.OnClickListener {

    Handler handler;
    /**
     * 获取已存储的token
     */
    private String token;

    private Context ctx;
    private static final int REQUEST_PERMISSION_STORAGE = 0x01;
    private  Button btnFileDownload;
    private  TextView tvDownloadSize;
    private TextView tvProgress;
    private  TextView tvNetSpeed;
    private  NumberProgressBar pbProgress;

    //
//        @Bind(R.id.fileDownload) Button btnFileDownload;
//        @Bind(R.id.downloadSize) TextView tvDownloadSize;
//        @Bind(R.id.tvProgress) TextView tvProgress;
//        @Bind(R.id.netSpeed) TextView tvNetSpeed;
//        @Bind(R.id.pbProgress) NumberProgressBar pbProgress;
    private NumberFormat numberFormat;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //    getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ctx = this;
        handler = new Handler(Looper.getMainLooper());
        //取出存放在SharedPreferences中的数据,第一个参数是写入是的键，第二个参数是如果没有获取到数据就默认返回的值。
        token = DataUtils.getLocalData(this, "token", "");
        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
        initView();
    }

    /**
     * 打开程序时判断是否已经登陆过，且token有效
     */
    private void initView() {
        btnFileDownload = findViewById(R.id.fileDownload);
        tvDownloadSize = findViewById(R.id.downloadSize);
        tvProgress = findViewById(R.id.tvProgress);
        tvNetSpeed = findViewById(R.id.netSpeed);
        pbProgress = findViewById(R.id.pbProgress);
        btnFileDownload.setOnClickListener(this);
        if("".equals(token)||"value".equals(token)){
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                OkGo.<String>post(Constant.GET_VERSION)
                        .tag(1)
                        .headers("Authorization", "Bearer " + token)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                //这个就是返回来的结果
                                String data = response.body();
                                String code = "";
                                String version = "";
                                try {
                                    JSONObject jsonObject=new JSONObject(data);
                                    code = jsonObject.get("code").toString();
                                    version = jsonObject.get("data").toString();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.v("data",""+code);
                                if(Constant.SUCCESS_CODE.equals(code)){
//                                    GApp.TOKEN = token;
//                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                                    startActivity(intent);
//                                    finish();
                                    if(Constant.VERSION.equals(version)){
                                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Log.e("SplashActivity","需要更新版本到---"+version);
                                        Toast.makeText(ctx, "您需要更新新版本", Toast.LENGTH_SHORT).show();
                                        getVersion();
                                    }
                                }else{
                                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onError(Response<String> response) {
                                Toast.makeText(ctx, "请求失败", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                            }
                        });
            }
        }, 3);

    }


    public void getVersion(){
        //退出的确认弹出框
        new AlertDialog.Builder(ctx)
                .setTitle("提示")
                .setMessage("请更新最新版本")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                        getApk();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                })
                .show();
    }

    /**
     * 监听Back键按下事件,方法1:
     * 注意:
     * super.onBackPressed()会自动调用finish()方法,关闭
     * 当前Activity.
     * 若要屏蔽Back键盘,注释该行代码即可
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Log.e("SplashActivity","按了返回键");
        Toast.makeText(ctx, "按了返回键", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fileDownload:
                Toast.makeText(ctx, "dianjixiazai ", Toast.LENGTH_SHORT).show();
//                fileDownload(v);
                Uri uri = Uri.parse("https://lighttruck.com.cn:8086/app.apk");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            default:break;
        }
    }


        public void fileDownload(View view) {
            OkGo.<File>get("https://lighttruck.com.cn:8086/app.apk")//
                    .tag(1)//
                    .headers("header1", "headerValue1")//
                    .params("param1", "paramValue1")//
                    .execute(new FileCallback("app.apk") {
                        @Override
                        public void onSuccess(Response<File> response) {
//                            handleResponse(response);
                            btnFileDownload.setText("下载完成");
                        }
                        @Override
                        public void onError(Response<File> response) {
//                            handleError(response);
                            btnFileDownload.setText("下载出错");
                        }
                        @Override
                        public void downloadProgress(Progress progress) {
                            System.out.println(progress);
                            String downloadLength = Formatter.formatFileSize(getApplicationContext(), progress.currentSize);
                            String totalLength = Formatter.formatFileSize(getApplicationContext(), progress.totalSize);
                            tvDownloadSize.setText(downloadLength + "/" + totalLength);
                            String speed = Formatter.formatFileSize(getApplicationContext(), progress.speed);
                            tvNetSpeed.setText(String.format("%s/s", speed));
                            tvProgress.setText(numberFormat.format(progress.fraction));
                            pbProgress.setMax(10000);
                            pbProgress.setProgress((int) (progress.fraction * 10000));
                        }
                    });

    }

    public void getApk(){
        Uri uri = Uri.parse("https://lighttruck.com.cn:8086/app.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
