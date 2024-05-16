package com.keduit.shop.service;


import com.keduit.shop.constant.ItemSellStatus;
import com.keduit.shop.dto.CartItemDTO;
import com.keduit.shop.entity.CartItem;
import com.keduit.shop.entity.Item;
import com.keduit.shop.entity.Member;
import com.keduit.shop.repository.CartItemRepository;
import com.keduit.shop.repository.ItemRepository;
import com.keduit.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
public class CartServiceTests {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;

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
    @DisplayName("장바구니 담기 테스트")
    public void addCart(){
        Item item=saveItem();
        Member member=saveMember();

        CartItemDTO cartItemDTO=new CartItemDTO();
        cartItemDTO.setCount(50);
        cartItemDTO.setItemId(item.getId());

        Long cartItemId= cartService.addCart(cartItemDTO, member.getEmail());

        CartItem cartItem=cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);


        assertEquals(item.getId(), cartItem.getItem().getId());
        assertEquals(cartItemDTO.getCount(), cartItem.getCount());
    }
}
