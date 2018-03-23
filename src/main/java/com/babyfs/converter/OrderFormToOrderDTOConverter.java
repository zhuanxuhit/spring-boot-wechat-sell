package com.babyfs.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.babyfs.dataobject.OrderDetail;
import com.babyfs.dto.OrderDTO;
import com.babyfs.enums.ResultEnum;
import com.babyfs.exception.SellException;
import com.babyfs.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderFormToOrderDTOConverter {
    public static OrderDTO convert(OrderForm orderForm){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
            orderDetailList = JSON.parseArray(orderForm.getItems(),OrderDetail.class);
        }catch (Exception e){
            log.error("【格式转换错误】result={} msg={}",orderForm.getItems(),e.getMessage());
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),e.getMessage());
        }
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }
}
