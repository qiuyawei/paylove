package com.ipd.paylove.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 本地存储
 */
public class SharedPreferencesUtils {
    static  SharedPreferences sharedPreferences;
    /****字符串存储***/
    public static void setSharedPreferences(Context context,String key,String data){
            sharedPreferences = context.getSharedPreferences("config",context.MODE_APPEND|context.MODE_MULTI_PROCESS);
            sharedPreferences.edit().putString(key,data).commit();
    };
    public static String getSharedPreferences(Context context,String key){
        sharedPreferences = context.getSharedPreferences("config", context.MODE_APPEND|context.MODE_MULTI_PROCESS);
        return sharedPreferences.getString(key, "");

    };
    /****布尔值存储***/
    public  static void setbooleanSharedPreferences(Context context, String key, boolean flag) {
        sharedPreferences = context.getSharedPreferences("config",context.MODE_APPEND|context.MODE_MULTI_PROCESS);
        sharedPreferences.edit().putBoolean(key, flag).commit();
    }
    public static boolean getbooleanSharedPreferences(Context context,String key){
        sharedPreferences = context.getSharedPreferences("config", context.MODE_APPEND|context.MODE_MULTI_PROCESS);
        return sharedPreferences.getBoolean(key, false);

    };


}
