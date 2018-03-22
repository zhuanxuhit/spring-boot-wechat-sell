package com.babyfs.converter;

import com.babyfs.dataobject.OrderMaster;
import com.babyfs.dto.OrderDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 颛顼
 * @Description:
 * @date 2018-03-22 下午11:19
 */
public class OrderMasterToOrderDTOConverter {
    public static OrderDTO convert(OrderMaster orderMaster) {
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        return orderDTO;
    }

    public static List<OrderDTO> convert(List<OrderMaster> orderMasterList) {
        return orderMasterList.stream().map(OrderMasterToOrderDTOConverter::convert
        ).collect(Collectors.toList());
    }
}
