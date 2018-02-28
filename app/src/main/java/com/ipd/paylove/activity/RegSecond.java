package com.ipd.paylove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ipd.paylove.R;
import com.ipd.paylove.base.BaseActivity;

import org.xutils.view.annotation.ViewInject;


/**
 * @author Administrator
 * 注册页面第二步
 *
 */
public class RegSecond extends BaseActivity {
	@ViewInject(R.id.bt_next)
	private Button bt_next;//下一步
	@ViewInject(R.id.bt_now)
	private Button bt_now;//已有账号，现在登录
	@Override
	public int setLayout() {
		return R.layout.activity_reg_two;
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
				intent=new Intent(RegSecond.this,CompletPersonInfor.class);
				startActivity(intent);
				break;
			case R.id.bt_now:
				//下一步，判断邀请码是否正确，发送网络请求，正确执行下一步，否则提示邀请码错误
				//getInvitecode();
				intent=new Intent(RegSecond.this,Login.class);
				startActivity(intent);
				finish();
				break;
		}
	}
}
