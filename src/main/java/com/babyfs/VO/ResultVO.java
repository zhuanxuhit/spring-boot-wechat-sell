package com.babyfs.VO;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultVO<T>{
    /** 错误码 */
    private Integer code;
    /** 错误信息 */
    private String msg;
    private T data;
}
