package com.babyfs.thirdparty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.babyfs.dataobject.OrderDetail;
import com.babyfs.dto.CartDTO;
import com.sun.tools.javac.util.ArrayUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
public class FastJsonTest {
    @Test
    public void parseArray(){
        List<CartDTO> cartDTOList = new ArrayList<>();
        cartDTOList.add(new CartDTO("121231231",1));
        cartDTOList.add(new CartDTO("121231231",2));
        log.info(JSON.toJSONString(cartDTOList));
        String jsonString = JSON.toJSONString(cartDTOList);
        List<OrderDetail> orderDetailList = JSON.parseArray(jsonString, OrderDetail.class);
        log.info(orderDetailList.toString());
    }
    @Test(expected = JSONException.class)
    public void parseArrayException(){
        String jsonString = "{\"productId\":\"121231231\",\"productQuantity\":1},{\"productId\":\"121231231\",\"productQuantity\":2}]";
        List<OrderDetail> orderDetailList = JSON.parseArray(jsonString, OrderDetail.class);
        log.info(orderDetailList.toString());
    }
}
