package com.ipd.paylove.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ipd.paylove.R;
import com.ipd.paylove.base.BaseActivity;
import com.ipd.paylove.utils.ToastUtils;

import org.xutils.view.annotation.ViewInject;


/**
 * @author Administrator
 * 完善个人信息
 *
 */
public class CompletPersonInfor extends BaseActivity {
	@ViewInject(R.id.rl_skBank)
	private RelativeLayout rl_skBank;
	@ViewInject(R.id.tv_skBank)
	private TextView tv_skBank;
	@ViewInject(R.id.bt_submit)
	private Button bt_submit;

	@Override
	public int setLayout() {
		return R.layout.activity_reg_three;
	}

	@Override
	public void init(Bundle savedInstanceState) {
		setTopTitle("完善信息");
		setBack();

	}

	@Override
	public void setListener() {
		bt_submit.setOnClickListener(this);
		rl_skBank.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.rl_skBank:
				AlertDialog.Builder builder=new AlertDialog.Builder(CompletPersonInfor.this);
				builder.setTitle(" 请选择收款银行!");
				final String[] banks={"中国建设银行","中国工商银行","中国农业银行","中国邮政"};
				builder.setItems(banks, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_skBank.setText(banks[which]);
					}
				});
				builder.show();
				break;

			case R.id.bt_submit:
				intent=new Intent(getApplicationContext(),RegEnd.class);
				startActivity(intent);
				break;
		}
	}
}
