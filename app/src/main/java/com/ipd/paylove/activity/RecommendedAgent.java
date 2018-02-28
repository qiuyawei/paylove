package com.ipd.paylove.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ipd.paylove.R;
import com.ipd.paylove.base.BaseActivity;
import com.ipd.paylove.utils.ToastUtils;

import org.xutils.view.annotation.ViewInject;

/********推荐代理商页面*********/
public class RecommendedAgent extends BaseActivity {
    @ViewInject(R.id.tv_regCode)
    private TextView tv_regCode;
    @ViewInject(R.id.bt_copy)
    private Button bt_copy;

    @Override
    public int setLayout() {
        return R.layout.activity_recommended_agent;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setBack();
        setTopTitle("推荐代理商");


    }

    @Override
    public void setListener() {
        bt_copy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_copy:
                //复制
                copy(getApplicationContext(),tv_regCode.getText().toString().trim());
                break;
        }
    }

    private void  copy(Context context,String content){
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
        ToastUtils.show(context,"复制成功!");
    }



}
