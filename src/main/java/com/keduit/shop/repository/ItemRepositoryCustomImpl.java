package com.keduit.shop.repository;

import com.keduit.shop.constant.ItemSellStatus;
import com.keduit.shop.dto.ItemSearchDTO;
import com.keduit.shop.dto.MainItemDTO;
import com.keduit.shop.dto.QMainItemDTO;
import com.keduit.shop.entity.Item;
import com.keduit.shop.entity.QItem;
import com.keduit.shop.entity.QItemImg;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

//where구문 넣어 주는 곳
//Itemrepositorycustom의 구현체이는 ~impl을 붙여야 잘 작동함
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    //동적쿼리 생성 위해 쿼리팩토리 가져옴.
    private JPAQueryFactory queryFactory;

    //ItemRepositoryCustomImpl는 EntityManager가 필요하고,
    //queryFactory를 생성함
    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory=new JPAQueryFactory(em);
    }
    //상품판매 상태 조건이 전체이면 NULL을 리턴.이 때 WHERE절에서 해당 조건은 무시됨
    //상태조건이 있으면 해당 조건을 사용하여 WHERE구문이 만들어짐
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus==null? null: QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType){
        LocalDateTime dateTime=LocalDateTime.now();
        if(StringUtils.equals("all", searchDateType) ||searchDateType==null){
            return null;

        }else if(StringUtils.equals("1d", searchDateType)){
            dateTime=dateTime.minusDays(1);
        }else if(StringUtils.equals("1w", searchDateType)){
            dateTime=dateTime.minusWeeks(1);
        }else if(StringUtils.equals("1m", searchDateType)){
            dateTime=dateTime.minusMonths(1);
        }else if(StringUtils.equals("6m", searchDateType)){
            dateTime=dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%"+searchQuery+"%");
        }else if(StringUtils.equals("createBy", searchBy)){
            return QItem.item.createdBy.like("%"+searchQuery+"%");
        }
        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable) {

        System.out.println("itemSearchDTO=====>"+itemSearchDTO);
        System.out.println("pageable===>"+pageable);
        List<Item>result=queryFactory.selectFrom(QItem.item)
                        .where(regDtsAfter(itemSearchDTO.getSearchDateType()),
                        searchSellStatusEq(itemSearchDTO.getSearchSellStatus()),
                        searchByLike(itemSearchDTO.getSearchBy(),itemSearchDTO.getSearchQuery())
                         )
                .orderBy(QItem.item.id.desc())
                //pageable.getOffset()은 현재 페이지의 시작 위치를 반환하는 메소드입니다.
                .offset(pageable.getOffset())
                //한 페이지에 보여줄 결과의 최대 개수를 설정하는 메서드입니다.
                .limit(pageable.getPageSize())
                .fetch();

        long total=queryFactory
                .select(Wildcard.count)
                .from(QItem.item)
                .where(regDtsAfter(itemSearchDTO.getSearchDateType()),
                        searchSellStatusEq(itemSearchDTO.getSearchSellStatus()),
                        searchByLike(itemSearchDTO.getSearchBy(),itemSearchDTO.getSearchQuery()))
                .fetchOne(); //하나의 결과를 읽어옴




        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Page<MainItemDTO> getMianItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable) {
        QItem item=QItem.item;
        QItemImg itemImg=QItemImg.itemImg;

        List<MainItemDTO> result=queryFactory
                .select(
                        new QMainItemDTO(item.id, item.itemNm, item.itemDetail, itemImg.imgUrl, item.price)
                ).from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDTO.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total=queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDTO.getSearchQuery()))
                .fetchOne();
        return new PageImpl<>(result, pageable, total);
    }

    private Predicate itemNmLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery)?null:
                QItem.item.itemNm.like("%"+searchQuery+"%");
    }

}
