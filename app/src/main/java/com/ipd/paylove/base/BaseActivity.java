package com.ipd.paylove.base;

import com.ipd.paylove.R;
import com.ipd.paylove.gloable.MyApplication;
import com.ipd.paylove.utils.CommonUtils;
import com.ipd.paylove.utils.ToastUtils;
import com.ipd.paylove.widget.HKDialogLoading;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.x;

/**
 * 界面基类
 *
 * @author
 *
 */
public abstract class BaseActivity extends FragmentActivity  implements View.OnClickListener{
	private RelativeLayout back;
	private TextView title;
	public Intent intent;
	private HKDialogLoading dialogLoading;
	//public ShapeLoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 手机窗口设置无标
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置一直是竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// 设置填充的layout
		setContentView(setLayout());
		dialogLoading = new HKDialogLoading(this,R.style.HKDialog);
		// 注入控件
		x.view().inject(this);
		init(savedInstanceState);
		setListener();

		MyApplication.addActivity(BaseActivity.this);

		if(!CommonUtils.isNetworkAvailable(this)){
			ToastUtils.show(this,"请检查网络连接！");
		}
	}

	/**
	 * 设置界面加载的Layout
	 *
	 * @return
	 */
	public abstract int setLayout();

	/**
	 * 初始化数据
	 */
	public abstract void init(Bundle savedInstanceState);

	/**
	 * 对view设置监听事件
	 */
	public abstract void setListener();

	/**
	 * 设置头部标题
	 *
	 * @param str
	 */
	public void setTopTitle(String str) {
		title = (TextView) findViewById(R.id.tv_title);
		if (str != null) {
			title.setText(str);
		}

	}

	/**
	 * 返回
	 */
	public void setBack() {
		back = (RelativeLayout) findViewById(R.id.rl_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}

	/**
	 * 设置右边button的名字和监听�?
	 *
	 * @param text
	 *            名字
	 * @param listener
	 *            监听
	 */
	public void setRightButton(String text, OnClickListener listener) {

		TextView btn = (TextView) findViewById(R.id.tv_right);
		btn.setVisibility(View.VISIBLE);
		if (!TextUtils.isEmpty(text)) {
			btn.setText(text);
		}
		if (listener != null) {
			btn.setOnClickListener(listener);
		}
	}

	/**
	 *
	 * @param
	 *
	 * @param listener
	 */
	public void setRightIv(int resource, OnClickListener listener) {
		RelativeLayout rl_right = (RelativeLayout) findViewById(R.id.rl_right);
		ImageView iv = (ImageView) findViewById(R.id.ivRight);
		rl_right.setVisibility(View.VISIBLE);
		iv.setBackgroundResource(resource);
		if (listener != null) {
			rl_right.setOnClickListener(listener);
		}
	}

	/**
	 * 获取上下文
	 *
	 * @return
	 */
	public Context getContext() {
		return this;
	}

	public void dismiss() {
		if (dialogLoading.isShowing()) {
			dialogLoading.dismiss();
		}
	}

	public void dialog() {
		dialogLoading.show();// 显示加载中对话框
	}

}
