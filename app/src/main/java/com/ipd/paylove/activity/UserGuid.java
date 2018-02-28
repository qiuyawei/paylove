package com.ipd.paylove.activity;

import android.os.Bundle;
import android.view.View;

import com.ipd.paylove.R;
import com.ipd.paylove.base.BaseActivity;

/**
 * Created by lenovo on 2017/2/12.
 */
public class UserGuid extends BaseActivity {
    @Override
    public int setLayout() {
        return R.layout.user_guid;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setBack();
        setTopTitle("使用说明");
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
