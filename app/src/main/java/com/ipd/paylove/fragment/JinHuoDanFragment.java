package com.ipd.paylove.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.ipd.paylove.R;
import com.ipd.paylove.activity.SureOrder;
import com.ipd.paylove.base.BaseFragment;
import com.ipd.paylove.entity.ShopCarEntity;
import com.ipd.paylove.gloable.Constant;
import com.ipd.paylove.swipemenulistview.SwipeMenu;
import com.ipd.paylove.swipemenulistview.SwipeMenuCreator;
import com.ipd.paylove.swipemenulistview.SwipeMenuItem;
import com.ipd.paylove.swipemenulistview.SwipeMenuListView;
import com.ipd.paylove.utils.CommonUtils;
import com.ipd.paylove.utils.DialogUtils;
import com.ipd.paylove.utils.SharedPreferencesUtils;
import com.ipd.paylove.utils.ToastUtils;
import com.ipd.paylove.widget.HKDialogLoading;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 中间界面进货单Fragment
 */

public class JinHuoDanFragment extends BaseFragment {
    private boolean isHidden=false;

    //刷新
    @ViewInject(R.id.swrefsh)
    private SwipeRefreshLayout swipeRefreshLayout;


    private HKDialogLoading dialogLoading;
    //刷新和加载
    @ViewInject(R.id.listView)
    private SwipeMenuListView listView;


    /*****
     * 全选
     *****/
    @ViewInject(R.id.cb_All)
    private CheckBox cb_All;
    /*****
     * 去结算
     *****/
    @ViewInject(R.id.tv_AllMoney)
    private TextView tv_AllMoney;
    /*****
     * 总价
     *****/
    @ViewInject(R.id.bt_jiesuan)
    private Button bt_jiesuan;
    private AdapterMiddle adapterMiddle;
    private List<ShopCarEntity> data = new ArrayList<>();
    private List<ShopCarEntity> Tempdata=new ArrayList<>();
    private int pages = 0;//页码
    private int addNumber = 1, descNumber = 1;//点击增加后的数字和点击减少后的数字
    /*****
     * 是否所有商品被选中，全部则为true，反之有任何一个为false
     ****/
    private boolean isAllSelect = true;
    private double totalMoney = 0;//商品总价格

    //侧滑删除
    SwipeMenuCreator creator;

    @Override
    public View initView(LayoutInflater inflater) {
        view = View.inflate(context, R.layout.fragment_middle, null);
        return view;
    }



    @Override
    public void initData(Bundle savedInstanceState) {
        swipeRefreshLayout.setColorSchemeColors(R.color.main_color);
        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(CommonUtils.dp2px(context, 90));
                // set a icon
                deleteItem.setIcon(R.drawable.delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);


        dialogLoading = new HKDialogLoading(context, R.style.HKDialog);
        adapterMiddle = new AdapterMiddle(data);
        listView.setAdapter(adapterMiddle);
//        getData();
    }

