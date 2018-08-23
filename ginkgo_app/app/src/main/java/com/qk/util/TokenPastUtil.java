package com.qk.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.qk.Constant;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * token过期工具类
 */
public class TokenPastUtil {

    /**
     * 拿去参数偏好设置
     */
    public static void pddate(long token_data) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        Handler   handler = new Handler(Looper.getMainLooper());
        long date2 = Integer.parseInt(simpleDateFormat.format(date));
        if(token_data<date2){
            Log.e("date","token超期了");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    OkGo.<String>post(Constant.TOKEN_VALID_URL)
                        //强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                        .tag(1)
                        .headers("Authorization","Bearer ")
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                //这个就是返回来的结果
                                String data = response.body();
                                String code = "";
                            }
                            @Override
                            public void onError(Response<String> response) {
                            }
                        });
                }
            }, 3);
        }else{
            Log.e("date","没超期");
        }
    }
}
