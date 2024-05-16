package com.keduit.shop.controller;

import com.keduit.shop.dto.MemberFormDTO;
import com.keduit.shop.entity.Member;
import com.keduit.shop.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberControllerTests {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(String email, String password){
        MemberFormDTO memberFormDTO=new MemberFormDTO();
        memberFormDTO.setEmail(email);
        memberFormDTO.setName("용춘권");
        memberFormDTO.setAddress("서울시 관악구 봉천동");
        memberFormDTO.setPassword("1234");
        Member member=Member.createMember(memberFormDTO, passwordEncoder);

        return memberService.saveMember(member);

    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception{
        String email="test122132132131@hong.com";
        String password="1234";
        this.createMember(email, password);
        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(email).password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }


    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception{
        String email="test122132132131@hong.com";
        String password="1234324";
        this.createMember(email, password);
        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl("/members/login")
                        .user(email).password(password))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }


}
