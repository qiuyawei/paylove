package com.ipd.paylove.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ipd.paylove.R;
import com.ipd.paylove.entity.ShopCarEntity;

import java.util.List;

/**
 * Created by qiuyawei on 2016/12/5.
 */
public class ShopCarAdapter extends BaseAdapter {
    private List<ShopCarEntity> data;
    private ViewHolder vh;
    private Context context;

    public ShopCarAdapter(Context context,List<ShopCarEntity> data) {
        this.context=context;
        this.data = data;
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
            convertView = View.inflate(context, R.layout.item_car, null);
            vh.pro_photo = (ImageView) convertView.findViewById(R.id.iv_indexPhoto);
            vh.pro_name = (TextView) convertView.findViewById(R.id.tv_indexName);
            vh.pro_price = (TextView) convertView.findViewById(R.id.tv_indexPrice);
            vh.pro_number = (TextView) convertView.findViewById(R.id.tv_count);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(data.get(position).goods_img).placeholder(R.drawable.erropic).error(R.drawable.erropic).into(vh.pro_photo);
        vh.pro_name.setText(data.get(position).goods_name);
        vh.pro_price.setText("¥" + data.get(position).price + "/箱");
        vh.pro_number.setText("数量："+data.get(position).buy_count);

        return convertView;
    }


    private class ViewHolder {
        ImageView pro_photo;
        TextView pro_name;
        TextView pro_price;
        TextView pro_number;
    }
}
