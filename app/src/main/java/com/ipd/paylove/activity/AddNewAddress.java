package com.ipd.paylove.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ipd.paylove.R;
import com.ipd.paylove.adapter.AdapterBank;
import com.ipd.paylove.base.BaseActivity;
import com.ipd.paylove.entity.EntityAddress;
import com.ipd.paylove.gloable.Constant;
import com.ipd.paylove.utils.AddressData;
import com.ipd.paylove.utils.CommonUtils;
import com.ipd.paylove.utils.PopupUtils;
import com.ipd.paylove.utils.ToastUtils;
import com.ipd.paylove.widget.WheelView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Arrays;

/********
 * 添加收获地址界面
 ************/
public class AddNewAddress extends BaseActivity {
    private EntityAddress entityAddress;
    private String addr_province = "", addr_name = "", default_status = "", uname = "", phone = "";
    @ViewInject(R.id.tv_selectssq)
    private TextView tv_selectssq;
    @ViewInject(R.id.et_contact)
    private EditText et_contact;
    @ViewInject(R.id.et_phone)
    private EditText et_phone;
    /*  @ViewInject(R.id.et_areacode)
      private EditText et_areacode;*/
    @ViewInject(R.id.et_detailaddress)
    private EditText et_detailaddress;
    @ViewInject(R.id.bt_save)
    private Button bt_save;

    @ViewInject(R.id.checkbox)
    private CheckBox checkBox;

    @Override
    public int setLayout() {
        return R.layout.activity_add_new_address;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setBack();
        entityAddress = (EntityAddress) getIntent().getSerializableExtra("AddressEntity");
        if (entityAddress != null) {
            setTopTitle("修改收获地址");
            setData();
        } else setTopTitle("新建收获地址");


    }

    @Override
    public void setListener() {
        tv_selectssq.setOnClickListener(this);
        bt_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_selectssq:
                //选择省，市，区
                showProvince();
                break;
            case R.id.bt_save:
                uname = et_contact.getText().toString().trim();
                phone = et_phone.getText().toString().trim();
                addr_province = tv_selectssq.getText().toString().trim();
                addr_name = et_detailaddress.getText().toString().trim();
                //保存地址
                if (TextUtils.isEmpty(uname)) {
                    ToastUtils.show(getApplicationContext(), "联系人不能为空！");
                    return;
                }
                if (!CommonUtils.isMobileNO(phone)) {
                    ToastUtils.show(getApplicationContext(), "手机号码格式错误！");
                    return;
                }
                if (TextUtils.isEmpty(addr_province)) {
                    ToastUtils.show(getApplicationContext(), "请选择省市区！");
                    return;
                }
                if (TextUtils.isEmpty(addr_name)) {
                    ToastUtils.show(getApplicationContext(), "请填写详细地址！");
                    return;
                }
                addAddress();
                break;


        }
    }

    /******
     * 选择省市区
     *****/
    private void choiceCity() {
        PopupUtils.selectCity(this, new PopupUtils.OnFinishListener() {
            @Override
            public void onFinish(WheelView view) {
            }

            @Override
            public void onFinish(String country, String city, String ccity) {
                tv_selectssq.setText(country);
            }
        });
    }


    /*******
     * 添加收货地址,或者是修改收货地址
     ******/
    private void addAddress() {
        dialog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "UserAddress/saveUserAddress");
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        params.addBodyParameter("addr_province", addr_province);
        params.addBodyParameter("addr_name", addr_name);
        params.addBodyParameter("default_status", default_status);
        params.addBodyParameter("uname", uname);
        params.addBodyParameter("phone", phone);
        if (entityAddress != null) {
            params.addBodyParameter("aid", entityAddress.aid);
        }
        if (checkBox.isChecked()) {
            params.addBodyParameter("default_status", "1");
        } else
            params.addBodyParameter("default_status", "0");

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TAG", "result=" + result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        ToastUtils.show(getApplicationContext(), json.getString("msg"));
                        finish();

                    } else {
                        CommonUtils.loginOverrTime(json.getString("msg"), AddNewAddress.this,true);
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
        et_contact.setText(entityAddress.uname);
        et_phone.setText(entityAddress.phone);
        //   et_areacode.setText(entityAddress.);
        et_detailaddress.setText(entityAddress.addr_name);
        tv_selectssq.setText(entityAddress.addr_prov);
        if (entityAddress.default_status.equals("0")) {
            checkBox.setChecked(false);
        } else checkBox.setChecked(true);
        Log.i("TAG", "check=" + entityAddress.default_status);
    }


    //
    private void showProvince() {

        final Dialog dialog = new Dialog(AddNewAddress.this,R.style.dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view=View.inflate(AddNewAddress.this,R.layout.layout_dialog_province,null);
        ListView listView= (ListView) view.findViewById(R.id.listView);
        RelativeLayout relativeLayout= (RelativeLayout) view.findViewById(R.id.rl_p);
        final AdapterBank adapterBank=new AdapterBank(getApplicationContext(), Arrays.asList(AddressData.PROVINCES));
        listView.setAdapter(adapterBank);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_selectssq.setText((CharSequence) adapterBank.getItem(position));
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.PopupAnimation);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = (AddNewAddress.this).getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();


    }
}
