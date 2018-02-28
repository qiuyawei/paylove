package com.ipd.paylove.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ipd.paylove.activity.Login;
import com.ipd.paylove.gloable.Constant;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/5/4.
 * 通用工具类
 */
public class CommonUtils {
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private static DecimalFormat df = new DecimalFormat("#.##");

    /*******
     * 数据格式化成两位小数
     *******/
    public static String formatNumber(String s) {
        return df.format(s);
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动＿134?135?136?137?138?139?150?151?157(TD)?158?159?187?188
		 * 联?：130?131?132?152?155?156?185?186 电信＿133?153?180?189、（1349卫?）
		 * 总结起来就是第一位必定为1，第二位必定丿3房5房8，其他位置的可以丿0-9
		 */
        String telRegex = "[1][35847]\\d{9}";// "[1]"代表笿1位为数字1＿"[358]"代表第二位可以为3?5?8中的丿个，"\\d{9}"代表后面是可以是0?9的数字，朿9位?
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    // 验证身份证号码
    public static boolean idCardNumber(String number) {
        String rgx = "^\\d{15}|^\\d{17}([0-9]|X|x)$";

        return isCorrect(rgx, number);
    }

    // 正则验证
    public static boolean isCorrect(String rgx, String res) {
        Pattern p = Pattern.compile(rgx);

        Matcher m = p.matcher(res);

        return m.matches();
    }


    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }

        return false;
    }


    //判断UserId是否为空，看用户是否登录
    public static boolean goLogin(Context context) {
        Constant.USER_TOKEN = SharedPreferencesUtils.getSharedPreferences(context, "user_token");
        if (Constant.USER_TOKEN.trim().equals("")) {
            DialogUtils.login(context);
            return true;
        } else {
            //什么也不做
            return false;
        }
    }

    //登录超时，账户在别处登录
    public static void loginOverrTime(String time, Context context,boolean iftoast) {
        if (time.contains("超时")) {
            context.startActivity(new Intent(context, Login.class));
//        退出登录分成两种情况，一种是记住密码，一种是不记录密码
            if (SharedPreferencesUtils.getbooleanSharedPreferences(context, "ifAutoLogin")) {
//            记住密码情况下

            } else {
                SharedPreferencesUtils.setSharedPreferences(context, "login_name", "");
                SharedPreferencesUtils.setSharedPreferences(context, "login_pwd", "");
            }


            SharedPreferencesUtils.setSharedPreferences(context, "user_token", "");
            SharedPreferencesUtils.setSharedPreferences(context, "agent", "");
            SharedPreferencesUtils.setSharedPreferences(context, "phone", "");
            SharedPreferencesUtils.setSharedPreferences(context, "photo", "");
            SharedPreferencesUtils.setSharedPreferences(context, "real_name", "");
            SharedPreferencesUtils.setSharedPreferences(context, "reg_create", "");
            SharedPreferencesUtils.setSharedPreferences(context, "sex", "");
            SharedPreferencesUtils.setSharedPreferences(context, "team", "");
            SharedPreferencesUtils.setSharedPreferences(context, "user_token", "");
            Constant.USER_TOKEN = "";
//            ToastUtils.show(context,);
            Toast.makeText(context, "帐号已在别处登录,请重新登录！", Toast.LENGTH_LONG).show();
            Log.i("TAG", "==========" + context.getClass().getName() + "==" + time);
//            context.finish();
//            return true;
        } else {

                ToastUtils.show(context, time);
            if (time.contains("用户名或密码错误")) {
                Intent intent = new Intent(context, Login.class);
                intent.putExtra("login", "用户名或密码错误");
                context.startActivity(intent);

            }
//            return false;
        }
    }

}
