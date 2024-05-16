package com.keduit.shop.entity;

import com.keduit.shop.constant.OrderStatus;
import groovyjarjarantlr4.v4.runtime.atn.SemanticContext;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문 상태


    //Order와 orderitem은 1대 다의 연관관계를 가진다.
    //외래키가 orderitem에 있으므로 연관 관계의 주인은 orderitem이 됨 =>order는 주인이 아니므로 mappedby=order를 추가
    //여기에서 order는 orderItem에서 관리하는 이름
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true ,fetch = FetchType.LAZY)
    private List<OrderItem>orderItems=new ArrayList<>();


    //orderItems에는 주문 상품 정보를 추가함. orderItem객체를 order객체의 orderItems에 추가함.
    //order엔티티와 orderitem엔티티가 양방향 참조관계이므로 , orderitem객체에도 order객체를 넣어줌
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order=new Order();
        order.setMember(member);
        for(OrderItem orderItem:orderItemList){
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;

    }
    public int getTotalPrice(){
        int totalPrice=0;
        for(OrderItem orderItem:orderItems){
            totalPrice+=orderItem.getTotalPrice();
        }
        return totalPrice;
    }
    public void cancelOrder(){
        //주문 상태를 cancel로 변경
        this.orderStatus=OrderStatus.CANCEL;
        //주문 상품의 주문수량을 재고에 증가시킴
        for(OrderItem orderItem:orderItems){
            orderItem.cancel();
        }
    }
}
