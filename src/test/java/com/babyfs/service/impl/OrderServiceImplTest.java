package com.babyfs.service.impl;

import com.babyfs.dto.OrderDTO;
import com.babyfs.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author 颛顼
 * @Description:
 * @date 2018-03-23 上午8:10
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    private final String BUYER_OPENID = "1101110";

    private final String ORDERID = "1500790335414783795";

    @Test
    public void create() {
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setBuyerName("lolood");
        orderDTO.setBuyerAddress("武汉 洪山");
        orderDTO.setBuyerPhone("18576649086");
        orderDTO.setBuyerOpenid(BUYER_OPENID);

    }

    @Test
    public void findOne() {
    }

    @Test
    public void findList() {
    }

    @Test
    public void findListAll() {
    }

    @Test
    public void cancel() {
    }

    @Test
    public void finish() {
    }

    @Test
    public void paid() {
    }
}