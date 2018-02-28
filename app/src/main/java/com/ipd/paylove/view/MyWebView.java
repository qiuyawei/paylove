package com.ipd.paylove.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by lenovo on 2017/4/17.
 */

public class MyWebView extends WebView {
    public MyWebView(Context context) {
        this(context, null);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private float vRadius = 20;
    private int vWidth;
    private int vHeight;
    private int x;
    private int y;
    private Paint paint1;
    private Paint paint2;

    private void init(Context context) {
        paint1 = new Paint();
        paint1.setColor(Color.WHITE);
        paint1.setAntiAlias(true);
        paint1.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        //  
        paint2 = new Paint();
        paint2.setXfermode(null);
    }

    public void setRadius(float radius) {
        vRadius = radius;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        vWidth = getMeasuredWidth();
        vHeight = getMeasuredHeight();
    }

    @Override
    public void draw(Canvas canvas) {

        x = this.getScrollX();
        y = this.getScrollY();
        Bitmap bitmap = Bitmap.createBitmap(x + vWidth, y + vHeight,
                Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap);
        super.draw(canvas2);
        drawLeftUp(canvas2);
        drawRightUp(canvas2);
        drawLeftDown(canvas2);
        drawRightDown(canvas2);
        canvas.drawBitmap(bitmap, 0, 0, paint2);
        bitmap.recycle();
    }

    private void drawLeftUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(x, vRadius);
        path.lineTo(x, y);
        path.lineTo(vRadius, y);
        path.arcTo(new RectF(x, y, x + vRadius * 2, y + vRadius * 2), -90, -90);
        path.close();
        canvas.drawPath(path, paint1);
    }


    private void drawLeftDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(x, y + vHeight - vRadius);
        path.lineTo(x, y + vHeight);
        path.lineTo(x + vRadius, y + vHeight);
        path.arcTo(new RectF(x, y + vHeight - vRadius * 2,
                x + vRadius * 2, y + vHeight), 90, 90);
        path.close();
        canvas.drawPath(path, paint1);
    }


    private void drawRightDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(x + vWidth - vRadius, y + vHeight);
        path.lineTo(x + vWidth, y + vHeight);
        path.lineTo(x + vWidth, y + vHeight - vRadius);
        path.arcTo(new RectF(x + vWidth - vRadius * 2, y + vHeight
                - vRadius * 2, x + vWidth, y + vHeight), 0, 90);
        path.close();
        canvas.drawPath(path, paint1);
    }


    private void drawRightUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(x + vWidth, y + vRadius);
        path.lineTo(x + vWidth, y);
        path.lineTo(x + vWidth - vRadius, y);
        path.arcTo(new RectF(x + vWidth - vRadius * 2, y, x + vWidth,
                y + vRadius * 2), -90, 90);
        path.close();
        canvas.drawPath(path, paint1);
    }
}
