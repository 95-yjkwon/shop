package com.keduit.shop.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CartOrderDTO {
    //장바구니에서 여러개의 상품을 주문하므로 CartOrderdto클래스에 자기 자신을List로 가지고 있는 cartOrderDTOList를 선언함.
    private Long cartItemId;
    private List<CartOrderDTO>cartOrderDTOList;
}
