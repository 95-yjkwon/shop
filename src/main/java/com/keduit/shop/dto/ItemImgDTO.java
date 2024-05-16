package com.keduit.shop.dto;


import com.keduit.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
public class ItemImgDTO {
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repimgYn;

    //멤버 변수로 modelmapper객체를 추가함
    private static ModelMapper modelMapper=new ModelMapper();

    //엔티티 줄거니까 dto주삼
    //public static Member createMember(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder) 이 메서드를 굳이 써줄 필요가 없다.
    public static ItemImgDTO of(ItemImg itemImg){
        return modelMapper.map(itemImg, ItemImgDTO.class);
    }

}
