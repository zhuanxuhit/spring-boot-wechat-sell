package com.babyfs.controller;

import com.babyfs.VO.ResultVO;
import com.babyfs.converter.OrderFormToOrderDTOConverter;
import com.babyfs.dto.OrderDTO;
import com.babyfs.enums.ResultEnum;
import com.babyfs.exception.SellException;
import com.babyfs.form.OrderForm;
import com.babyfs.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {
    @Autowired
    private OrderService orderService;

    // 创建订单
    @PostMapping("/create")
    public ResultVO create(@Valid OrderForm orderForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("【创建订单】参数不正确, orderForm={} errmsg={}",orderForm,bindingResult.getAllErrors());
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        OrderDTO orderDTO = OrderFormToOrderDTOConverter.convert(orderForm);
        

        return new ResultVO();
    }
    // 订单列表
    // 订单详情
    // 取消订单
}
