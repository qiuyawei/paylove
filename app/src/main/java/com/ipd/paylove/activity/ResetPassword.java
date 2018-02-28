package com.ipd.paylove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ipd.paylove.R;
import com.ipd.paylove.base.BaseActivity;
import com.ipd.paylove.gloable.Constant;
import com.ipd.paylove.utils.SharedPreferencesUtils;
import com.ipd.paylove.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/************
 * 重置密码
 *********/
public class ResetPassword extends BaseActivity {
    @ViewInject(R.id.et_oldPassword)
    private EditText et_oldPassword;
    @ViewInject(R.id.et_pwd1)
    private EditText et_pwd1;
    @ViewInject(R.id.et_pwd2)
    private EditText et_pwd2;

    @Override
    public int setLayout() {
        return R.layout.activity_reset_password;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setBack();
        setTopTitle("修改密码");
        setRightButton("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_oldPassword.getText().toString().trim().equals("")) {
                    ToastUtils.show(getApplicationContext(), "原密码不能为空!");
                    return;
                }
                if (et_pwd1.getText().toString().trim().equals("")) {
                    ToastUtils.show(getApplicationContext(), "新密码不能为空!");
                    return;
                }
                if (et_pwd1.getText().toString().trim().length() < 6) {
                    ToastUtils.show(getApplicationContext(), "密码格式错误,密码不得少于六位!");
                    return;
                }
                if (!et_pwd1.getText().toString().trim().equals(et_pwd2.getText().toString().trim())) {
                    ToastUtils.show(getApplicationContext(), "两次输入的密码不一致!");
                    return;
                }
                resetLoginPwd(et_pwd1.getText().toString().trim(),et_oldPassword.getText().toString().trim());
            }
        });
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }


    /*******
     * 修改登录密码
     ******/
    private void resetLoginPwd(String password, String old_password) {
        dialog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "UserUpdate/updateLoginPassword");
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        params.addBodyParameter("password", password);
        params.addBodyParameter("old_password", old_password);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        ToastUtils.show(getApplicationContext(), "密码修改成功！");
                        loginOut();
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

    /****
     * 退出登录
     *****/

    private void loginOut() {
        intent=new Intent();
        intent.setAction("Cancel_price");
        sendBroadcast(intent);
       /* List<Activity> activityList = MyApplication.activityList;
        for(int i=0;i<activityList.size();i++){
            activityList.get(i).finish();
        }*/
        startActivity(new Intent(getApplicationContext(),Login.class));
        SharedPreferencesUtils.setbooleanSharedPreferences(getApplicationContext(), "ifAutoLogin", false);
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "user_token", "");
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "agent", "");
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "phone", "");
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "photo", "");
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "real_name", "");
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "reg_create", "");
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "sex", "");
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "team", "");
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "user_token", "");
//        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "login_name", "");
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "login_pwd", "");

        Constant.USER_TOKEN = "";
        finish();

    }
}
