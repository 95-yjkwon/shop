package com.keduit.shop.dto;

import com.keduit.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderItemDTO {

    public OrderItemDTO(OrderItem orderItem, String imgUrl){

        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;

    }

    private String itemNm;
    private int count;
    private int orderPrice;
    private String imgUrl;
}
