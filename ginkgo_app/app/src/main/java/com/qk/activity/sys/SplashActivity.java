package com.qk.activity.sys;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.qk.BuildConfig;
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
public class SplashActivity extends Activity{

    public static final int INSTALL_APK_REQUESTCODE = 3;
    public static final int GET_UNKNOWN_APP_SOURCES = 7;
    private File apkFile;

    Handler handler;
    /**
     * 获取已存储的token
     */
    private String token;

    private Context ctx;
    private static final int REQUEST_PERMISSION_STORAGE = 0x01;
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

        if("".equals(token) || "value".equals(token)){
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
                                    JSONObject jsonObject = new JSONObject(data);
                                    code = jsonObject.get("code").toString();
                                    version = jsonObject.get("data").toString();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.v("data",""+code);
                                if(Constant.SUCCESS_CODE.equals(code)){
<<<<<<< HEAD

=======
//                                    GApp.TOKEN = token;
//                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                                    startActivity(intent);
//
//
//                                 finish();
>>>>>>> 4ae85da24bcc7fd0c4e2a4e00ad537b018a0b60a
                                    if(Constant.VERSION.equals(version)){
                                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Log.e("SplashActivity","需要更新版本到---"+version);
                                        Toast.makeText(ctx, "您需要更新新版本", Toast.LENGTH_SHORT).show();
                                        getVersion();
                                    }
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

<<<<<<< HEAD
    /**
     * 更新弹出框
     */
=======
>>>>>>> 4ae85da24bcc7fd0c4e2a4e00ad537b018a0b60a
    public void getVersion(){
        //退出的确认弹出框
        new AlertDialog.Builder(ctx)
                .setTitle("提示")
                .setMessage("请更新最新版本")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(ctx, "dianjixiazai ", Toast.LENGTH_SHORT).show();
                        fileDownload();
                        View  view1 = getLayoutInflater().inflate(R.layout.update_app_progress, null);

                        tvDownloadSize = view1.findViewById(R.id.downloadSize);
                        tvProgress = view1.findViewById(R.id.tvProgress);
                        tvNetSpeed = view1.findViewById(R.id.netSpeed);
                        pbProgress = view1.findViewById(R.id.pbProgress);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                        builder.setTitle("正在下载");
//                builder.setMessage("请输入手机号码");
                        builder.setView(view1);
                        builder.setCancelable(false);
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.create().show();
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
     * 获取服务器apk
     */
    public void fileDownload() {
        //检查是否获得写入权限，未获得则向用户请求。Android 7.0 以上需要动态获取权限
        if (ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //未获得，向用户请求
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }
        OkGo.<File>get("http://10.2.72.105:8080/ceshi.pdf")
                .tag(1)
                .headers("header1", "headerValue1")
                .params("param1", "paramValue1")
                .execute(new FileCallback("ceshi.pdf") {
                    @Override
                    public void onSuccess(Response<File> response) {

<<<<<<< HEAD
                        String absolutePath = response.body().getAbsolutePath();
                        apkFile = response.body().getAbsoluteFile();

                        //Android8.0 版本更新无法自动安装问题解决
                    /*    if (Build.VERSION.SDK_INT >= 26) {
                            //来判断应用是否有权限安装apk
                            Log.e("appInstall---", "android版本8.0以上");
                            boolean installAllowed= getPackageManager().canRequestPackageInstalls();
                            //有权限
                            if (installAllowed) {
                                //安装apk
                                Log.e("appInstall---", "有权限安装apk");
                                installApk(apkFile);
                            } else {
                                //无权限 申请权限
                                Log.e("appInstall---", "无权限 申请权限");
                                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_APK_REQUESTCODE);
                            }
                        } else {
                            Log.e("appInstall---", "android版本低于8.0");
                            installApk(apkFile);
                        }*/
                    }
                    @Override
                    public void onError(Response<File> response) {
                        Throwable exception = response.getException();
                        exception.printStackTrace();
                        Log.e("eeeee", exception.getMessage());
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
    /**
     * 权限申请回调
     * @param requestCode requestCode
     * @param permissions permissions
     * @param grantResults grantResults
     */
=======
>>>>>>> 4ae85da24bcc7fd0c4e2a4e00ad537b018a0b60a
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case INSTALL_APK_REQUESTCODE:
                //有注册权限且用户允许安装
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("appInstall---", "有注册权限且用户允许安装");
                    installApk(apkFile);
                } else {
                    //将用户引导至安装未知应用界面。
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    startActivityForResult(intent, GET_UNKNOWN_APP_SOURCES);
                    Log.e("appInstall---", "将用户引导至安装未知应用界面");
                }
                break;
            default:break;
        }
    }
    /**
     * 将用户引导至安装未知应用界面，允许安装未知应用后，回到当前activity继续安装应用
     * @param requestCode requestCode
     * @param resultCode resultCode
     * @param data data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("appInstall---", "将用户引导至安装未知应用界面，允许安装未知应用后，回到当前activity继续安装应用");
        installApk(apkFile);
    }
    /**
     * 安装APK
     * @param apkFile apkFile
     */
    public void installApk(File apkFile){
        //判读版本是否在7.0以上
        if(Build.VERSION.SDK_INT >= 24) {
            Log.e("appInstall---", "android版本7.");
            //在AndroidManifest中的android:authorities值
            Uri apkUri = FileProvider.getUriForFile(ctx, BuildConfig.APPLICATION_ID+".provider", apkFile);Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            startActivity(install);
        } else {
            Log.e("appInstall---", "android版本低于7.0");
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(install);

        }
        finish();
    }

<<<<<<< HEAD


=======
        public void fileDownload(View view) {
            OkGo.<File>get("https://lighttruck.com.cn:8086/app.apk")
                    .tag(1)
                    .headers("header1", "headerValue1")
                    .params("param1", "paramValue1")
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
>>>>>>> 4ae85da24bcc7fd0c4e2a4e00ad537b018a0b60a

    /**
     * 调用默认浏览器下载apk
     */
    public void getApk(){
        Uri uri = Uri.parse("https://lighttruck.com.cn:8086/app.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
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

}
