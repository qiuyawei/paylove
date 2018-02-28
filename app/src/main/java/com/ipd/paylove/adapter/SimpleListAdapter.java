package com.ipd.paylove.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ipd.paylove.R;

import java.util.List;

/**
 * Created by lenovo on 2017/6/14.
 */

public class SimpleListAdapter extends BaseAdapter {
    private Context context;
    private List<String> data;
    public SimpleListAdapter(Context context,List<String> data){
        this.context=context;
        this.data=data;
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
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=View.inflate(context, R.layout.item_simple_order,null);
            viewHolder.orderName= (TextView) convertView.findViewById(R.id.tv_orderName);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.orderName.setText(data.get(position));
        return convertView;
    }

    public class ViewHolder{
        TextView orderName;
    }
}
