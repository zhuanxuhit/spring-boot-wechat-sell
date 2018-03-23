package com.babyfs.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class OrderForm {
    // 输入参数
    /**
     name: "张三"
     phone: "18868822111"
     address: "慕课网总部"
     openid: "ew3euwhd7sjw9diwkq" //用户的微信openid
     items: [{
     productId: "1423113435324",
     productQuantity: 2 //购买数量
     }]
     */
    @NotEmpty(message="姓名必填")
    private String name;
    @NotEmpty(message="手机号必填")
    private String phone;
    @NotEmpty(message="地址必填")
    private String address;
    @NotEmpty(message="openid必填")
    private String openid;
    @NotEmpty(message="购物车不能为空")
    private String items;
}
