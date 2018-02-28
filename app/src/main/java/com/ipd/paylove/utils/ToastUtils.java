package com.ipd.paylove.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/5/4.
 */
public class ToastUtils {
    public static void show(Context context,String s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
}