    @Override
    public void setListener() {
        bt_jiesuan.setOnClickListener(this);
        cb_All.setOnClickListener(this);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {

                switch (index) {
                    case 0:
                        if (CommonUtils.goLogin(context)) {
                            return;
                        }
                        deletGoods(data.get(position).car_id, position);
                        break;
                }

            }

        });




        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //全选
            case R.id.cb_All:
                if (cb_All.isChecked()) {
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).isCheck = true;
                    }
                } else {
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).isCheck = false;
                    }
                }
                adapterMiddle.notifyDataSetChanged();
                calculateAllMoney();
                break;
            case R.id.bt_jiesuan:
                if (CommonUtils.goLogin(context)) {
                    return;
                }

                if (Double.parseDouble(tv_AllMoney.getText().toString().replace("¥", "")) == 0) {
                    ToastUtils.show(getActivity(), "请选择商品!");
                    return;
                } else {
                    //去结算界面
                    intent = new Intent(getActivity(), SureOrder.class);
                    //把总计多少钱传过去
                    intent.putExtra("money", tv_AllMoney.getText().toString().trim());
                    intent.putExtra("data", (Serializable) data);
                    startActivity(intent);
                }
                break;
        }
    }


    public class AdapterMiddle extends BaseAdapter {
        private List<ShopCarEntity> mdata;
        private ViewHolder vh;

        public AdapterMiddle(List<ShopCarEntity> data) {
            this.mdata = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = View.inflate(context, R.layout.item_middle, null);
                vh.pro_photo = (ImageView) convertView.findViewById(R.id.iv_indexPhoto);
                vh.pro_name = (TextView) convertView.findViewById(R.id.tv_indexName);
                vh.pro_price = (TextView) convertView.findViewById(R.id.tv_indexPrice);
                vh.pro_number = (TextView) convertView.findViewById(R.id.et_middle);
                vh.add_number = (Button) convertView.findViewById(R.id.ib_add);
                vh.desc_number = (Button) convertView.findViewById(R.id.ib_desc);
                vh.cb = (CheckBox) convertView.findViewById(R.id.cb_middle);
                vh.cb.setTag(position);

                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            Glide.with(context).load(data.get(position).goods_img).placeholder(R.drawable.erropic).error(R.drawable.erropic).into(vh.pro_photo);
            vh.pro_name.setText(data.get(position).goods_name);
            vh.pro_price.setText("¥" + data.get(position).price + "/箱");
            vh.pro_number.setText(data.get(position).buy_count);
            vh.cb.setChecked(data.get(position).isCheck);

            //点击增加数量
            vh.add_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CommonUtils.goLogin(context)) {
                        return;
                    }
                    addNumber = Integer.parseInt(data.get(position).buy_count) + 1;
                    resetShopCarNumber(data.get(position).car_id, addNumber, position);
                    // calculateAllMoney();
                }
            });
            //点击减少数量，但是最少为1
            vh.desc_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CommonUtils.goLogin(context)) {
                        return;
                    }
                    if (data.get(position).buy_count.equals("1")) {
                        ToastUtils.show(getActivity(), "商品数量不能小于1！");
                        return;
                    }
                    descNumber = Integer.parseInt(data.get(position).buy_count) - 1;
                    resetShopCarNumber(data.get(position).car_id, descNumber, position);
                    //calculateAllMoney();

                }
            });
            vh.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.get(position).isCheck) {
                        data.get(position).isCheck = false;
                        //点击之前先把isAllSelect置为true，防止先前改变了它的值为false
                        isAllSelect = true;
                        // data.get(position).isCheck = !data.get(position).isCheck;
                        for (int i = 0; i < data.size(); i++) {
                            if (!data.get(i).isCheck) {
                                isAllSelect = false;
                            }
                        }
                        cb_All.setChecked(isAllSelect);
                        calculateAllMoney();

                    } else {

                        data.get(position).isCheck = true;
                        cb_All.setChecked(false);
                        calculateAllMoney();
                    }

                }
            });


            vh.pro_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CommonUtils.goLogin(context)) {
                        return;
                    }

                    DialogUtils.ShopCarshow(position, data, adapterMiddle, context, data.get(position).buy_count, dialogLoading, vh.pro_number, data.get(position).car_id);
                }
            });


            return convertView;
        }


        private class ViewHolder {
            ImageView pro_photo;
            TextView pro_name;
            TextView pro_price;
            TextView pro_number;
            Button add_number;
            Button desc_number;
            CheckBox cb;
        }
    }

    /*********
     * 计算总价
     *******/
    private void calculateAllMoney() {
        //用后置0
        totalMoney = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isCheck) {
                totalMoney = totalMoney + Double.parseDouble(data.get(i).price) * Integer.parseInt(data.get(i).buy_count);
            }

        }
        tv_AllMoney.setText("¥" + totalMoney);
    }


    //修改购物车商品数量
    private void resetShopCarNumber(String car_id, final int num, final int pos) {
        dialog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "Cart/setCartNum");
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        params.addBodyParameter("car_id", car_id);
        params.addBodyParameter("num", String.valueOf(num));

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TAGS", "result=" + result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        ToastUtils.show(context, json.getString("msg"));
                        data.get(pos).buy_count = num + "";
                        adapterMiddle.notifyDataSetChanged();
                        if (data.get(pos).isCheck) {
                            calculateAllMoney();
                        }
                    } else ToastUtils.show(context, json.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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

    //增加数量
    private void getData() {
        if (TextUtils.isEmpty(SharedPreferencesUtils.getSharedPreferences(getActivity(), "user_token"))) {
            swipeRefreshLayout.setRefreshing(false);
            data.clear();
            adapterMiddle.notifyDataSetChanged();
            return;
        }


        dialog();
        cb_All.setChecked(false);
        data.clear();
        RequestParams params = new RequestParams(Constant.BASE_URL + "Cart/index");
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        data.addAll(JSON.parseArray(json.getString("data").toString(), ShopCarEntity.class));
                    } else {
                        CommonUtils.loginOverrTime(json.getString("msg"),context,false);
                        if(!isHidden)
                        ToastUtils.show(getActivity(),json.getString("msg"));
                        calculateAllMoney();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapterMiddle.notifyDataSetChanged();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                adapterMiddle.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismiss();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });


    }

    //删除商品
    private void deletGoods(String car_id, final int pos) {
        dialog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "Cart/delCart");
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        params.addBodyParameter("car_id", car_id);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        ToastUtils.show(getActivity(), json.getString("msg"));
                        data.remove(pos);
                    } else {
                        CommonUtils.loginOverrTime(json.getString("msg"),context,false);

                        ToastUtils.show(getActivity(),json.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapterMiddle.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                adapterMiddle.notifyDataSetChanged();

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


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden=hidden;
        if (!hidden)
            getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden)
        getData();
    }
}
