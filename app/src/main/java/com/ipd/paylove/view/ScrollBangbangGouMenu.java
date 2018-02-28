package com.ipd.paylove.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ipd.paylove.R;

import java.util.List;

/**
 * Created by jumpbox on 16/9/16.
 */
public class ScrollBangbangGouMenu extends LinearLayout {

    private int mScreenWidth;

    public ScrollBangbangGouMenu(Context context) {
        super(context);
        init();
    }

    public ScrollBangbangGouMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollBangbangGouMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = manager.getDefaultDisplay().getWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.measure(0, 0);
            width += childView.getMeasuredWidth();
        }

        if (width < mScreenWidth) {
            width = mScreenWidth;
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int width = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.measure(0, 0);
            width += childView.getMeasuredWidth();
        }


        float dWidth = 0;
        if (width < mScreenWidth && getChildCount() > 0) {
            dWidth = ((float) (mScreenWidth - width)) / getChildCount();
            for ( int i = 0 ;i< getChildCount();i++){
                View childView = getChildAt(i);

                int left = (int) (childView.getLeft() + (i * dWidth));
                int right = (int) (left + childView.getWidth() + dWidth);

                childView.layout(left,childView.getTop(),right,childView.getBottom());
            }


        }


    }

    public void setMenu(List<String> titleList, final onMenuClickListener onMenuClickListener) {
        int pos = 0;
        for (String categoryBean : titleList) {
            LinearLayout titleMenu = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_bbg_menu, this, false);
            TextView tv_title = (TextView) titleMenu.findViewById(R.id.tv_title);
            tv_title.setText(categoryBean);

            if (pos == 0) {
                setChecked(true, titleMenu);
            } else {
                setChecked(false, titleMenu);
            }

            final int finalPos = pos;
            titleMenu.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < getChildCount(); i++) {
                        setChecked(finalPos == i ? true : false, (LinearLayout) getChildAt(i));
                    }

                    onMenuClickListener.onClick(finalPos);


                }
            });
            addView(titleMenu);
            pos++;
        }


    }


    public void setChecked(boolean isChecked, LinearLayout titleMenu) {
        ((TextView) (titleMenu.findViewById(R.id.tv_title))).setTextColor(isChecked ? getResources().getColor(R.color.main_color) :
                getResources().getColor(R.color.black));
        titleMenu.findViewById(R.id.view_btm_line).setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
    }

    public void setCheckedAtPosition(int pos) {
        for (int i = 0; i < getChildCount(); i++) {
            setChecked(pos == i ? true : false, (LinearLayout) getChildAt(i));
        }
    }


    public interface onMenuClickListener {
        void onClick(int pos);
    }
}
