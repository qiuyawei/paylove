package com.ipd.paylove.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ipd.paylove.R;
import com.ipd.paylove.activity.ProductDetail;
import com.ipd.paylove.activity.SplitOrder;
import com.ipd.paylove.entity.OrderEntity;
import com.ipd.paylove.gloable.Constant;
import com.ipd.paylove.utils.CommonUtils;
import com.ipd.paylove.utils.DialogUtils;
import com.ipd.paylove.view.MyListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 * 订单列表适配器
 */
public class OrderAdapter extends BaseAdapter {
    private Intent intent;
    private List<OrderEntity> data;
    private Context context;
    private ViewHolder vh;

    public OrderAdapter(Context mcontext, List<OrderEntity> mdata) {
        this.context = mcontext;
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
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_order, null);
            vh.orderNumber = (TextView) convertView.findViewById(R.id.tv_orderNumber);
            vh.myListView = (MyListView) convertView.findViewById(R.id.myList);
            vh.detail = (Button) convertView.findViewById(R.id.bt_detail);
            vh.cancell = (Button) convertView.findViewById(R.id.bt_cancell);
            vh.rightNow = (Button) convertView.findViewById(R.id.bt_rightNow);
            vh.state = (TextView) convertView.findViewById(R.id.tv_orderState);
            vh.tv_pretime = (TextView) convertView.findViewById(R.id.tv_preTime);
            vh.receiverPerson=(TextView) convertView.findViewById(R.id.tv_receiverPerson);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.orderNumber.setText("订单号:" + data.get(position).order_no);
        if(data.get(position).goods!=null&&data.get(position).goods.length>0){
            AdapterOrderInner adapterOrderInner = new AdapterOrderInner(context, Arrays.asList(data.get(position).goods));
            vh.myListView.setAdapter(adapterOrderInner);
        }


        vh.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, ProductDetail.class);
                intent.putExtra("entity", data.get(position));
                context.startActivity(intent);
            }
        });
        vh.cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.show(context, "", "确定要撤单吗?", "2", new DialogUtils.onDialogClickListener() {
                    @Override
                    public void onCommit(View view, String s) {
                        cancellOrder(position, data.get(position).order_no);
                    }

                    @Override
                    public void onCancel(View view, String s) {


                    }
                });
            }
        });

        vh.rightNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, SplitOrder.class);
                intent.putExtra("entity", data.get(position));
                context.startActivity(intent);
            }
        });

        vh.state.setText("订单状态:" + data.get(position).status_desc);
        if (data.get(position).order_status.equals("R")) {
            vh.cancell.setVisibility(View.GONE);
        }

//        判断是否显示及时发货按钮
        if(Integer.parseInt(data.get(position).delay_day)>0&&data.get(position).order_status.equals("F")){
            vh.rightNow.setVisibility(View.VISIBLE);
        }else  vh.rightNow.setVisibility(View.GONE);



        //预计发货时间显示与否
        if (data.get(position).order_status.equals("W")||data.get(position).order_status.equals("F")&&Integer.parseInt(data.get(position).delay_day)>0) {
            vh.tv_pretime.setVisibility(View.VISIBLE);
            if(data.get(position).send_time!=null)
            vh.tv_pretime.setText("预计发货时间:"+data.get(position).send_time);
        }else vh.tv_pretime.setText("");

//    只有待审核中有撤单选项按钮

        if (data.get(position).order_status.equals("W")) {
            vh.cancell.setVisibility(View.VISIBLE);
        } else vh.cancell.setVisibility(View.GONE);


        vh.receiverPerson.setText("收货人:"+data.get(position).send_name);
        return convertView;
    }


    private class ViewHolder {
        TextView state;
        TextView orderNumber;
        TextView receiverPerson;
        TextView tv_pretime;
        MyListView myListView;
        Button detail;
        Button cancell;
        Button rightNow;
    }


    /*取消订单*/
    private void cancellOrder(final int pos, String order_no) {
        RequestParams params = new RequestParams(Constant.BASE_URL + "Order/cancelOrder");
        params.addBodyParameter("user_token", Constant.USER_TOKEN);
        params.addBodyParameter("order_no", order_no);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("result").equals("true")) {
                        Intent intent = new Intent();
                        intent.setAction("UPDATEADAPTER");
                        intent.putExtra("pos", pos);
                        context.sendBroadcast(intent);
                    }else {
                        CommonUtils.loginOverrTime(json.getString("msg"),context,true);
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

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });

    }
}
