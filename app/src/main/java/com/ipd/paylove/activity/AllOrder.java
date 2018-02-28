package com.ipd.paylove.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;

import com.alibaba.fastjson.JSON;
import com.ipd.paylove.R;
import com.ipd.paylove.adapter.OrderAdapter;
import com.ipd.paylove.base.BaseActivity;
import com.ipd.paylove.entity.OrderEntity;
import com.ipd.paylove.gloable.Constant;
import com.ipd.paylove.utils.CommonUtils;
import com.ipd.paylove.utils.ToastUtils;
import com.ipd.paylove.view.PullToRefreshLayout;
import com.ipd.paylove.view.PullableListView;
import com.ipd.paylove.view.ScrollBangbangGouMenu;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*********
 * 全部订单
 *********/
public class AllOrder extends BaseActivity {
    private int index=0;
    private String status = "0";//订单状态（0全部订单，1待审核订单，2待发货订单，3已发货订单，4撤单订单）
    private int page = 1;
    private OrderAdapter orderAdapter;
    private List<OrderEntity> data = new ArrayList<>();
    private List<OrderEntity> Tempdata = new ArrayList<>();

    //刷新和加载
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout ptrl;
    @ViewInject(R.id.rfList)
    private PullableListView listView;

    //顶部选项卡分类
    private String[] names = {"全部", "待审核", "待发货", "已发货", "撤单","延迟发货"};
    @ViewInject(R.id.horizontalScrollView)
    private HorizontalScrollView horizontalScrollView;
    @ViewInject(R.id.sbg)
    private ScrollBangbangGouMenu scrollBangbangGouMenu;//设置关联的Viewpager指示器

    @Override
    public int setLayout() {
        return R.layout.activity_all_order;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setBack();
        setTopTitle("全部订单");
        orderAdapter = new OrderAdapter(AllOrder.this, data);
        listView.setAdapter(orderAdapter);
        getOrder("");

        scrollBangbangGouMenu.setMenu(Arrays.asList(names), new ScrollBangbangGouMenu.onMenuClickListener() {
            @Override
            public void onClick(int pos) {
                page = 1;
                status = String.valueOf(pos);
                getOrder("");
            }
        });
    }

    @Override
    public void setListener() {


        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout PullToRefreshLayout2) {

                // 下拉刷新操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件刷新完毕了哦！
                        page = 1;
                        getOrder("refresh");
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
                        getOrder("loadMore");
                        ptrl.loadmoreFinish(PullToRefreshLayout2.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 3000);

            }
        });
        horizontalScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                horizontalScrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                View curView = scrollBangbangGouMenu.getChildAt(index);
                float menuX = curView.getX() - (curView.getMeasuredWidth() * 2);
                horizontalScrollView.smoothScrollTo((int) menuX, 0);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }


    /*********
     * 查询订单
     *********/
    private void getOrder(String canRoate) {

        if (page == 1) {
            data.clear();
        }
        Tempdata.clear();
        if (TextUtils.isEmpty(canRoate.trim())) {
            dialog();
        }
        RequestParams params = new RequestParams(Constant.BASE_URL + "Order/orderList");
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        params.addBodyParameter("status", status);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("page_size", Constant.SIZE);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TAG", "res=" + result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        Tempdata = JSON.parseArray(json.getString("data"), OrderEntity.class);
                        data.addAll(Tempdata);

                    } else {

                            CommonUtils.loginOverrTime(json.getString("msg"),AllOrder.this,true);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                orderAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.show(getApplicationContext(), "查询订单失败！");
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.i("TAG", "status=" + status);

                dismiss();
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("UPDATEADAPTER");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("UPDATEADAPTER")) {
                int pos = intent.getIntExtra("pos", 1);
                data.remove(pos);
                orderAdapter.notifyDataSetChanged();
                ToastUtils.show(getApplicationContext(), "订单取消成功！");
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getOrder("");
    }
}
