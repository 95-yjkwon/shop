package com.keduit.shop.service;

import com.keduit.shop.constant.ItemSellStatus;
import com.keduit.shop.dto.OrderDTO;
import com.keduit.shop.entity.Item;
import com.keduit.shop.entity.Member;
import com.keduit.shop.entity.Order;
import com.keduit.shop.entity.OrderItem;
import com.keduit.shop.repository.ItemRepository;
import com.keduit.shop.repository.MemberRepository;
import com.keduit.shop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class OrderServiceTests {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MemberRepository memberRepository;

    public Item saveItem(){
        Item item=new Item();
        item.setItemNm("와 나는 천재다");
        item.setPrice(50000);
        item.setItemDetail("상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);

        return itemRepository.save(item);
    }
    public Member saveMember(){
        Member member=new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    public void order(){
        Item item=saveItem();
        Member member=saveMember();

        OrderDTO orderDTO=new OrderDTO();
        orderDTO.setCount(10);
        orderDTO.setItemId(item.getId());

        Long orderId= orderService.order(orderDTO, member.getEmail());

        Order order=orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orederItems=order.getOrderItems();

        int totalPrice= orderDTO.getCount()*item.getPrice();

        assertEquals(totalPrice, order.getTotalPrice());


    }
}
