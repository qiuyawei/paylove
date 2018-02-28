package com.ipd.paylove.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ipd.paylove.R;
import com.ipd.paylove.entity.EntityName;

import java.util.List;

/**
 * Created by qiuyawei on 2016/12/5.
 */
public class AdapterName extends BaseAdapter{
    ViewHolder vh=null;
    private Context context;
    private List<EntityName> data;
    public AdapterName(Context context,List<EntityName> data){
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

        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_name,null);
            vh=new ViewHolder();
            vh.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            vh.checkBox= (ImageView) convertView.findViewById(R.id.cb);
            vh.rl= (RelativeLayout) convertView.findViewById(R.id.rl);
            convertView.setTag(vh);
        }else {
            vh= (ViewHolder) convertView.getTag();
        }
        vh.tv_name.setText(data.get(position).uname);
        if(data.get(position).isChecked){
            vh.checkBox.setImageResource(R.drawable.tjcy_08);
        }else  vh.checkBox.setImageResource(R.drawable.tjcy_12);


        return convertView;
    }


    class ViewHolder{
        TextView tv_name;
        ImageView checkBox;
        RelativeLayout rl;
    }
}
