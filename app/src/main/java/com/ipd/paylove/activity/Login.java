package com.ipd.paylove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.ipd.paylove.R;
import com.ipd.paylove.base.BaseActivity;
import com.ipd.paylove.entity.LoginResult;
import com.ipd.paylove.gloable.Constant;
import com.ipd.paylove.utils.CommonUtils;
import com.ipd.paylove.utils.SharedPreferencesUtils;
import com.ipd.paylove.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


/**
 * @author Administrator
 *         登陆界面
 */
public class Login extends BaseActivity {
    private String loginName, loginPwd;
    private LoginResult loginResult;

    @ViewInject(R.id.bt_login)
    private Button bt_login;
    @ViewInject(R.id.bt_reg)
    private Button bt_reg;
    @ViewInject(R.id.login_cb)
    private CheckBox login_cb;

    //	登录账号和密码
    @ViewInject(R.id.et_loginName)
    private EditText et_loginName;

    @ViewInject(R.id.et_loginPwd)
    private EditText et_loginPwd;

    private boolean ifJump=false;//是否跳转
    @Override
    public int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("登录");
        setBack();
        if(getIntent().getStringExtra("login")!=null&&!getIntent().getStringExtra("login").equals("")){
            ifJump=true;
        }
    }

    @Override
    public void setListener() {
        bt_login.setOnClickListener(this);
        bt_reg.setOnClickListener(this);
        login_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferencesUtils.setbooleanSharedPreferences(getApplicationContext(),"ifAutoLogin",isChecked);
            }
        });

        et_loginName.setText(SharedPreferencesUtils.getSharedPreferences(getApplicationContext(),"login_name"));
        et_loginPwd.setText(SharedPreferencesUtils.getSharedPreferences(getApplicationContext(),"login_pwd"));
        if(!TextUtils.isEmpty(SharedPreferencesUtils.getSharedPreferences(getApplicationContext(),"login_name"))){
            login_cb.setChecked(true);
        }else login_cb.setChecked(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:

                loginName = et_loginName.getText().toString().trim();
                loginPwd = et_loginPwd.getText().toString().trim();
                if (TextUtils.isEmpty(loginName)) {
                    ToastUtils.show(getApplicationContext(), "账号不能为空！");
                    return;
                } else if (TextUtils.isEmpty(loginPwd)) {
                    ToastUtils.show(getApplicationContext(), "密码不能为空！");
                    return;
                }
                loginApp(loginName, loginPwd);
                break;
            case R.id.bt_reg:
                intent = new Intent(Login.this, RegFirst.class);
                startActivity(intent);
                break;
        }

    }


    private void loginApp(String login_name, String login_pwd) {
        if (!CommonUtils.isNetworkAvailable(this)) {
            ToastUtils.show(getApplicationContext(), "请检查网络！");
            return;
        }
        dialog();
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
                    /*    //是否记住密码
                        if (login_cb.isChecked()) {
                            //获取用户名和密码，保存，下次登陆时候自动填上
                            SharedPreferencesUtils.setbooleanSharedPreferences(getApplicationContext(), "ifAutoLogin", true);
                        } else
                            SharedPreferencesUtils.setbooleanSharedPreferences(getApplicationContext(), "ifAutoLogin", false);*/

                        loginResult = JSON.parseObject(json.getString("data"), LoginResult.class);
                        if (loginResult != null) {
                            //保存用户登录信息
                            saveLoginInfor();
//                            intent = new Intent(Login.this, TotalActivity.class);
//                            startActivity(intent);
                        }
                    } else ToastUtils.show(getApplicationContext(), json.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("TAG", "onError=" + ex.getMessage());

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismiss();
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });


    }

    private void saveLoginInfor() {

        SharedPreferencesUtils.setbooleanSharedPreferences(getApplicationContext(), "ifAutoLogin", true);
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "agent", loginResult.agent);
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "phone", loginResult.phone);
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "photo", loginResult.photo);
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "real_name", loginResult.real_name);
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "reg_create", loginResult.reg_create);
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "sex", loginResult.sex);
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "team", loginResult.team);
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "user_token", loginResult.user_token);

        if(login_cb.isChecked()){
            SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "login_pwd", loginPwd);
            SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "login_name", loginResult.login_name);
        }else {
            SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "login_pwd", "");
            SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "login_name", "");
        }
        Constant.USER_TOKEN = loginResult.user_token;

        //发送广播刷新显示价格
        intent = new Intent(Login.this, TotalActivity.class);
//        intent.setAction("REFRESH");
//        sendBroadcast(intent);
        if(ifJump){
            startActivity(intent);
        }else
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //到此页面判断是否上次记录了账号和密码
        if (SharedPreferencesUtils.getbooleanSharedPreferences(getApplicationContext(), "ifAutoLogin")) {
//            记住密码情况下
            login_cb.setChecked(true);
            et_loginName.setText(SharedPreferencesUtils.getSharedPreferences(getApplicationContext(),"login_name"));
            et_loginPwd.setText(SharedPreferencesUtils.getSharedPreferences(getApplicationContext(),"login_pwd"));

        } else{
            login_cb.setChecked(false);
        }
    }
}
