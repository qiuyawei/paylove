package com.ipd.paylove.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/22.
 * 地址列表实体类
 */
public class EntityAddress implements Serializable {
    public String aid;//收货地址id
    public String phone;//手机
    public String uname;//收货人姓名
    public String default_status;//是否为默认地址（1是，0否）
    public String addr_prov;//省
    public String addr_name;//详细地址


}
