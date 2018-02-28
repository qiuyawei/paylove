package com.ipd.paylove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ipd.paylove.R;
import com.ipd.paylove.adapter.ViewPagerAdapter;
import com.ipd.paylove.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/2/16.
 * 引导页
 */
public class Guid extends BaseActivity {
    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    //引导图片资源
    private static final int[] pics = {R.mipmap.guid_one, R.mipmap.guid_two, R.mipmap.guid_three};
    //底部小点图片
    private ImageView[] dots;
    //记录当前选中位置
    private int currentIndex;

    @Override
    public int setLayout() {
        return R.layout.guid;
    }

    @Override
    public void init(Bundle savedInstanceState) {

        views = new ArrayList<View>();

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        //初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setImageResource(pics[i]);
            views.add(iv);
        }
        vp = (ViewPager) findViewById(R.id.viewpager);
        //初始化Adapter
        vpAdapter = new ViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //设置底部小点选中状态
//                setCurDot(position);
                if (position == pics.length - 1) {
                    vp.getChildAt(0).setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            Intent intent = new Intent(Guid.this, TotalActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    });


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //初始化底部小点
//        initDots();

    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
    }


    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[pics.length];

        //循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);//都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);//设置为白色，即选中状态
    }

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }

        vp.setCurrentItem(position);

    }

    /**
     * 这只当前引导小点的选中
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }

        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = positon;
    }


}
