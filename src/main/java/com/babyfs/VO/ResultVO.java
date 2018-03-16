package com.babyfs.VO;

public class ResultVO<T> {
    /** 错误码 */
    private Integer code;
    /** 错误信息 */
    private String msg;
    private T data;
}
