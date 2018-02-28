package com.ipd.paylove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ipd.paylove.R;
import com.ipd.paylove.base.BaseActivity;
import com.ipd.paylove.gloable.Constant;
import com.ipd.paylove.utils.ImageCatchUtil;
import com.ipd.paylove.utils.SharedPreferencesUtils;
import com.ipd.paylove.utils.ToastUtils;

import org.xutils.view.annotation.ViewInject;

/*********
 * 设置页面
 *******/
public class Set extends BaseActivity {
    @ViewInject(R.id.rl_guid)
    private RelativeLayout rl_guid;
    @ViewInject(R.id.rl_aboutUs)
    private RelativeLayout rl_aboutUs;
    @ViewInject(R.id.rl_clear)
    private RelativeLayout rl_clear;
    @ViewInject(R.id.tv_clear)
    private TextView tv_clear;
    @ViewInject(R.id.bt_loginOut)
    private Button bt_loginOut;

    @Override
    public int setLayout() {
        return R.layout.activity_set;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setBack();
        setTopTitle("设置");
        tv_clear.setText(ImageCatchUtil.getInstance().getCacheSize(Glide.getPhotoCacheDir(this)));
    }

    @Override
    public void setListener() {
        rl_clear.setOnClickListener(this);
        rl_aboutUs.setOnClickListener(this);
        bt_loginOut.setOnClickListener(this);
        rl_guid.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_aboutUs:
                intent = new Intent(Set.this, AboutUs.class);
                startActivity(intent);

                break;
            case R.id.rl_clear:
                tv_clear.setText("0M");
                ImageCatchUtil.getInstance().clearImageDiskCache();
                ToastUtils.show(getApplicationContext(), "缓存清除成功!");
                break;
            case R.id.bt_loginOut:
                loginOut();
                break;
            case R.id.rl_guid:
//                使用说明
                startActivity(new Intent(getApplicationContext(),UserGuid.class));
                break;
        }
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
        SharedPreferencesUtils.setbooleanSharedPreferences(getApplicationContext(), "ifAutoLogin", false);
        SharedPreferencesUtils.setSharedPreferences(Set.this, "user_token", "");
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "agent", "");
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "phone", "");
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "photo", "");
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "real_name", "");
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "reg_create", "");
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "sex", "");
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "team", "");
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "user_token", "");
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "login_name", "");
        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "login_pwd", "");

        Constant.USER_TOKEN = "";
        startActivity(new Intent(getApplicationContext(),Login.class));

        finish();

    }
}
