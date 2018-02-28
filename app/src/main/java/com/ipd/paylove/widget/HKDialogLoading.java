package com.ipd.paylove.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipd.paylove.R;


/**
 * 加载对话框
 */
public class HKDialogLoading extends Dialog {
	private TextView tv_text;

	public HKDialogLoading(Context context,int them) {
		super(context,them);
		/**设置对话框背景透明*/
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		setContentView(R.layout.progress_dialog);
		tv_text = (TextView) findViewById(R.id.tv_text);
		setCanceledOnTouchOutside(false);
	}

	/**
	 * 为加载进度个对话框设置不同的提示消息
	 *
	 * @param message 给用户展示的提示信息
	 * @return build模式设计，可以链式调用
	 */
	public HKDialogLoading setMessage(String message) {
		tv_text.setText(message);
		return this;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// return true;
			dismiss();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}