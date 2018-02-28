package com.ipd.paylove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ipd.paylove.R;
import com.ipd.paylove.base.BaseActivity;
import com.ipd.paylove.entity.PersonInforResult;
import com.ipd.paylove.gloable.Constant;
import com.ipd.paylove.utils.CommonUtils;
import com.ipd.paylove.utils.SelectPicPopupWindow2;
import com.ipd.paylove.utils.SharedPreferencesUtils;
import com.ipd.paylove.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/******
 * 个人信息
 ******/
public class PersonInfor extends BaseActivity {
    public static int REAL_NAME_RESET_REQUEST_CODE = 0;
    public static int NICK_RESET_REQUEST_CODE = 1;


    private PersonInforResult personInforResult;

    //头像
    @ViewInject(R.id.rl_photo)
    private RelativeLayout rl_photo;
    @ViewInject(R.id.iv_photo)
    private ImageView iv_photo;
    //用户昵称
    @ViewInject(R.id.rl_loginName)
    private RelativeLayout rl_loginName;
    @ViewInject(R.id.tv_nickName)
    private TextView tv_nickName;
    //性别
    @ViewInject(R.id.rl_sex)
    private RelativeLayout rl_sex;
    @ViewInject(R.id.tv_sex)
    private TextView tv_sex;
    //真实姓名
    @ViewInject(R.id.rl_reallName)
    private RelativeLayout rl_reallName;
    @ViewInject(R.id.tv_realName)
    private TextView tv_reallName;
    //注册时间
    @ViewInject(R.id.rl_creatTiem)
    private RelativeLayout rl_creatTime;
    @ViewInject(R.id.tv_regTiem)
    private TextView tv_regTime;
    //修改密码
    @ViewInject(R.id.rl_resetPassword)
    private RelativeLayout rl_resetPassword;
    //推荐代理商
    @ViewInject(R.id.tv_agent)
    private TextView tv_agent;
    //团队
    @ViewInject(R.id.tv_group)
    private TextView tv_group;

    @ViewInject(R.id.ll_parent)
    private LinearLayout ll_parent;
    private SelectPicPopupWindow2 menuWindow;

    @Override
    public int setLayout() {
        return R.layout.activity_person_infor;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setBack();
        setTopTitle("基础信息");
        setData();

    }

    @Override
    public void setListener() {
//        rl_sex.setOnClickListener(this);
//        rl_reallName.setOnClickListener(this);
        rl_resetPassword.setOnClickListener(this);
      //  rl_loginName.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //弹出选择性别
            case R.id.rl_sex:
                menuWindow = new SelectPicPopupWindow2(this, itemsOnClick);
                menuWindow.showAtLocation(ll_parent, Gravity.BOTTOM, 0, 0);
                break;

            case R.id.rl_reallName:
                intent = new Intent(getApplicationContext(), Reset.class);
                intent.putExtra("reset", "resetName");
                startActivityForResult(intent, REAL_NAME_RESET_REQUEST_CODE);
                break;
            case R.id.rl_resetPassword:
                intent = new Intent(getApplicationContext(), ResetPassword.class);
                startActivity(intent);
                break;
           /* case R.id.rl_loginName:
                //修改登录姓名
                intent = new Intent(getApplicationContext(), Reset.class);
                intent.putExtra("reset", "resetNick");
                startActivityForResult(intent, NICK_RESET_REQUEST_CODE);
                break;*/
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REAL_NAME_RESET_REQUEST_CODE && resultCode == RESULT_OK) {
            resetReallName(data.getStringExtra("resetContent"));
        } else if (requestCode == NICK_RESET_REQUEST_CODE && resultCode == RESULT_OK) {
            resetLoginName(data.getStringExtra("resetContent"));
        }

    }


    private View.OnClickListener itemsOnClick = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            // 隐藏弹出窗口
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.takePhotoBtn:// 男
                    resetSex("男");
                    break;
                case R.id.pickPhotoBtn:// 女
                    resetSex("女");
                    break;
                case R.id.cancelBtn:// 取消
                    break;
                default:
                    break;
            }
        }
    };


    /*******
     * 修改用户真实姓名
     *******/
    private void resetReallName(final String real_name) {
        dialog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "UserUpdate/updateRealName");
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        params.addBodyParameter("real_name", real_name);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "real_name",real_name);
                        ToastUtils.show(getApplicationContext(), "真实姓名修改成功！");
                        tv_reallName.setText(real_name);
                    }else {
                        CommonUtils.loginOverrTime(json.getString("msg"),PersonInfor.this,true);
                    }
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

    /*******
     * 修改用户性别
     *******/
    private void resetSex(final String sex) {
        dialog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "UserUpdate/updateSex");
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        params.addBodyParameter("sex", sex);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TAG", "result=" + result);

                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        ToastUtils.show(getApplicationContext(), "性别修改成功！");
                        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "sex",sex);
                        tv_sex.setText(sex);
                    }else {
                        CommonUtils.loginOverrTime(json.getString("msg"),PersonInfor.this,true);
                    }
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

    /*******
     * 修改用户名
     *******/
    private void resetLoginName(final String real_name) {
        dialog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "UserUpdate/updateLoginName");
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        params.addBodyParameter("login_name", real_name);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        SharedPreferencesUtils.setSharedPreferences(getApplicationContext(), "login_name", real_name);
                        ToastUtils.show(getApplicationContext(), "用户名修改成功！");
                        tv_nickName.setText(real_name);
                    }else {
                        CommonUtils.loginOverrTime(json.getString("msg"),PersonInfor.this,true);
                    }
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

    private void setData() {
        x.image().bind(iv_photo, SharedPreferencesUtils.getSharedPreferences(getApplicationContext(), "photo"));
        tv_sex.setText(SharedPreferencesUtils.getSharedPreferences(getApplicationContext(), "sex"));
        tv_regTime.setText(SharedPreferencesUtils.getSharedPreferences(getApplicationContext(), "reg_create"));
        tv_reallName.setText(SharedPreferencesUtils.getSharedPreferences(getApplicationContext(), "real_name"));
        tv_agent.setText(SharedPreferencesUtils.getSharedPreferences(getApplicationContext(), "agent"));
        tv_group.setText(SharedPreferencesUtils.getSharedPreferences(getApplicationContext(), "team"));
        tv_nickName.setText(SharedPreferencesUtils.getSharedPreferences(getApplicationContext(), "login_name"));
    }
}
