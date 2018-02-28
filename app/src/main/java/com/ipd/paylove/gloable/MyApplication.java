package com.ipd.paylove.gloable;

import com.ipd.paylove.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.ImageView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class MyApplication extends Application {
	public static List<Activity> activityList=new ArrayList<>();
	public static OkHttpClient client=new OkHttpClient();
	public static  MyApplication instance;
	@Override
	public void onCreate() {
		super.onCreate();
		x.Ext.init(this);//Xutils初始化
		instance=this;
	}

	public static MyApplication getInstance(){
		return instance;
	}

/******把打开的acitvity添加到容器中*****/
	public static  void addActivity(Activity activity){
		activityList.add(activity);
	}



}
