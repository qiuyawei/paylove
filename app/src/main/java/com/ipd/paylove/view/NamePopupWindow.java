package com.ipd.paylove.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ipd.paylove.R;
import com.ipd.paylove.activity.AdapterName;
import com.ipd.paylove.entity.EntityName;

import java.util.List;


/**
 * All rights Reserved, Designed By GeofferySun 
 * @Description:从底部弹出或滑出选择菜单或窗口
 */
public class NamePopupWindow extends PopupWindow {
	public static String NAME,NameId;
	private Button okBtn;
	private MyListView myListView;
	private View mMenuView;
	private List<EntityName> entityNames;
	private	AdapterName adapterName;
	private TextView textView;
	@SuppressLint("InflateParams")
	public NamePopupWindow(Context context, final List<EntityName> names, final TextView textView) {
		super(context);
		this.entityNames=names;
		this.textView=textView;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.dialog_select_person, null);
		okBtn = (Button) mMenuView.findViewById(R.id.okBtn);
		myListView= (MyListView) mMenuView.findViewById(R.id.myList);
		 adapterName=new AdapterName(context,names);
		myListView.setAdapter(adapterName);

		myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.i("TAG","pos="+position);
				for(int i=0;i<names.size();i++){
					names.get(i).isChecked=false;

					if(i==position){
						names.get(i).isChecked=true;
						NAME=names.get(i).uname;
						NameId=names.get(i).dk_id;
					}
				}
				adapterName.notifyDataSetChanged();
			}
		});

		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				textView.setText(NAME);
			}
		});


		// 设置按钮监听
		//cancelBtn.setOnClickListener(itemsOnClick);
		//pickPhotoBtn.setOnClickListener(itemsOnClick);
		//takePhotoBtn.setOnClickListener(itemsOnClick);
		
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.PopupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x80000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			@Override
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});



	}

}
