package com.ipd.paylove.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ipd.paylove.R;
import com.ipd.paylove.activity.AllOrder;
import com.ipd.paylove.activity.DetailInfor;
import com.ipd.paylove.activity.PersonInfor;
import com.ipd.paylove.activity.RecommendedAgent;
import com.ipd.paylove.activity.Set;
import com.ipd.paylove.base.BaseFragment;
import com.ipd.paylove.utils.CommonUtils;
import com.ipd.paylove.utils.SharedPreferencesUtils;

import org.xutils.view.annotation.ViewInject;

/*******
 * 个人中心页面 Fragment
 ******/

public class PersonCenterFragment extends BaseFragment {
    @ViewInject(R.id.rl_jcxx)
    private RelativeLayout rl_jcxx;
    @ViewInject(R.id.rl_xxxx)
    private RelativeLayout rl_xxxx;
    @ViewInject(R.id.rl_tjdls)
    private RelativeLayout rl_tjdls;
    @ViewInject(R.id.rl_qbdd)
    private RelativeLayout rl_qbdd;
    @ViewInject(R.id.rl_set)
    private RelativeLayout rl_set;
    @ViewInject(R.id.tv_nickName)
    private TextView tv_nickName;

    @ViewInject(R.id.ll_all)
    private LinearLayout ll_all;

    @Override
    public View initView(LayoutInflater inflater) {
        view = View.inflate(context, R.layout.fragment_person_center, null);
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
            tv_nickName.setText(SharedPreferencesUtils.getSharedPreferences(context,"real_name"));
    }

    @Override
    public void setListener() {
        rl_jcxx.setOnClickListener(this);
        rl_xxxx.setOnClickListener(this);
        rl_tjdls.setOnClickListener(this);
        rl_qbdd.setOnClickListener(this);
        rl_set.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(CommonUtils.goLogin(context)){
            return;
        }
        switch (v.getId()) {
            case R.id.rl_jcxx:
                intent = new Intent(context, PersonInfor.class);
                context.startActivity(intent);

                break;
            case R.id.rl_xxxx:
                intent = new Intent(context, DetailInfor.class);
                context.startActivity(intent);

                break;
            case R.id.rl_tjdls:
                intent = new Intent(context, RecommendedAgent.class);
                context.startActivity(intent);
                break;
            case R.id.rl_qbdd:
                intent = new Intent(context, AllOrder.class);
                context.startActivity(intent);
                break;
            case R.id.rl_set:
                intent = new Intent(context, Set.class);
                context.startActivity(intent);
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        tv_nickName.setText(SharedPreferencesUtils.getSharedPreferences(context,"real_name"));

    }
}
