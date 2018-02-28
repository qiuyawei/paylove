package com.ipd.paylove.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ipd.paylove.R;
import com.ipd.paylove.base.BaseActivity;

import org.xutils.view.annotation.ViewInject;


/**
 * @author Administrator
 * 注册页面第一步
 *
 */
public class RegFirst extends BaseActivity  {
	@ViewInject(R.id.et_yqm)
	private EditText et_yqm;//输入邀请码
	@ViewInject(R.id.bt_next)
	private Button bt_next;//下一步
	@ViewInject(R.id.bt_now)
	private Button bt_now;//已有账号，现在登录
	@Override
	public int setLayout() {
		return R.layout.activity_reg_first;
	}

	@Override
	public void init(Bundle savedInstanceState) {
		setTopTitle("注册");
		setBack();

	}

	@Override
	public void setListener() {
		bt_next.setOnClickListener(this);
		bt_now.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.bt_next:
				//下一步，判断邀请码是否正确，发送网络请求，正确执行下一步，否则提示邀请码错误
				//getInvitecode();
				intent=new Intent(RegFirst.this,RegSecond.class);
				startActivity(intent);
				break;
			case R.id.bt_now:
				//下一步，判断邀请码是否正确，发送网络请求，正确执行下一步，否则提示邀请码错误
				//getInvitecode();
				intent=new Intent(RegFirst.this,Login.class);
				startActivity(intent);
				finish();
				break;
		}
	}

	private void getInvitecode(){

	}
}
