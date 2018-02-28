package com.ipd.paylove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.ipd.paylove.R;
import com.ipd.paylove.base.BaseActivity;
import com.ipd.paylove.entity.EnityOne;
import com.ipd.paylove.entity.EntityAddress;
import com.ipd.paylove.entity.JsonEnity;
import com.ipd.paylove.entity.OrderEntity;
import com.ipd.paylove.entity.OrderEntityInner;
import com.ipd.paylove.gloable.Constant;
import com.ipd.paylove.utils.CommonUtils;
import com.ipd.paylove.utils.Constants;
import com.ipd.paylove.utils.DialogUtils;
import com.ipd.paylove.utils.ToastUtils;
import com.ipd.paylove.view.MyListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/******
 * 确认订单界面
 *******/
public class SplitOrder extends BaseActivity {
    @ViewInject(R.id.ll_p)
    private RelativeLayout ll_p;
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
    private List<OrderEntityInner> data = new ArrayList<>();
    private List<OrderEntityInner> Tempdata = new ArrayList<>();

    private SplitAdapter adapter;
    @ViewInject(R.id.bt_ok)
    private Button bt_ok;

    private EntityAddress entityAddress;
    private EnityOne enityOne;
    private OrderEntity orderEntity;
    private JsonEnity jsonEnity;
    private HashMap<String, String> jsonEnityList = new HashMap<>();
    //订单参数
    private String car_id = "", day = "0", dk_id = "1";
    private int buyCount = 0, temp = 0, orgin = 0;//数量
    //    判断拆分订单至少一个产品数量不为0 ，方可去拆分
    private boolean canDo = false;

    @Override
    public int setLayout() {
        return R.layout.activity_split_order;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        data.clear();
        Tempdata.clear();
        orderEntity = (OrderEntity) getIntent().getSerializableExtra("entity");
        data.addAll(Arrays.asList(orderEntity.goods));
//        虚拟一条数据出来
        for (int i = 0; i < data.size(); i++) {
            data.get(i).orginNumber = 0+"";
        }
        Tempdata.addAll(data);

        setBack();
        setTopTitle("及时发货");
        scrollView.smoothScrollTo(0, 20);
        getUserDefaultAddress();
        adapter = new SplitAdapter(Tempdata);
        listView.setAdapter(adapter);
    }

    @Override
    public void setListener() {
        rl_select.setOnClickListener(this);
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

            case R.id.bt_ok:
                //先判断是否发货的订单数量都是0  ，那么没必要生成订单，提示用户重新选择
                for (int i = 0; i < Tempdata.size(); i++) {
                    if (Integer.parseInt(Tempdata.get(i).orginNumber) > 0) {
                        canDo = true;
                    }
                }
                //生成订单
                if (canDo)
                    splitOrder();
                else
                    ToastUtils.show(getApplicationContext(), "发货数量不能全部为0 ！");
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            getUserDefaultAddress();
        }
        if (resultCode == Constants.SELECT_ADDRESS_RESULT_CODE && requestCode == Constants.SELECT_ADDRESS_REQUEST_CODE) {
            entityAddress = (EntityAddress) data.getSerializableExtra("AddressEntity");
            setAddress(entityAddress);
            Log.i("TAG", "////////////");
        }
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
                Log.i("TAG", "res=" + result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        enityOne = JSON.parseObject(json.getString("data"), EnityOne.class);
                        entityAddress = enityOne.address;
                        setAddress(entityAddress);

                    } else {
                        CommonUtils.loginOverrTime(json.getString("msg"), SplitOrder.this, true);
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
     * 拆分订单，生成订单
     *********/
    private void splitOrder() {
        canDo = false;
        dialog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "Order/immediateOrder");
        params.addBodyParameter("order_no", orderEntity.order_no);
        params.addBodyParameter("address_id", entityAddress.aid);
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        for (int i = 0; i < Tempdata.size(); i++) {
            //封装json 对象
            jsonEnityList.put(Tempdata.get(i).goods_id, Tempdata.get(i).orginNumber);
        }
        JSONArray jsonArray = new JSONArray();

        jsonArray.addAll(Arrays.asList(jsonEnityList));
        params.addParameter("data", jsonArray.toJSONString());


        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TAG", "result=" + result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        ToastUtils.show(getApplicationContext(), "拆单成功！");
                        //startActivity(new Intent(SplitOrder.this, AllOrder.class));
                        finish();
                    } else {
                        CommonUtils.loginOverrTime(json.getString("msg"), SplitOrder.this, true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("TAG", "ex=" + ex.getLocalizedMessage());
                ToastUtils.show(getApplicationContext(), "拆单失败！");


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
        name.setText(entityAddress.uname);
        phone.setText(entityAddress.phone);
        detail_address.setText(entityAddress.addr_prov);
    }


    public class SplitAdapter extends BaseAdapter {
        private List<OrderEntityInner> datas;
        private ViewHolder vh;
        ;

        public SplitAdapter(List<OrderEntityInner> data) {
            this.datas = data;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = View.inflate(getApplicationContext(), R.layout.item_split, null);
                vh.pro_photo = (ImageView) convertView.findViewById(R.id.iv_indexPhoto);
                vh.pro_name = (TextView) convertView.findViewById(R.id.tv_indexName);
                vh.pro_price = (TextView) convertView.findViewById(R.id.tv_indexPrice);
                vh.totalNuber = (TextView) convertView.findViewById(R.id.tv_total);
                vh.pro_number = (TextView) convertView.findViewById(R.id.et_middle);
                vh.add_number = (Button) convertView.findViewById(R.id.ib_add);
                vh.desc_number = (Button) convertView.findViewById(R.id.ib_desc);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            Glide.with(SplitOrder.this).load(datas.get(position).goods_img).placeholder(R.drawable.erropic).error(R.drawable.erropic).into(vh.pro_photo);
            vh.pro_name.setText(datas.get(position).goods_name);
            vh.totalNuber.setText("总数量：" + datas.get(position).buy_count);
            /*if (orgin == 0) {
                vh.pro_number.setText("0");
            } else*/
                vh.pro_number.setText(datas.get(position).orginNumber);
            //点击增加数量
            vh.add_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    orgin++;
                    //计算初始数字和总数字
                    buyCount = Integer.parseInt(datas.get(position).orginNumber);
                    temp = Integer.parseInt(datas.get(position).buy_count);
                    if (buyCount >= temp) {
                        ToastUtils.show(getApplicationContext(), "不能超过总数量！");
                    } else {
                        buyCount++;
                        datas.get(position).orginNumber = String.valueOf(buyCount);
                        adapter.notifyDataSetChanged();

                    }
                }
            });
            //点击减少数量，但是最少为1
            vh.desc_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    orgin++;
                    buyCount = Integer.parseInt(datas.get(position).orginNumber);
                    if (buyCount <= 0) {
                        ToastUtils.show(getApplicationContext(), "数量不能再少了！");
                    } else {
                        buyCount--;
//                        vh.pro_number.setText(String.valueOf(buyCount));
                        datas.get(position).orginNumber = String.valueOf(buyCount);
                        adapter.notifyDataSetChanged();
                    }

                }
            });

//            输入要发货的数量
            vh.pro_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    orgin++;
                    DialogUtils.SplitOrderShow(position, datas, adapter, SplitOrder.this);

                }
            });


            return convertView;
        }


        private class ViewHolder {
            ImageView pro_photo;
            TextView pro_name;
            TextView pro_price;
            TextView totalNuber;
            TextView pro_number;
            Button add_number;
            Button desc_number;
        }
    }
}
