package com.ipd.paylove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ipd.paylove.R;
import com.ipd.paylove.base.BaseActivity;
import com.ipd.paylove.gloable.Constant;
import com.ipd.paylove.utils.CommonUtils;
import com.ipd.paylove.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/********
 * 修改真实姓名
 * <p/>
 * 以及详细信息页面里的修改操作
 ********/
public class Reset extends BaseActivity {
    @ViewInject(R.id.et_reallName)
    private EditText et_reallName;
    private String reset = "";
    private String body = "";

    private String phone= "",id_card= "",birthday= "",email= "",zipcord= "",bank_name= "",bank_account= "",account_holder= "",bank_addr= "";

    @Override
    public int setLayout() {
        return R.layout.activity_reset_name;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        reset = getIntent().getStringExtra("reset");
        setBack();
        switch (reset) {
            case "resetName":
                body = "真实姓名";
                et_reallName.setHint("请输入您的真实姓名");
                et_reallName.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case "resetNick":
                body = "用户名";
                et_reallName.setHint("请输入您的用户名");
                et_reallName.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case "resetPhone":
                body = "联系手机";
                et_reallName.setHint("请输入您的手机号码");
                et_reallName.setInputType(InputType.TYPE_CLASS_PHONE);

                break;
            case "resetID":
                body = "身份证号";
                et_reallName.setHint("请输入您的身份证号码");
                et_reallName.setInputType(InputType.TYPE_CLASS_NUMBER);

                break;
            case "resetBirthdy":
                body = "生日";
                et_reallName.setHint("请输入您的生日");
                et_reallName.setInputType(InputType.TYPE_CLASS_TEXT);

                break;
            case "resetEmail":
                body = "邮箱";
                et_reallName.setHint("请输入您的邮箱");
                et_reallName.setInputType(InputType.TYPE_CLASS_TEXT);

                break;
            case "resetCode":
                body = "邮政编码";
                et_reallName.setHint("请输入您所在地的邮政编码");
                et_reallName.setInputType(InputType.TYPE_CLASS_NUMBER);

                break;
            case "resetBankcount":
                body = "银行账号";
                et_reallName.setHint("请输入您的银行卡号");
                et_reallName.setInputType(InputType.TYPE_CLASS_NUMBER);

                break;
            case "resetKhr":
                body = "开户人";
                et_reallName.setHint("请输入银行卡的开户人姓名");
                et_reallName.setInputType(InputType.TYPE_CLASS_TEXT);

                break;
            case "resetKhhaddress":
                body = "开户行地址";
                et_reallName.setHint("请输入开户行地址");
                et_reallName.setInputType(InputType.TYPE_CLASS_TEXT);

                break;

        }

        setTopTitle(body);
        setRightButton("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_reallName.getText().toString().trim().equals("")) {
                    ToastUtils.show(getApplicationContext(), body + "不能为空!");
                    return;
                }
                if (reset.equals("resetName")||reset.equals("resetNick")) {
                    intent = new Intent(getApplicationContext(), PersonInfor.class);
                    intent.putExtra("resetContent", et_reallName.getText().toString().trim());
                    //设置返回数据
                    Reset.this.setResult(RESULT_OK, intent);
                    Reset.this.finish();
                    //关闭Activity
                } else {
                    switch (reset) {
                        case "resetPhone":
                            phone=et_reallName.getText().toString().trim();
                            break;
                        case "resetID":
                            id_card=et_reallName.getText().toString().trim();
                            break;
                        case "resetBirthdy":
                            birthday=et_reallName.getText().toString().trim();
                            break;
                        case "resetEmail":
                            email=et_reallName.getText().toString().trim();
                            break;
                        case "resetCode":
                            zipcord=et_reallName.getText().toString().trim();
                            break;
                        case "resetBankcount":
                            bank_account=et_reallName.getText().toString().trim();
                            break;
                        case "resetKhr":
                            account_holder=et_reallName.getText().toString().trim();
                            break;
                        case "resetKhhaddress":
                            bank_addr=et_reallName.getText().toString().trim();
                            break;

                    }
                    resetPersonInfor();
                }



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
     * 修改用户其他信息（详细信息）
     ******/

  /*  user_token	登录时返回的user_token
    phone	手机
    id_card	身份证号
    birthday	生日
    email	邮箱
    zipcord	邮编
    bank_name	开户行名称
    bank_account	银行账号
    account_holder	开户人姓名
    bank_addr	开户行地址*/
    private void resetPersonInfor() {
        dialog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "UserUpdate/updateOtherInfo");
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("id_card", id_card);
        params.addBodyParameter("birthday", birthday);
        params.addBodyParameter("email", email);
        params.addBodyParameter("zipcord", zipcord);
        params.addBodyParameter("bank_name", bank_name);
        params.addBodyParameter("bank_account", bank_account);
        params.addBodyParameter("account_holder", account_holder);
        params.addBodyParameter("bank_addr", bank_addr);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        ToastUtils.show(getApplicationContext(), "改成功！");
                        finish();
                    } else
                        CommonUtils.loginOverrTime(json.getString("msg"),Reset.this,true);

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
}
