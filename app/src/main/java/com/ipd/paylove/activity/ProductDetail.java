package com.ipd.paylove.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ipd.paylove.R;
import com.ipd.paylove.adapter.AdapterDetail;
import com.ipd.paylove.base.BaseActivity;
import com.ipd.paylove.entity.OrderDetailEntity;
import com.ipd.paylove.entity.OrderEntity;
import com.ipd.paylove.gloable.Constant;
import com.ipd.paylove.utils.ToastUtils;
import com.ipd.paylove.view.MyListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Arrays;

/***********
 * 订单明细界面
 ***********/
public class ProductDetail extends BaseActivity {
    @ViewInject(R.id.myList)
    private MyListView myListView;
    private AdapterDetail adapterDetail;


    private OrderDetailEntity orderDetailEntity;
    private OrderEntity orderEntity;
    @ViewInject(R.id.tv_top)
    private TextView tv_top;
    @ViewInject(R.id.tv_orderNumber)
    private TextView tv_ddh;
    @ViewInject(R.id.tv_state)
    private TextView tv_zt;
    @ViewInject(R.id.tv_money)
    private TextView tv_ddje;
    @ViewInject(R.id.tv_shrName)
    private TextView tv_name;
    @ViewInject(R.id.tv_phone)
    private TextView tv_phone;
    @ViewInject(R.id.tv_address)
    private TextView tv_address;
    @ViewInject(R.id.tv_payName)
    private TextView tv_payName;
    @ViewInject(R.id.tv_creatTime)
    private TextView tv_creatTime;
    @ViewInject(R.id.tv_orderDesc)
    private TextView tv_orderDesc;
    @ViewInject(R.id.tv_zdName)
    private TextView tv_zdName;
    @ViewInject(R.id.tv_groupName)
    private TextView tv_groupName;
    @ViewInject(R.id.tv_xdsh)
    private TextView tv_xdsh;
    @ViewInject(R.id.tv_shms)
    private TextView tv_shms;

    @ViewInject(R.id.tv_wuliu)
    private TextView tv_wuliu;
    @Override
    public int setLayout() {
        return R.layout.activity_product_detail;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setBack();
        setTopTitle("查看明细");


        orderEntity = (OrderEntity) getIntent().getSerializableExtra("entity");
        getData();
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }


    /*********
     * 查询订单
     *********/
    private void getData() {
        dialog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "Order/orderInfo");
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        params.addBodyParameter("order_no", orderEntity.order_no);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TAG", "res=" + result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        orderDetailEntity = JSON.parseObject(json.getString("data"), OrderDetailEntity.class);
                        adapterDetail = new AdapterDetail(ProductDetail.this, Arrays.asList(orderDetailEntity.goods));
                        myListView.setAdapter(adapterDetail);
                        setData();
                    } else {
                        ToastUtils.show(getApplicationContext(), json.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.i("TAG", "ex=" + ex.getLocalizedMessage());

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


    private void setData(){
        tv_top.setText("尊敬的客户,您的订单正在"+orderDetailEntity.isverify);
        tv_ddh.setText(orderDetailEntity.order_no);
//        状态
        switch (orderDetailEntity.order_status){
            case "W":
                tv_zt.setText("待审核");
                break;
            case "F":
                tv_zt.setText("待打单");
                break;
            case "P":
                tv_zt.setText("已打单");
                break;
            case "H":
                tv_zt.setText("已发货");
                break;
            case "VE":
                tv_zt.setText("审核未通过");
                break;
            case "R":
                tv_zt.setText("待撤单审核");
                break;
            case "RS":
                tv_zt.setText("撤单成功");
                break;
            case "T":
                tv_zt.setText("待退单审核");
                break;
            case "TS":
                tv_zt.setText("退单成功");
                break;
            case "TE":
                tv_zt.setText("退单驳回");
                break;
        }


        tv_ddje.setText(orderDetailEntity.allprice);
        tv_name.setText(orderDetailEntity.send_name);
        tv_phone.setText(orderDetailEntity.send_phone);
        tv_address.setText(orderDetailEntity.send_addrprov+orderDetailEntity.send_addr);
        tv_payName.setText(orderDetailEntity.dk_name);
        tv_creatTime.setText(orderDetailEntity.create_time);
        tv_orderDesc.setText(orderDetailEntity.order_desc);
        tv_zdName.setText(orderDetailEntity.zd_name);
        tv_groupName.setText(orderDetailEntity.team_name);
        tv_xdsh.setText(orderDetailEntity.isverify);
        tv_shms.setText(orderDetailEntity.verify_result);
        tv_wuliu.setText(orderDetailEntity.send_wl);



    }


}
