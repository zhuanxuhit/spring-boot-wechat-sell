package com.babyfs.enums;

import lombok.Getter;

@Getter
public enum  ResultEnum implements CodeEnum {

    PRODUCT_NOT_EXI(10,"商品不存在"),
    PRODUCT_STATUS_ERROR(23,"商品状态不正确"),
    ;

    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
