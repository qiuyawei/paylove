package com.ipd.paylove.entity;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by qiuyawei on 2016/11/17.
 */
public class ShopCarEntity implements Serializable{
    public String car_id; //购物车id
    public String goods_id; //商品id
    public String goods_name; //商品名称
    public String price; //商品价格
    public String buy_count; //购买数量
    public String allprice; //总价格
    public String goods_img; //商品图片


    public boolean isCheck;
}
