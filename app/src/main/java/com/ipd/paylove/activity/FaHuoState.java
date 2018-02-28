package com.ipd.paylove.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ipd.paylove.R;
import com.ipd.paylove.base.BaseActivity;
import com.ipd.paylove.entity.OrderDetailInner;

import org.xutils.view.annotation.ViewInject;

/*******查看发货情况********/
public class FaHuoState extends BaseActivity {
    @ViewInject(R.id.orderNumber)
    private TextView orderNumber;
    @ViewInject(R.id.tv_remark)
    private TextView tv_remark;
    private OrderDetailInner orderDetailInner;
    @Override
    public int setLayout() {
        return R.layout.activity_fahuo_state;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setBack();
        setTopTitle("发货情况");
        orderDetailInner= (OrderDetailInner) getIntent().getSerializableExtra("entity");
        if(orderDetailInner!=null){
            orderNumber.setText(orderDetailInner.send_odd);
            tv_remark.setText(orderDetailInner.send_mark);
        }
    }

    @Override
    public void setListener() {

    }



    @Override
    public void onClick(View v) {

    }


}
