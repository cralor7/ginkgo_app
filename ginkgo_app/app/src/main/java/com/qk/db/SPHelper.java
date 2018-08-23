package com.qk.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.qk.Constant;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * SPHelper
 */
public final class SPHelper {

    /**
     * 得到2g/3g下是否可以下载图片
     * @param context context
     * @return boolean
     */
    public static boolean isShowImg(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.LOGTAG, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isShowImg", true);
    }

    /**
     * 设置2g/3g下是否可以下载图片
     * @param context context
     * @param is boolean
     */
    public static void setShowImg(Context context, boolean is) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.LOGTAG, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("isShowImg", is).apply();
    }

    /**
     * 得到wifi下是否可以自动缓存
     * @param context context
     * @return boolean
     */
    public static boolean isWifiDown(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.LOGTAG, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isWifiDown", true);
    }

    /**
     * 设置wifi下是否可以自动缓存
     * @param context context
     * @param is boolean
     */
    public static void setWifiDown(Context context, boolean is) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.LOGTAG, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("isWifiDown", is).apply();
    }

    /**
     * 设置主题
     * true 表示明亮主题 false 表示安主题
     * @param context context
     * @param is boolean
     */
    public static void setTheme(Context context, boolean is) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.LOGTAG, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("theme", is).apply();
    }


    /**
     * 得到主题
     * @param context context
     * @return true 表示明亮主题 false 表示安主题
     */
    public static boolean getTheme(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.LOGTAG, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("theme", false);
    }

}
