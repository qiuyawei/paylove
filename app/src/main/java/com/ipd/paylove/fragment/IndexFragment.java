package com.ipd.paylove.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.ipd.paylove.R;
import com.ipd.paylove.activity.SearchProduct;
import com.ipd.paylove.base.BaseFragment;
import com.ipd.paylove.entity.EntiyIndex;
import com.ipd.paylove.gloable.Constant;
import com.ipd.paylove.utils.CommonUtils;
import com.ipd.paylove.utils.DialogUtils;
import com.ipd.paylove.utils.SharedPreferencesUtils;
import com.ipd.paylove.utils.ToastUtils;
import com.ipd.paylove.view.PullToRefreshLayout;
import com.ipd.paylove.view.PullableListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页界面Fragment
 */

public class IndexFragment extends BaseFragment {
    private int page = 1;
    private String keywords = "";//搜索关键字

    @ViewInject(R.id.ll_seach)
    private LinearLayout ll_seach;


    //刷新和加载
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout ptrl;
    @ViewInject(R.id.rfList)
    private PullableListView listView;

    private static AdapterIndex adapterIndex;
    private static List<EntiyIndex> data = new ArrayList<>();
    private List<EntiyIndex> Tempdata = new ArrayList<>();

    @Override
    public View initView(LayoutInflater inflater) {
        view = View.inflate(context, R.layout.fragment_index, null);
        return view;
    }



    @Override
    public void initData(Bundle savedInstanceState) {
 /*       if(!CommonUtils.goLogin(context)){
//            登录了才有弹出框
            DialogUtils.showIndex(context,Constant.BASE_URL+"index");
        }*/

        /****准备数据，加数据****/

        adapterIndex = new AdapterIndex(data);
        listView.setAdapter(adapterIndex);
//        getData("");
    }

    @Override
    public void setListener() {
       ll_seach.setOnClickListener(this);


        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout PullToRefreshLayout2) {

                // 下拉刷新操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件刷新完毕了哦！
                        page = 1;
                        getData("refesh");
                        ptrl.refreshFinish(PullToRefreshLayout2.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 3000);

            }

            @Override
            public void onLoadMore(final PullToRefreshLayout PullToRefreshLayout2) {
                // 加载操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件加载完毕了哦！
                        page++;
                        getData("loadMore");
                        ptrl.loadmoreFinish(PullToRefreshLayout2.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 3000);

            }
        });





    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_seach:
                startActivity(new Intent(context, SearchProduct.class));
                break;
        }
    }


    /* keywords	搜索关键字（获取所有商品时传空或不传）
     page	页码（默认为1）
     page_size	每页显示数量（默认为10）*/
    private void getData(String canRoate) {
        Tempdata.clear();
        if (page == 1) {
            data.clear();
        }
        if (TextUtils.isEmpty(canRoate))
            dialog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "Goods/index");
        params.addBodyParameter("keywords", keywords);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("page_size", Constant.SIZE);
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TAG", "res=" + result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        Tempdata.addAll(JSON.parseArray(json.getString("data"), EntiyIndex.class));
                        data.addAll(Tempdata);
                    } else {
                        CommonUtils.loginOverrTime(json.getString("msg"),context,false);
                        ToastUtils.show(getActivity(),json.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapterIndex.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                adapterIndex.notifyDataSetChanged();
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


    public class AdapterIndex extends BaseAdapter {
        private List<EntiyIndex> data;
        private AdapterIndex adapter;
        ViewHolder vh = null;
        public AdapterIndex(List<EntiyIndex> mdata) {
            this.data = mdata;
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

          //  if (convertView == null) {
                vh = new ViewHolder();
                convertView = View.inflate(context, R.layout.item_index, null);
                vh.pro_photo = (ImageView) convertView.findViewById(R.id.iv_indexPhoto);
                vh.pro_name = (TextView) convertView.findViewById(R.id.tv_indexName);
                vh.pro_price = (TextView) convertView.findViewById(R.id.tv_indexPrice);
                vh.pro_number = (TextView) convertView.findViewById(R.id.et_indexNumber);
                vh.add_car = (Button) convertView.findViewById(R.id.bt_indexAddcar);
                convertView.setTag(vh);
          /*  } else {
                vh = (ViewHolder) convertView.getTag();
            }*/
            Glide.with(context).load(data.get(position).goods_img).placeholder(R.drawable.erropic).error(R.drawable.erropic).centerCrop().into(vh.pro_photo);
            vh.pro_name.setText(data.get(position).goods_name);
            vh.pro_price.setText("¥" + data.get(position).price);
            vh.pro_number.setText(data.get(position).number + "");


            vh.add_car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(CommonUtils.goLogin(context)){
                        return;
                    }
                    if (data.get(position).number.equals("0") || TextUtils.isEmpty(data.get(position).number)) {
                        ToastUtils.show(context, "数量最小为1哦！");
                        return;
                    } else
                        addShopCar(data.get(position).goods_id, data.get(position).number);
                }
            });

            vh.pro_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(CommonUtils.goLogin(context)){
                        return;
                    }
                    DialogUtils.ShowIndex(context,vh.pro_number,vh.pro_number.getText().toString().trim(),adapterIndex,data,position);
                }
            });

//未登录不显示价格user_token
            Log.i("TAG","==="+SharedPreferencesUtils.getSharedPreferences(getActivity(),"user_token"));
            if(TextUtils.isEmpty(SharedPreferencesUtils.getSharedPreferences(getActivity(),"user_token"))){
                vh.pro_price.setVisibility(View.GONE);
            }else vh.pro_price.setVisibility(View.VISIBLE);

            return convertView;
        }


        private class ViewHolder {
            ImageView pro_photo;
            TextView pro_name;
            TextView pro_price;
            TextView pro_number;
            Button add_car;
        }


    }

    private void addShopCar(String goods_id, String num) {
        dialog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "Cart/add");
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        params.addBodyParameter("goods_id", goods_id);
        params.addBodyParameter("num", num);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("200")) {
                        ToastUtils.show(context, json.getString("msg"));
                    } else {
                        CommonUtils.loginOverrTime(json.getString("msg"),context,false);
                        ToastUtils.show(getActivity(),json.getString("msg"));
                    }
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

 /*   @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("REFRESH");
        intentFilter.addAction("Cancel_price");
        context.registerReceiver(myReciver,intentFilter);
    }
    BroadcastReceiver myReciver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("REFRESH")||intent.getAction().equals("Cancel_price")){
                page=1;
                getData("");
            }
        }
    };*/


    @Override
    public void onResume() {
        super.onResume();
        page=1;
        getData("");
    }
}
