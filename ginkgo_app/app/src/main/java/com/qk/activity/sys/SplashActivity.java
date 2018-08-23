package com.qk.activity.sys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.Window;

import com.qk.R;
import com.qk.activity.MainActivity;
import com.qk.util.DataUtils;

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
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    /*    handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                OkGo.<String>post(Constant.TOKEN_VALID_URL)
                        //强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                        .tag(1)
                        .headers("Authorization", "Bearer " + token)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                //这个就是返回来的结果
                                String data = response.body();
                                String code = "";
                                try {
                                    JSONObject jsonObject=new JSONObject(data);
                                    code = jsonObject.get("code").toString();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.v("data",""+code);
                                if(Constant.TOKEN_VALID_CODE.equals(code)){
                                    GApp.TOKEN = token;
                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
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
        }, 3);*/

    }
}
