package com.ipd.paylove.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
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
public class HKDialogLoading2 extends Dialog {
	Context context;

	public HKDialogLoading2(Context context, int theme) {
		super(context, theme);
		this.context = context;
		ini();
	}

	public HKDialogLoading2(Context context) {
		super(context);
		this.context = context;
	//	ini();
	}

	void ini() {
		/**
		 * "加载项"布局，此布局被添加到ListView的Footer中。
		 */
		LinearLayout contentView = new LinearLayout(context);
		contentView.setMinimumHeight(120);
		contentView.setOrientation(LinearLayout.VERTICAL);
		contentView.setMinimumWidth(200);;
		contentView.setGravity(Gravity.TOP);
		/**
		 * 向"加载项"布局中添加一个圆型进度条。
		 */
		ImageView image = new ImageView(context);
		//image.setImageResource(R.mipmap.loadcircle);
		TextView title=new TextView(context);
		title.setPadding(10,6,0,0);
		title.setText("");
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.rotate_repeat);

		LinearInterpolator lir = new LinearInterpolator();
		anim.setInterpolator(lir);
		image.setAnimation(anim);

		contentView.addView(image);
		contentView.addView(title);
		setContentView(contentView);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//this.dismiss();
		}

		return true;
	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle("加载中...");
	}
}