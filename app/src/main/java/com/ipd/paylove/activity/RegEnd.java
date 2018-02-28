package com.ipd.paylove.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ipd.paylove.R;
import com.ipd.paylove.base.BaseActivity;

import org.xutils.view.annotation.ViewInject;


/**
 * @author Administrator
 * 注册完成
 *
 */
public class RegEnd extends BaseActivity {
	@ViewInject(R.id.bt_regSucess)
	private Button bt_regSucess;

	@Override
	public int setLayout() {
		return R.layout.activity_end;
	}

	@Override
	public void init(Bundle savedInstanceState) {

	}

	@Override
	public void setListener() {
		bt_regSucess.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.bt_regSucess:
				intent=new Intent(RegEnd.this,Login.class);
				startActivity(intent);
				finish();
				break;
		}
	}
}
