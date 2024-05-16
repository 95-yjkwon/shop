package com.keduit.shop.entity;

import com.keduit.shop.constant.ItemSellStatus;
import com.keduit.shop.dto.ItemFormDTO;
import com.keduit.shop.exception.OutOfStockException;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Item extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;   //상품코드

    @Column(nullable = false, length = 50)
    private String itemNm; //상품명

    @Column(name = "price", nullable = false)
    private int price;   //가격

    @Column(nullable = false)
    private int stockNumber; //재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;   //상품 판매 현황

    public void updateItem(ItemFormDTO itemFormDTO){
        this.itemNm= itemFormDTO.getItemNm();
        this.price= itemFormDTO.getPrice();
        this.stockNumber= itemFormDTO.getStockNumber();
        this.itemDetail= itemFormDTO.getItemDetail();
        this.itemSellStatus=itemFormDTO.getItemSellStatus();
    }

    public void removeStock(int stockNumber){
        int restStock=this.stockNumber-stockNumber;
        if(restStock<0){
            throw new OutOfStockException("상품 재고가 부족합니다.(현재 재고 수량:"+this.stockNumber+")");
        }
        this.stockNumber=restStock;

    }

    //주문 취소 재고 증가
    public void addStock(int stockNumber){
        this.stockNumber+=stockNumber;
    }

//
//    @CreationTimestamp
//    private LocalDateTime regDate; //등록시간
//
//    @UpdateTimestamp
//    private LocalDateTime updateTime; //수정시간


}
