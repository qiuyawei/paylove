package com.ipd.paylove.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.ipd.paylove.R;
import com.ipd.paylove.adapter.MyViewpagerAdatper;
import com.ipd.paylove.widget.ArrayWheelAdapter;
import com.ipd.paylove.widget.NumericWheelAdapter;
import com.ipd.paylove.widget.OnWheelChangedListener;
import com.ipd.paylove.widget.OnWheelScrollListener;
import com.ipd.paylove.widget.WheelView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class PopupUtils implements OnClickListener {
	private static OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int n_year = year.getCurrentItem() + 1950;//
			int n_month = month.getCurrentItem() + 1;//
			day.setAdapter(new NumericWheelAdapter(1, getDay(n_year, n_month), "%02d"));
		}
	};

	/**
	 * 初始化popupWindow
	 *
	 * @param view
	 */
	public static PopupWindow menuWindow;
	private static WheelView year;
	private static WheelView month;
	private static WheelView day;

	private static Dialog dialog;

	private static Dialog myDialog;

	private static Dialog cityDialog;
	private static ViewPager viewPager;
	private static MyViewpagerAdatper adapter;
	private static ArrayList<View> lists;
	private static ImageView image;
	private static View view;

	public static void showPopwindow(Context context, View view) {

		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// dialog.setContentView(view, new
		// LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		dialog.setContentView(view);
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.PopupAnimation);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = LayoutParams.MATCH_PARENT;
		wl.height = LayoutParams.WRAP_CONTENT;
		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	public static View getDataPick(final Context context, final onSelectFinishListener listener) {
		Calendar c = Calendar.getInstance();
		final int curYear = c.get(Calendar.YEAR) - 100;
		int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
		int curDate = c.get(Calendar.DATE);
		final View view = View.inflate(context, R.layout.dialog_datapick, null);

		year = (WheelView) view.findViewById(R.id.year);
		year.setAdapter(new NumericWheelAdapter(curYear, curYear + 100));
		year.setLabel("年");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);

		month = (WheelView) view.findViewById(R.id.month);
		month.setAdapter(new NumericWheelAdapter(1, 12));
		month.setLabel("月");
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		day = (WheelView) view.findViewById(R.id.day);
		day.setAdapter(new NumericWheelAdapter(1, getDay(curYear, curMonth), "%02d"));

		day.setLabel("日");
		day.setCyclic(true);

		year.setCurrentItem(0);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);

		TextView tv_commit = (TextView) view.findViewById(R.id.tv_commit);
		tv_commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (listener != null) {
					listener.onSelectFinish(curYear + year.getCurrentItem() + "", month.getCurrentItem() + 1 + "",
							day.getCurrentItem() + 1 + "");
				}
			}
		});
		return view;
	}

	public static void selectCity(Context context, final OnFinishListener listener) {

		View view = View.inflate(context, R.layout.city_choose_dialog, null);
		final WheelView wv_country = (WheelView) view.findViewById(R.id.wv_country);
		final WheelView wv_city = (WheelView) view.findViewById(R.id.wv_city);
		final WheelView wv_ccity = (WheelView) view.findViewById(R.id.wv_ccity);
		TextView tv_commit = (TextView) view.findViewById(R.id.tv_commit);
		tv_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cityDialog.dismiss();
				int countryIndex = wv_country.getCurrentItem();
				int cityIndex = wv_city.getCurrentItem();
				int ccityIndex = wv_ccity.getCurrentItem();
				String country = AddressData.PROVINCES[countryIndex];
				String city = AddressData.CITIES[countryIndex][cityIndex];
				String ccity = AddressData.COUNTIES[countryIndex][cityIndex][ccityIndex];
				if (listener != null) {
					listener.onFinish(country, city, ccity);
				}
			}
		});
		wv_country.setAdapter(new CountryAdapter());

		final String cities[][] = AddressData.CITIES;
		final String ccities[][][] = AddressData.COUNTIES;

		wv_country.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateCities(wv_city, cities, wv_country.getCurrentItem());
				updatecCities(wv_ccity, ccities, wv_country.getCurrentItem(), wv_city.getCurrentItem());
			}
		});

		wv_city.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updatecCities(wv_ccity, ccities, wv_country.getCurrentItem(), wv_city.getCurrentItem());
			}
		});

		wv_country.setCurrentItem(0);
		wv_city.setCurrentItem(0);
		wv_ccity.setCurrentItem(0);

		updateCities(wv_city, cities, 0);
		updatecCities(wv_ccity, ccities, wv_country.getCurrentItem(), 0);

		cityDialog = new Dialog(context);
		cityDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// dialog.setContentView(view, new
		// LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		cityDialog.setContentView(view);
		Window window = cityDialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.PopupAnimation);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = LayoutParams.MATCH_PARENT;
		wl.height = LayoutParams.WRAP_CONTENT;
		// 设置显示位置
		cityDialog.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		cityDialog.setCanceledOnTouchOutside(true);
		cityDialog.show();
	}



	public interface onSelectFinishListener {
		public void onSelectFinish(String year, String month, String day);
	}

	/**
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
			case 0:
				flag = true;
				break;
			default:
				flag = false;
				break;
		}
		switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				day = 31;
				break;
			case 2:
				day = flag ? 29 : 28;
				break;
			default:
				day = 30;
				break;
		}
		return day;
	}

	public static class DateArrayAdapter extends ArrayWheelAdapter<String> {

		public DateArrayAdapter(String[] items) {
			super(items);
		}

		@Override
		public String getItem(int index) {
			return super.getItem(index);
		}

		@Override
		public int getItemsCount() {
			return super.getItemsCount();
		}

		@Override
		public int getMaximumLength() {
			return super.getMaximumLength();
		}

	}

	/**
	 * 通用的Dialog
	 * @param context
	 * @param view
	 * @param position
	 * @param pictures
	 */
	public  void setDialog(Context context, View view, int position, List<String> pictures) {
		myDialog = new Dialog(context);
		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		myDialog.setContentView(view);
		viewPager= (ViewPager) myDialog.findViewById(R.id.viewPager);

		setMyAdaperData(myDialog,context,pictures,position,viewPager);

	}

	private void setMyAdaperData(Dialog myDialogs, Context context, List<String> pictures, int position, ViewPager viewpagers) {
		lists = new ArrayList<>();
		//BitmapUtils bitmapUtils=new BitmapUtils(context);
		//ImageLoader imageLoader=new ImageLoader();
		//设置图片
		LayoutInflater inflater = LayoutInflater.from(context);
		if(pictures!=null){

			for(int i=0;i<pictures.size();i++){
				view = inflater.inflate(R.layout.image_view_layout, null);
				image = (ImageView) view.findViewById(R.id.imageview);
				ImageLoader.getInstance().displayImage(Constants.BASE_PIC + pictures.get(i),image);
				lists.add(view);
				image.setOnClickListener(this);
			}
			adapter = new MyViewpagerAdatper(context, lists);
			viewpagers.setAdapter(adapter);
		}
		viewpagers.setCurrentItem(position);

		viewpagers.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		Window window = myDialogs.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.PopupAnimation);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = LayoutParams.MATCH_PARENT;
		wl.height = LayoutParams.MATCH_PARENT;
		// 设置显示位置
		myDialogs.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		myDialogs.setCanceledOnTouchOutside(true);
		myDialogs.show();
	}
	@Override
	public void onClick(View view) {
		if(myDialog!=null){
			myDialog.dismiss();

		}

	}
	public static void closeSharePopup(){
		if(dialog!=null){
			dialog.dismiss();
		}
	}
	public static void closePopup(){
		if(cityDialog!=null){
			cityDialog.dismiss();

		}
	}

	public static void showView(Context context, String[] content, final OnFinishListener listener) {
		View view = View.inflate(context, R.layout.custom_select_view, null);
		final WheelView wv_view = (WheelView) view.findViewById(R.id.wv_view);
		TextView tv_commit = (TextView) view.findViewById(R.id.tv_commit);
		tv_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (listener != null) {
					listener.onFinish(wv_view);
				}
			}
		});

		wv_view.setAdapter(new ArrayWheelAdapter<String>(content));
		wv_view.setCurrentItem(0);

		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// dialog.setContentView(view, new
		// LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		dialog.setContentView(view);
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.PopupAnimation);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = LayoutParams.MATCH_PARENT;
		wl.height = LayoutParams.WRAP_CONTENT;
		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();

	}

	/**
	 * Updates the ccity wheel
	 */
	public static void updatecCities(WheelView city, String ccities[][][], int index, int index2) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(ccities[index][index2]);
		city.setAdapter(adapter);
		city.setCurrentItem(0);
	}

	/**
	 * Updates the city wheel
	 */
	public static void updateCities(WheelView city, String cities[][], int index) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(cities[index]);
		city.setAdapter(adapter);
		city.setCurrentItem(0);
	}

	/**
	 * Adapter for countries
	 */
	public static class CountryAdapter extends ArrayWheelAdapter<String> {

		public CountryAdapter() {
			super(AddressData.PROVINCES, AddressData.PROVINCES.length);
		}

	}


	public interface OnFinishListener {
		public void onFinish(WheelView view);

		public void onFinish(String country, String city, String ccity);
	}
}
