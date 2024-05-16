package com.keduit.shop.repository;

import com.keduit.shop.dto.CartDetailDTO;
import com.keduit.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    //  카트 아이디와 상품 아이디를 주고 장바구니 상품을 읽어 옴.
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);
    //jpql에서 연관관계 지연 로딩으로 설정한 경우 엔티티에 매핑된 다른 엔테티를 조회할 때 추가적으로 쿼리문이 실행되는데 이 때 성능 최적화를 위해 dto의 생성자를 이용하여 반환값으로 dto객체를 생성할 수 있다.
    // cartDetailDTO의 생성자를 이용하여 dto를 반환:패키지명을 포함한 dto의 이름을 기술
    //주의사항: 생성자의 파라미터 순서는 꼭 지켜야 함.
    @Query("select  new com.keduit.shop.dto.CartDetailDTO(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) "+
            "from CartItem ci, ItemImg im "+
            "join ci.item i "+
            "where ci.cart.id=:cartId "+
            "and im.item.id=ci.item.id "+
            "and im.repimgYn='Y' "+
            "order by ci.regTime desc")
    //파람 어노테이션을 붙여주지 않는경우
    //1.파라미터 1개 2. 파라미터 이름과 매핑명이 동일 3.JPA 2.0이상
    //위의 세가지 조건이 만족할 때 @PARAM은 생갹 가능 @param("cartId") Long cartId
    List<CartDetailDTO> findCartDetailDTOList(Long cartId);
}