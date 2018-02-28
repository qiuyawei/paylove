package com.ipd.paylove.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ipd.paylove.R;
import com.ipd.paylove.base.BaseActivity;

import org.xutils.view.annotation.ViewInject;

/*******关于我们********/
public class AboutUs extends BaseActivity {
    @ViewInject(R.id.ivImg)
    private ImageView ivImg;
    Bitmap bmp = null;
    @Override
    public int setLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setBack();
        setTopTitle("关于我们");
        fitImage(AboutUs.this,ivImg,1258,2586);

    }

    @Override
    public void setListener() {

    }



    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bmp!=null){
            bmp.recycle();
        }
    }


    public  void fitImage(Activity activity, ImageView imageView, float picWidth, float picHeight) {
        WindowManager wm = activity.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        float height = (float) width / picWidth * picHeight;
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = (int) height;
        imageView.setLayoutParams(layoutParams);
    }
}
