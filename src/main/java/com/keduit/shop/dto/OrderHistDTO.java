package com.keduit.shop.dto;

import com.keduit.shop.constant.OrderStatus;
import com.keduit.shop.entity.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class OrderHistDTO {

    private Long orderId;               // 주문 아이디
    private String orderDate;           // 주문 날짜
    private OrderStatus orderStatus;    // 주문 상태

    public OrderHistDTO(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    // 주문 상품 리스트
    private List<OrderItemDTO> orderItemDTOList = new ArrayList<>();

    // OrderItemDTO를 주문 상품 리스트에 추가
    public void addOrderItemDTO(OrderItemDTO orderItemDTO){
        orderItemDTOList.add(orderItemDTO);
    }

}