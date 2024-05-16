package com.keduit.shop.entity;


import com.keduit.shop.constant.Role;
import com.keduit.shop.dto.MemberFormDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
//extends baseEntity:작성자, 수정자, 작성일, 수정일을 자동 등록

@Entity
@Getter
@Setter
@ToString
public class Member extends BaseEntity{

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDTO memberFormDTO,
                                      PasswordEncoder passwordEncoder){
        Member member=new Member();
        member.setName(memberFormDTO.getName());
        member.setEmail(memberFormDTO.getEmail());
        member.setAddress(memberFormDTO.getAddress());
        String password=passwordEncoder.encode(memberFormDTO.getPassword());
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return member;
    }
}
