package com.ipd.paylove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.ipd.paylove.R;
import com.ipd.paylove.base.BaseActivity;
import com.ipd.paylove.entity.LoginResult;
import com.ipd.paylove.gloable.Constant;
import com.ipd.paylove.utils.CommonUtils;
import com.ipd.paylove.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/*******
 * s闪图页面
 ******/
public class Splash extends BaseActivity {
    private boolean ifAutoLogin = false;
    private boolean isFirstStall = false;//false是第一次安装
    private LoginResult loginResult;
    private String loginName, loginPwd;

    @Override
    public int setLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ifAutoLogin = SharedPreferencesUtils.getbooleanSharedPreferences(getApplicationContext(), "ifAutoLogin");
        loginName = SharedPreferencesUtils.getSharedPreferences(getApplicationContext(), "login_name");
        loginPwd = SharedPreferencesUtils.getSharedPreferences(getApplicationContext(), "login_pwd");
        isFirstStall = SharedPreferencesUtils.getbooleanSharedPreferences(getApplicationContext(), "isFirstStall");
        SystemClock.sleep(2000);
        Log.i("TAG","ifAutoLogin==="+ifAutoLogin);
        if (isFirstStall) {
            if (!CommonUtils.isNetworkAvailable(this)) {
//                ToastUtils.show(getApplicationContext(), "请检查网络！");
                intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
                return;
            }

            if (ifAutoLogin) {
                //自动登录
                loginApp(loginName, loginPwd);

            } else {
                   /* intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();*/
                intent = new Intent(Splash.this, TotalActivity.class);
                startActivity(intent);
                finish();
            }
        }else {
            SharedPreferencesUtils.setbooleanSharedPreferences(getApplicationContext(),"isFirstStall",true);
            intent = new Intent(Splash.this, Guid.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }


    private void loginApp(String login_name, String login_pwd) {
        Log.i("TAG","login===");
//        dialog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "User/login");
        params.addBodyParameter("login_name", login_name);
        params.addBodyParameter("login_pwd", login_pwd);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TAG", "result=" + result);

                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {

                        loginResult = JSON.parseObject(json.getString("data"), LoginResult.class);
                        if (loginResult != null) {
                            //保存用户登录信息
                            saveLoginInfor();
                            intent = new Intent(Splash.this, TotalActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        CommonUtils.loginOverrTime(json.getString("msg"),Splash.this,true);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("TAG", "onError=" + ex.getMessage());
                intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
//                dismiss();
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });


    }

    private void saveLoginInfor() {
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "login_name", loginResult.login_name);
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "agent", loginResult.agent);
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "phone", loginResult.phone);
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "photo", loginResult.photo);
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "real_name", loginResult.real_name);
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "reg_create", loginResult.reg_create);
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "sex", loginResult.sex);
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "team", loginResult.team);
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "user_token", loginResult.user_token);
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "login_pwd", loginPwd);

        Constant.USER_TOKEN = loginResult.user_token;
    }
}
