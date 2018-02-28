package com.ipd.paylove.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ipd.paylove.R;
import com.ipd.paylove.adapter.ShopCarAdapter;
import com.ipd.paylove.base.BaseActivity;
import com.ipd.paylove.entity.EnityOne;
import com.ipd.paylove.entity.EntityAddress;
import com.ipd.paylove.entity.EntityName;
import com.ipd.paylove.entity.ShopCarEntity;
import com.ipd.paylove.gloable.Constant;
import com.ipd.paylove.utils.Constants;
import com.ipd.paylove.utils.ToastUtils;
import com.ipd.paylove.view.MyListView;
import com.ipd.paylove.view.NamePopupWindow;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/******
 * 确认订单界面
 *******/
public class SureOrder extends BaseActivity {
    @ViewInject(R.id.et_mark)
    private EditText et_mark;

    @ViewInject(R.id.ll_p)
    private RelativeLayout ll_p;
    @ViewInject(R.id.rl_give)
    private RelativeLayout rl_give;
    @ViewInject(R.id.tv_give)
    private TextView tv_give;
    @ViewInject(R.id.tv_AllMoney)
    private TextView tv_AllMoney;
    @ViewInject(R.id.myList)
    private MyListView listView;
    @ViewInject(R.id.scrollView)
    private ScrollView scrollView;
    @ViewInject(R.id.rl_select)
    private RelativeLayout rl_select;
    @ViewInject(R.id.name)
    private TextView name;
    @ViewInject(R.id.phone)
    private TextView phone;
    @ViewInject(R.id.detail_address)
    private TextView detail_address;
    private ShopCarAdapter adapterSureOrder;
    private List<ShopCarEntity> data = new ArrayList<>();
    private List<ShopCarEntity> Tempdata = new ArrayList<>();

    @ViewInject(R.id.bt_ok)
    private Button bt_ok;
    @ViewInject(R.id.et1)
    private EditText et_day;
    private EntityAddress entityAddress;
    private List<EntityName> names = new ArrayList<>();
    private NamePopupWindow namePopupWindow;
    private EnityOne enityOne;

    //订单参数
    private String car_id = "", day = "0", dk_id = "1";

    @Override
    public int setLayout() {
        return R.layout.activity_sure_order;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setBack();
        setTopTitle("确认订单");
        tv_AllMoney.setText(getIntent().getStringExtra("money"));
        Tempdata = (List<ShopCarEntity>) getIntent().getSerializableExtra("data");
        scrollView.smoothScrollTo(0, 20);
        adapterSureOrder = new ShopCarAdapter(getApplicationContext(), data);
        listView.setAdapter(adapterSureOrder);
        setData();
        getUserDefaultAddress();

    }

    @Override
    public void setListener() {
        rl_select.setOnClickListener(this);
        rl_give.setOnClickListener(this);
        bt_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_select:
                //选取收货地址
                intent = new Intent(getApplicationContext(), Address.class);
                intent.putExtra("select", true);
                startActivityForResult(intent, Constants.SELECT_ADDRESS_REQUEST_CODE);
                break;
            case R.id.rl_give:
                //选择打款人
                namePopupWindow = new NamePopupWindow(this, Arrays.asList(enityOne.dk_people), tv_give);
                namePopupWindow.showAtLocation(ll_p, Gravity.BOTTOM, 0, 0);
                break;

            case R.id.bt_ok:
                //生成订单
                day = et_day.getText().toString().trim();
                dk_id = NamePopupWindow.NameId;
//                day 要《=30
                if(Integer.parseInt(day)>30){
                    ToastUtils.show(getApplicationContext(),"延迟发货时间不能超过30天！");
                    return;
                }

//                判断
                if (entityAddress == null||entityAddress.aid.equals("0")) {
                    ToastUtils.show(getApplicationContext(), "请选择收货地址！");
                    return;
                }
                if (tv_give.getText().toString().trim().equals("")) {
                    ToastUtils.show(getApplicationContext(), "请选择打款给谁！");
                    return;
                }
                sureOrder();
                break;
        }
    }

    private void setData() {
        data.clear();
        for (int i = 0; i < Tempdata.size(); i++) {
            if (Tempdata.get(i).isCheck) {
                data.add(Tempdata.get(i));
                if (car_id.equals("")) {
                    car_id = Tempdata.get(i).car_id;
                } else {
                    car_id = car_id + "," + Tempdata.get(i).car_id;
                }
            }

        }
        adapterSureOrder.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            getUserDefaultAddress();
            Log.i("TAG","======");
        }else {
            Log.i("TAG","===111==="+requestCode);

        }

        if (resultCode == Constants.SELECT_ADDRESS_RESULT_CODE && requestCode == Constants.SELECT_ADDRESS_REQUEST_CODE) {
            entityAddress = (EntityAddress) data.getSerializableExtra("AddressEntity");
            setAddress(entityAddress);
            Log.i("TAG", "////////////");
        }
        //如果选择地址时新建了地址，那么重新查询

    }


    /*********
     * 获取默认地址或者第一条
     *********/
    private void getUserDefaultAddress() {
        RequestParams params = new RequestParams(Constant.BASE_URL + "UserAddress/getDefaultAddress");
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TAGS", "res=" + result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        enityOne = JSON.parseObject(json.getString("data"), EnityOne.class);
                        entityAddress = enityOne.address;
                        setAddress(entityAddress);

                    }else {

                        ToastUtils.show(getApplicationContext(),json.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("TAG", "ex=" + ex.getLocalizedMessage());

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }


    /*********
     * 生成订单
     *********/
    private void makeOrder() {
        dialog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "Order/bindOrder");
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        params.addBodyParameter("car_id", car_id);
        params.addBodyParameter("address_id", entityAddress.aid);
        params.addBodyParameter("day", day);
        params.addBodyParameter("dk_id", dk_id);
        params.addBodyParameter("order_desc",et_mark.getText().toString().trim());
//        Log.i("TAGS", "user_token=" + Constant.USER_TOKEN + " /car_id" + car_id + "/address_id=" + entityAddress.aid + "/day=" + day + "/dk_id=" + dk_id);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TAG", "result=" + result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        ToastUtils.show(getApplicationContext(), "订单生成成功！");
                        startActivity(new Intent(SureOrder.this, AllOrder.class));
                        finish();
                    } else {
                        ToastUtils.show(getApplicationContext(), json.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("TAG", "ex=" + ex.getLocalizedMessage());

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

    private void setAddress(EntityAddress entityAddress) {
        if (entityAddress != null&&!entityAddress.aid.equals("0")) {
            name.setText(entityAddress.uname);
            phone.setText(entityAddress.phone);
            detail_address.setText(entityAddress.addr_prov + entityAddress.addr_name);
        }else {
            name.setText("");
            phone.setText("");
            detail_address.setText("");
        }
    }


    public void sureOrder() {
        final Dialog dialog = new Dialog(SureOrder.this, R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(SureOrder.this);
        View view = inflater.inflate(R.layout.sure_order_dialog, null, true);
        final TextView tv_sure = (TextView) view.findViewById(R.id.tv_sure);
        final TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_desc);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                makeOrder();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }



}
