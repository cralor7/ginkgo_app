package com.qk;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.utils.OkLogger;
import com.qk.activity.sys.LoginActivity;
import com.qk.module.GetCode;
import com.qk.util.DataUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 自定义的拦截器，如果请求时token失效了，会自动刷新token并再次请求
 */
public class OkTokenInterceptor implements Interceptor
{
    private static final String LOG = OkTokenInterceptor.class.getSimpleName();
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private volatile HttpLoggingInterceptor.Level printLevel = HttpLoggingInterceptor.Level.NONE;
    @Override
    public Response intercept(Chain chain) throws IOException
    {
        final String token = GApp.TOKEN;
        Request originalRequest = chain.request();
        HttpUrl originalHttpUrl = originalRequest.url();
        final   Response originalResponse = chain.proceed(originalRequest);
        ResponseBody responseBody = originalResponse.body();
        Log.e("拦截器","originalResponse----"+originalResponse.body());
        //排除的API（登录，刷新token的）
        if ((originalHttpUrl.url() + "").equals(Constant.NEW_TOKEN) || (originalHttpUrl.url() + "").equals(Constant.LOGIN))
        {
            return originalResponse;
        }
        String code = "0";
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();
        Charset charset = UTF_8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF_8);
        }
        String bodyString = buffer.clone().readString(charset);
//        HttpResult httpResult = new Gson().fromJson(bodyString, HttpResult.class);
        try {
            JSONObject jsonObject = new JSONObject(bodyString);
            code = "" + jsonObject.get("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        InputStream data = responseBody.byteStream();
//        String data = isTokenExpired(originalResponse);
//            JSONObject jsonObject = new JSONObject(data);
//            code = "" + jsonObject.get("code");
//            Log.v("dsdsd111" ,code);
        /*
         * code 等于 -3代表token失效
         * code 等于 -4代表refresh_token失效,需要重新登录
         */
        if((Constant.TOKEN_OVERTIME).equals(code)){

            if (TextUtils.isEmpty(token))
            {
                Log.e("拦截器","token为空或者token没过期，如果为空说明用户第一次登录或者用户点击了退出登录，应该跳转到登录页面，token没过期，也不需要重新刷新token");
                Intent intent = new Intent(GApp.context, LoginActivity.class);
                GApp.context.startActivity(intent);
            }
            else
            {
                Response response = OkGo.post(Constant.NEW_TOKEN)
                        .tag(this)
                        .headers("Authorization", "Bearer "+GApp.TOKEN)
                        .headers("username", GApp.username)
                        .execute();
                String data2 = response.body().string();
                String newToken = "";
                String code1 = "";
                Log.v("refresh_token", "刷新了token");
                try {
                    JSONObject jsonObject2= new JSONObject(data2);
                    code1 = "" + jsonObject2.get("code");
                    if((Constant.RELOGIN_CODE).equals(code1)){
                        Intent intent = new Intent(GApp.context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        GApp.context.startActivity(intent);
                        OkGo.getInstance().cancelTag(1);
                    }
                    newToken = "" + jsonObject2.get("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                GApp.TOKEN = newToken;
                DataUtils.updateLocalData(GApp.context, "token", newToken);
                Request original = chain.request();
                //请求定制：添加请求头
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "bearer " + newToken);
                Request request2 = requestBuilder.build();
                return chain.proceed(request2);
            }
        }

/*        originalRequest = chain.request();
        originalResponse = chain.proceed(originalRequest);*/
        return originalResponse;
    }

    private static Charset getCharset(MediaType contentType)
    {
        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
        if (charset == null) {
            charset = UTF_8;
        }
        return charset;
    }

    /**
     * 打印body参数
     *
     * @param request 请求
     */
    private void bodyToString(Request request) {
        try {
            Request copy = request.newBuilder().build();
            RequestBody body = copy.body();
            if (body == null) {
                return;
            }
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            Charset charset = getCharset(body.contentType());
            Log.e("99999","\tbody:" + buffer.readString(charset));
        } catch (Exception e) {
            OkLogger.printStackTrace(e);
        }
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private static boolean isPlaintext(MediaType mediaType) {
        if (mediaType == null) {
            return false;
        }
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        String subtype = mediaType.subtype();
        if (subtype != null) {
            subtype = subtype.toLowerCase();
            if (subtype.contains("x-www-form-urlencoded") || subtype.contains("json") || subtype.contains("xml") || subtype.contains("html")) {
                return true;
            }
        }
        return false;
    }

    private String isTokenExpired(Response response) {
            ResponseBody body = response.body();
            if (body != null){
                try {
                    MediaType mediaType = body.contentType();
                    if (mediaType != null) {
                        if(isText(mediaType)) {
                            String resp = body.string();
                            GetCode model = new Gson().fromJson(resp, GetCode.class);
                            //4061是与后台商量的token失效后的错误码，具体应根据自己的项目决定
                                return model.getData();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        return "";
    }

    private boolean isText(MediaType mediaType)
    {
        if (mediaType.type() != null && mediaType.type().equals("text"))
        {
            return true;
        }
        if (mediaType.subtype() != null)
        {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")
                    )
                return true;
        }
        return false;
    }

}