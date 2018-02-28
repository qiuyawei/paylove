package com.ipd.paylove.entity;

/**
 * Created by qiuyawei on 2016/12/6.
 */
public class OrderDetailEntity {
    public String  order_no ;
    public String   order_status;
    public String   send_name;
    public String   send_phone;
    public String   send_addrprov;
    public String   send_addr;
    public String   create_time;
    public String   allprice;

    public String  isverify ;
    public String   verify_result;
    public String   order_desc;
    public String   status_desc;
    public String   dk_name;
    public String   zd_name;
    public String   team_name;
    public String   send_wl;
    public String    send_mark;
    public OrderDetailInner[] goods;
/*
    "order_no": "23115",
            "order_status": "H",
            "send_name": "李惠娟",
            "send_phone": "13894020734",
            "send_addrprov": "吉林省",
            "send_addr": "白山市抚松县客运站家属楼对面车库3库",
            "create_time": "2016-04-14 10:37:31",
            "allprice": "520.00",
            "isverify": "审核通过",
            "verify_result": "暂无",
            "order_desc": "暂无",
            "status_desc": "已发货",
            "dk_name": "蓓蓓",
            "zd_name": "邓晓钰",
            "team_name": "公主钰",*/
}
