package com.qk;

import com.lzy.okgo.OkGo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 自定义的拦截器
 */
public class OkTokenInterceptor2 implements Interceptor
{
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response originalResponse = chain.proceed(request);

        //根据和服务端的约定判断token过期
        if (1==1) {
            originalResponse.body().close();
            //重新请求
            return chain.proceed(request);
        }
        return originalResponse;
    }

    /**
     * 根据Response约定，判断Token是否失效
     * @param response
     */
    private boolean isTokenExpired(String response) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(response);
            if (true) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 同步请求方式，获取最新的Token
     */
    private ResponseBody getNewToken() throws IOException {
        // 通过一个特定的接口获取新的token，此处要用到同步的retrofit请求
//        Call<LoginEntity> loginCall = RetrofitHelper.getInstance().getRetrofit(Api.class)
//                .getToken("参数");

        Response response = OkGo.get("http://www.baidu.com")//
                .tag(1)//
                .headers("aaa", "111")//
                .params("bbb", "222")
                .execute();

        return response.body();
    }
}