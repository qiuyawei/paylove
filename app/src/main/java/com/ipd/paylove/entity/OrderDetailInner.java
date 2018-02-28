package com.ipd.paylove.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/22.
 * 订单列表内部小列表实体类
 */
public class OrderDetailInner implements Serializable{
    public String order_no;
    public String price;

    public String goods_id;
    public String buy_count;
    public String goods_img;
    public String goods_name;
    public String send_mark;
    /*快递单号*/
    public String send_odd;
/*
    "order_no": "23115",
            "goods_id": "94",
            "buy_count": "1",
            "goods_img": "http://www.cnfuaibao.cn/promanage/upload/goods/043.jpg",
            "goods_name": "护发素试用装
            send_mark, send_odd
"*/

}
