package com.example.ordersystem.util;

import com.example.ordersystem.dto.*;
import com.example.ordersystem.entity.Order;
import org.springframework.beans.BeanUtils;

import static com.example.ordersystem.dto.TransactionStatus.APPROVED;

public class EntityDtoUtil {

    public static void setTransactionRequestDto(RequestContext requestContext) {
        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setUserId(requestContext.getOrderRequestDto().getUserId());
        transactionRequestDto.setAmount(requestContext.getProductDto().getPrice());
        requestContext.setTransactionRequestDto(transactionRequestDto);
    }

    public static Order getOrder(RequestContext requestContext) {
        Order order = new Order();
        order.setUserId(requestContext.getOrderRequestDto().getUserId());
        order.setProductId(requestContext.getOrderRequestDto().getProductId());
        order.setAmount(requestContext.getProductDto().getPrice());

        TransactionStatus status = requestContext.getTransactionResponseDto().getStatus();

        order.setOrderStatus(status.equals(APPROVED) ? OrderStatus.COMPLETED : OrderStatus.FAILED);

        return order;
    }

    public static OrderResponseDto toOrderResponseDto(Order order) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        BeanUtils.copyProperties(order, orderResponseDto);
        orderResponseDto.setOrderId(order.getId());
        return orderResponseDto;
    }

}
