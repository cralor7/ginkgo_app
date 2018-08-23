package com.qk.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qk.GApp;
import com.qk.module.Menu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static com.qk.GApp.context;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 数据工具类
 */
public class DataUtils {

    /**
     * 清除SharedPreferences中对应key的本都存储数据
     * @param context 调用方法的Activity
     * @param key 要清除的数据
     */
    public static void removeLocalData(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, "");
        editor.apply();
    }
    /**
     * 获取SharedPreferences中对应key的本地存储数据，默认值为value，适合不关注默认值的
     * @param context 调用方法的Activity
     * @param key 要获取数据的key
     * @return  key对应的值
     */
    public static String getLocalData(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, MODE_PRIVATE);
        return sharedPreferences.getString(key, "value");
    }
    /**
     * 获取SharedPreferences中对应key的本地存储数据，可改变默认值
     * @param context 调用方法的Activity
     * @param key 要获取数据的key
     * @param defaultValue 默认值
     * @return  key对应的值
     */
    public static String getLocalData(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }
    /**
     * 获取SharedPreferences中对应key的本地存储数据，默认值为value，适合不关注默认值的
     * @param context 调用方法的Activity
     * @param key 要获取数据的key
     * @return  key对应的值
     */
    public static boolean getLocalDataBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }
    /**
     * 获取SharedPreferences中对应key的本地存储数据，可改变默认值
     * @param context 调用方法的Activity
     * @param key 要获取数据的key
     * @param defaultValue 默认值
     * @return  key对应的值
     */
    public static Boolean getLocalDataBoolean(Context context, String key, Boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }
    /**
     * 修改SharedPreferences中对应key的本地存储数据
     * @param context 调用方法的Activity
     * @param key 要修改的key
     * @param value 要修改的数据
     */
    public static void updateLocalData(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    /**
     * 修改SharedPreferences中对应key的本地存储数据
     * @param context 调用方法的Activity
     * @param key 要修改的key
     * @param value 要修改的数据
     */
    public static void updateLocalData(Context context, String key, Boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    /**
     * 保存新数据到SharedPreferences
     * @param context 调用方法的Activity
     * @param key 要保存的key
     * @param value 要保存的数据
     */
    public static void saveLocalData(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    /**
     * 将用户权限列表保存到GApp和SharedPreferences中
     * @param menuList 权限列表
     * @param ctx Context
     */
    public static void saveMenu(ArrayList<ArrayList<Menu>> menuList,Context ctx) {
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(menuList);
        Log.e("LoginActivity","menuList--"+strJson);
        DataUtils.saveLocalData(ctx,"menuList",strJson);
        GApp.parentMenu = new String[menuList.size()];
        GApp.parentImage = new int[menuList.size()];
        GApp.childMenu = new String[menuList.size()][];
        GApp.childImage = new int[menuList.size()][];
        for (int i = 0; i < menuList.size(); i++){
            //是一级菜单
            int j;
            for (j = 0; j < menuList.get(i).size(); j++) {
                if("1".equals(menuList.get(i).get(j).getLevel())){
                    int resId = ctx.getResources().getIdentifier(menuList.get(i).get(j).getAlias(), "mipmap" , ctx.getPackageName());
                    GApp.parentImage[i] = resId;
                    GApp.parentMenu[i] = menuList.get(i).get(j).getName();
                    menuList.get(i).remove(j);
                    break;
                }
            }
            GApp.childMenu[i] = new String[menuList.get(i).size()];
            GApp.childImage[i] = new int[menuList.get(i).size()];
            for (j=0;j<menuList.get(i).size();j++){
                GApp.childMenu[i][j] = menuList.get(i).get(j).getName();
                    int resId = ctx.getResources().getIdentifier(menuList.get(i).get(j).getAlias()+"", "mipmap" , ctx.getPackageName());
                    GApp.childImage[i][j] = resId;
            }
        }

    }
}
