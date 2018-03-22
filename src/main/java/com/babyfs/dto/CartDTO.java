package com.babyfs.dto;

import lombok.Data;

/**
 * @author 颛顼
 * @Description:
 * @date 2018-03-22 下午10:56
 */
@Data
public class CartDTO {
    /**
     * 商品Id.
     */
    private String productId;

    /**
     * 数量.
     */
    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
