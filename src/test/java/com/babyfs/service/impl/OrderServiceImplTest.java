package com.babyfs.service.impl;

import com.babyfs.dataobject.OrderDetail;
import com.babyfs.dto.OrderDTO;
import com.babyfs.enums.OrderStatusEnum;
import com.babyfs.enums.PayStatusEnum;
import com.babyfs.repository.OrderMasterRepository;
import com.babyfs.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author 颛顼
 * @Description:
 * @date 2018-03-23 上午8:10
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    private final String BUYER_OPENID = "1101110";

    @Test
    public void create() {
        OrderDTO orderDTO = createOrder();
        log.info("【创建订单】result={}", orderDTO);
    }

    @Test
    @Transactional
    public void findOne() {
        OrderDTO orderDTO = createOrder();
        OrderDTO result = orderService.findOne(orderDTO.getOrderId());
        log.info("【查询单个订单】result={}", orderDTO);
        assertEquals(result.getOrderId(), orderDTO.getOrderId());
    }

    @Test
    @Transactional
    public void findList() {
        // 先清空数据库
        orderMasterRepository.deleteAll();
        OrderDTO orderDTO = createOrder();
        PageRequest request = PageRequest.of(0,2);
        Page<OrderDTO> orderDTOPage = orderService.findList(orderDTO.getBuyerOpenid(), request);
        log.info("【查询用户订单】result={}", orderDTOPage);
        assertNotEquals(0, orderDTOPage.getTotalPages());
    }

    @Test
    @Transactional
    public void findListAll() {
        // 先清空数据库
        orderMasterRepository.deleteAll();
        createOrder();
        PageRequest request = PageRequest.of(0,2);
        Page<OrderDTO> orderDTOPage = orderService.findListAll(request);
        log.info("【查询所有订单】result={}", orderDTOPage);
        assertNotEquals(0, orderDTOPage.getTotalPages());
    }

    @Test
    @Transactional
    public void cancel() {
        OrderDTO orderDTO = createOrder();
        assertEquals(orderDTO.getOrderStatus(), OrderStatusEnum.NEW.getCode());
        // 取消订单
        orderService.cancel(orderDTO);
        assertEquals(OrderStatusEnum.CANCEL.getCode(), orderDTO.getOrderStatus());
    }

    @Test
    @Transactional
    public void finish() {
        OrderDTO orderDTO = createOrder();
        assertEquals(orderDTO.getOrderStatus(), OrderStatusEnum.NEW.getCode());
        // 取消订单
        orderService.finish(orderDTO);
        assertEquals(OrderStatusEnum.FINISHED.getCode(), orderDTO.getOrderStatus());
    }

    @Test
    @Transactional
    public void paid() {
        OrderDTO orderDTO = createOrder();
        assertEquals(orderDTO.getPayStatus(), PayStatusEnum.WAIT.getCode());
        // 取消订单
        orderService.paid(orderDTO);
        assertEquals(PayStatusEnum.SUCCESS.getCode(), orderDTO.getPayStatus());
    }

    private OrderDTO createOrder(){
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setBuyerName("lolood");
        orderDTO.setBuyerAddress("武汉 洪山");
        orderDTO.setBuyerPhone("18576649086");
        orderDTO.setBuyerOpenid(BUYER_OPENID);

        //购物车
        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail o1 = new OrderDetail();
        o1.setProductId("1234567891");
        o1.setProductQuantity(1);

        OrderDetail o2 = new OrderDetail();
        o2.setProductId("1234567892");
        o2.setProductQuantity(2);

        orderDetailList.add(o1);
        orderDetailList.add(o2);

        orderDTO.setOrderDetailList(orderDetailList);

        orderService.create(orderDTO);
        return orderDTO;
    }
}