package com.babyfs.service;

import com.babyfs.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;

/**
 * @author 颛顼
 * @Description:
 * @date 2018-03-22 下午9:55
 */
public interface OrderService {
    /**
     * 创建订单.
     */
    @NotNull OrderDTO create(OrderDTO orderDTO);

    /**
     * 查询单个订单.
     */
    OrderDTO findOne(String orderId);

    /**
     * 根据openid查询订单列表.
     */
    Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);

    Page<OrderDTO> findListAll(Pageable pageable);

    /**
     * 取消订单.
     */
    OrderDTO cancel(OrderDTO orderDTO);

    /**
     * 完结订单.
     */
    OrderDTO finish(OrderDTO orderDTO);

    /**
     * 支付订单.
     */
    OrderDTO paid(OrderDTO orderDTO);
}
