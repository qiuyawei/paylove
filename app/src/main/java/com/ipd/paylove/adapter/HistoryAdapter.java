package com.ipd.paylove.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ipd.paylove.R;

import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 *  订单列表适配器
 *
 */
public class HistoryAdapter extends BaseAdapter{
    private List<String> data;
    private Context context;
    private ViewHolder vh;

    public HistoryAdapter(Context mcontext, List<String> mdata) {
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
            convertView = View.inflate(context, R.layout.item_history, null);
            vh.name= (TextView) convertView.findViewById(R.id.tv_indexName);

            convertView.setTag(vh);
        }else {
            vh= (ViewHolder) convertView.getTag();
        }



        vh.name.setText(data.get(position));
        return convertView;
    }





    private class ViewHolder {
        TextView name;


    }

}
