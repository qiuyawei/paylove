package com.ipd.paylove.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ipd.paylove.R;
import com.ipd.paylove.entity.OrderDetailInner;
import com.ipd.paylove.utils.DialogUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 *  订单列表适配器
 *
 */
public class AdapterDetail extends BaseAdapter{
    private List<OrderDetailInner> data;
    private Context context;
    private ViewHolder vh;

    public AdapterDetail(Context mcontext, List<OrderDetailInner> mdata) {
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
            convertView = View.inflate(context, R.layout.item_order_detail, null);
            vh.photo= (ImageView) convertView.findViewById(R.id.iv_orderPhoto);
            vh.name= (TextView) convertView.findViewById(R.id.tv_productName);
            vh.number= (TextView) convertView.findViewById(R.id.tv_productNumber);
            vh.tv_price= (TextView) convertView.findViewById(R.id.tv_price);
            vh.bt_deatil= (Button) convertView.findViewById(R.id.tv_ckfhqk);
            convertView.setTag(vh);
        }else {
            vh= (ViewHolder) convertView.getTag();
        }

        Glide.with(context).load(data.get(position).goods_img).error(R.drawable.erropic).centerCrop().into(vh.photo);
        vh.name.setText(data.get(position).goods_name);
        vh.number.setText("数量:"+data.get(position).buy_count);
        vh.tv_price.setText("¥"+data.get(position).price);
        vh.bt_deatil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent=new Intent(context, FaHuoState.class);
                intent.putExtra("entity",data.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*/
                DialogUtils.showFaHuo(context, data.get(position).send_odd,data.get(position).send_mark);
            }
        });

        return convertView;
    }





    private class ViewHolder {
        ImageView photo;
        TextView name;
        TextView number;
        TextView tv_price;
        Button bt_deatil;

    }

}
