package com.keduit.shop.repository;

import com.keduit.shop.dto.MemberFormDTO;
import com.keduit.shop.entity.Cart;
import com.keduit.shop.entity.Member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
//@Transactional
public class CartRepositoryTests {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @PersistenceContext
    EntityManager em;

    public Member createMember(){
        MemberFormDTO memberFormDTO = new MemberFormDTO();
        memberFormDTO.setEmail("test5256@email.com");
        memberFormDTO.setName("홍길동");
        memberFormDTO.setAddress("서울시");
        memberFormDTO.setPassword("12345678");
        return Member.createMember(memberFormDTO, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest(){
        Member member = createMember();
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

//      엔티티 메니저 데이터 베이스에 반영
//        em.flush();
//        em.clear();

        Cart saveCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(saveCart.getMember().getId(), member.getId());
    }
}