package com.babyfs.service;

import com.babyfs.dto.OrderDTO;

/**
 * @author 颛顼
 * @Description:
 * @date 2018-03-23 下午9:31
 */
public interface BuyerService {
    //查询一个订单
    OrderDTO findOrderOne(String openid, String orderId);

    //取消订单
    OrderDTO cancelOrder(String openid, String orderId);
}
