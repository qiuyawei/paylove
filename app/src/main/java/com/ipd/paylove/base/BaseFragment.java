package com.ipd.paylove.base;


import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ipd.paylove.R;
import com.ipd.paylove.utils.CommonUtils;
import com.ipd.paylove.utils.ToastUtils;
import com.ipd.paylove.widget.HKDialogLoading;

import org.xutils.x;

/**
 * Fragment基类
 * 
 * @author yang
 * 
 */
public abstract class BaseFragment extends Fragment implements OnClickListener {
	/**
	 * 返回的view对象
	 */
	protected View view;
	/**
	 * 获取上下文
	 */
	public Context context;
	public Intent intent;
	private HKDialogLoading dialogLoading;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		dialogLoading = new HKDialogLoading(context,R.style.HKDialog);
		if(!CommonUtils.isNetworkAvailable(context)){
			ToastUtils.show(context,"请检查网络连接！");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = initView(inflater);
		// 注入控件
		x.view().inject(this, view);
		setListener();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData(savedInstanceState);
	}

	/**
	 * 初始化View 对象
	 * 
	 * @param inflater
	 *            view填充器 需要布局文件
	 * @return 返回view 对象
	 */
	public abstract View initView(LayoutInflater inflater);

	/**
	 * 初始化数据
	 * 
	 * @param savedInstanceState
	 */
	public abstract void initData(Bundle savedInstanceState);

	/**
	 * 对view设置监听事件
	 */
	public abstract void setListener();

	private FragmentTitleListener titleListener;

	public FragmentTitleListener getTitleListener() {
		return titleListener;
	}

	public void setTitleListener(FragmentTitleListener titleListener) {
		this.titleListener = titleListener;
	}

	public interface FragmentTitleListener {
		public void setTitle();
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
