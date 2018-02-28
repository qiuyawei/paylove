package com.ipd.paylove.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ipd.paylove.R;
import com.ipd.paylove.entity.EntityAddress;
import com.ipd.paylove.entity.EntiyIndex;

import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 *  收货地址列表适配器
 *
 */
public class AddressAdapter extends BaseAdapter  {
    private List<EntityAddress> data;
    private Context context;
    private ViewHolder vh;

    public AddressAdapter(Context mcontext, List<EntityAddress> mdata) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_address, null);
            vh.name= (TextView) convertView.findViewById(R.id.tv_shrName);
            vh.tv_default= (TextView) convertView.findViewById(R.id.tv_default);

            vh.address= (TextView) convertView.findViewById(R.id.tv_shrAddress);
            vh.phone= (TextView) convertView.findViewById(R.id.tv_shrPhone);
            vh.relativeLayout= (RelativeLayout) convertView.findViewById(R.id.rr_addressAll);
            convertView.setTag(vh);
        }else {
            vh= (ViewHolder) convertView.getTag();
        }
        vh.phone.setText("联系电话："+data.get(position).phone);
        vh.address.setText("收货地址："+data.get(position).addr_prov+data.get(position).addr_name);
        vh.name.setText("收货人："+data.get(position).uname);
        if(data.get(position).default_status.equals("1")){
            vh.tv_default.setVisibility(View.VISIBLE);
        }else {

            vh.tv_default.setVisibility(View.GONE);
        }

        return convertView;
    }




    private class ViewHolder {
        TextView name,tv_default;
        TextView address;
        TextView phone;
        RelativeLayout relativeLayout;
    }

}
