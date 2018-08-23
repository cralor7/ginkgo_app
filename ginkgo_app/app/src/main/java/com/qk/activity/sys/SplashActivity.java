package com.qk.activity.sys;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qk.Constant;
import com.qk.GApp;
import com.qk.R;
import com.qk.activity.MainActivity;
import com.qk.util.DataUtils;

import org.json.JSONException;
import org.json.JSONObject;

import static com.qk.GApp.context;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 启动页 Activity
 */
public class SplashActivity extends Activity {

    Handler handler;
    /**
     * 获取已存储的token
     */
    private String token;

    private Context ctx;

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
        initView();
    }

    /**
     * 打开程序时判断是否已经登陆过，且token有效
     */
    private void initView() {
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
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {

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
        Log.e("SplashActivity","按了返回键");
        Toast.makeText(ctx, "按了返回键", Toast.LENGTH_SHORT).show();
    }


}
