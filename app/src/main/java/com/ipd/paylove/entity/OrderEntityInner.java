package com.ipd.paylove.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 * 订单列表内部小列表实体类
 */
public class OrderEntityInner implements Serializable{


    public String goods_id;
    public String buy_count;
    public String goods_img;
    public String goods_name;
    public String orginNumber;


/*
    "order_no": "23115",
            "goods_id": "94",
            "buy_count": "1",
            "goods_img": "http://www.cnfuaibao.cn/promanage/upload/goods/043.jpg",
            "goods_name": "护发素试用装"*/

}
