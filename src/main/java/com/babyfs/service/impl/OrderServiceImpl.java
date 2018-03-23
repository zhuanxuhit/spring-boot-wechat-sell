package com.babyfs.service.impl;

import com.babyfs.converter.OrderMasterToOrderDTOConverter;
import com.babyfs.dataobject.OrderDetail;
import com.babyfs.dataobject.OrderMaster;
import com.babyfs.dataobject.ProductInfo;
import com.babyfs.dto.CartDTO;
import com.babyfs.dto.OrderDTO;
import com.babyfs.enums.OrderStatusEnum;
import com.babyfs.enums.PayStatusEnum;
import com.babyfs.enums.ResultEnum;
import com.babyfs.exception.SellException;
import com.babyfs.repository.OrderDetailRepository;
import com.babyfs.repository.OrderMasterRepository;
import com.babyfs.service.OrderService;
import com.babyfs.service.ProductService;
import com.babyfs.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 颛顼
 * @Description:
 * @date 2018-03-22 下午10:09
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        String orderId = KeyUtil.genUniqueKey();
        // 1. 查询商品价格
        for (OrderDetail orderDetail :
                orderDTO.getOrderDetailList()) {
            Optional<ProductInfo> optionalProductInfo = productService.findOne(orderDetail.getProductId());
            if (!optionalProductInfo.isPresent()) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXI);
            }
            ProductInfo productInfo = optionalProductInfo.get();
            // 2. 计算总价
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo, orderDetail);
            // 写入 OrderDetail
            orderDetailRepository.save(orderDetail);
        }
        // 3. 写入订单数据（主订单，）
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        // 注意此处 setOrderId 操作应该放在 copyProperties 之后，不然会被设置为null
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);
        // 4. 扣库存
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(
                e -> new CartDTO(e.getProductId(), e.getProductQuantity())
        ).collect(Collectors.toList());

        productService.decreaseStock(cartDTOList);
        // 重新将值赋值回去，例如最重要的 Order_id,orderAmount
        BeanUtils.copyProperties(orderMaster, orderDTO);
        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderDTO orderDTO = new OrderDTO();
        Optional<OrderMaster> optionalOrderMaster = orderMasterRepository.findById(orderId);
        if (!optionalOrderMaster.isPresent()) {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXI);
        }
        // 设置detail
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new SellException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }
        orderDTO.setOrderDetailList(orderDetailList);
        BeanUtils.copyProperties(optionalOrderMaster.get(), orderDTO);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
        List<OrderDTO> orderDTOList = OrderMasterToOrderDTOConverter.convert(orderMasterPage.getContent());
        return new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }

    @Override
    public Page<OrderDTO> findListAll(Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findAll(pageable);
        List<OrderDTO> orderDTOList = OrderMasterToOrderDTOConverter.convert(orderMasterPage.getContent());
        return new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());

    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【取消订单操作】 订单状态不正确 orderId={},orderStats={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);

        // save 方法永远不会返回null
        orderMasterRepository.save(orderMaster);
//        if (updateResult == null) {
//            log.error("取消订单失败,orderMaster={}", orderMaster);
//            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
//        }
        //返回库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【取消订单】订单中无商品详情, orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e ->
                new CartDTO(e.getProductId(), e.getProductQuantity())
        ).collect(Collectors.toList());
        productService.increaseStock(cartDTOList);
        //支付成功退款
        if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS)) {
//            payService.refund(orderDTO);
        }
        return orderDTO;
    }


    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【取消订单操作】 订单状态不正确 orderId={},orderStats={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);

        orderMasterRepository.save(orderMaster);

//        if (updateResult == null) {
//            log.error("取消完结失败,orderMaster={}", orderMaster);
//            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
//        }

//        pushMessageService.orderStatus(orderDTO);
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【支付订单操作】 订单状态不正确 orderId={},orderStats={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【支付订单操作】 订单支付状态不正确 orderId={},orderStats={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        //修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        // save 操作是先select再决定是insert还是update
        orderMasterRepository.save(orderMaster);

//        if (updateResult == null) {
//            log.error("支付订单失败,orderMaster={}", orderMaster);
//            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
//        }
        return orderDTO;
    }
}
