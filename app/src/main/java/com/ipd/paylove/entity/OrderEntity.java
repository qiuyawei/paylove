package com.ipd.paylove.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/22.
 * 订单实体类
 */
public class OrderEntity implements Serializable{
    public String order_no;
    public OrderEntityInner[] goods;
    public String order_status;
    public String create_time;
    public String allprice;
    public String status_desc;
    public String    delay_day;//延迟发货
    /*预计发货时间*/
    public String    send_time;
    public String    send_name;

}
