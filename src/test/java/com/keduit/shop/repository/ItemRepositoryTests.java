package com.keduit.shop.repository;

import com.keduit.shop.constant.ItemSellStatus;
import com.keduit.shop.entity.Item;
import com.keduit.shop.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ItemRepositoryTests {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createIStemTest(){
        Item item=new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
//        item.setRegDate(LocalDateTime.now());
        Item savedItem=itemRepository.save(item);
        System.out.println(savedItem);

    }

    @Test
    @DisplayName("상품 저장 목록 테스트")
    public void createItemListTest(){

        for(int i=1; i<=10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품"+i);
            item.setPrice(10000+i*10000);
            item.setItemDetail("상품 상세 설명 테스트"+i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
//        item.setRegDate(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
            System.out.println(savedItem);
        }
    }


    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTests(){
        List<Item> itemList=itemRepository.findByItemNm("테스트 상품");
        for(Item item:itemList){
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("상품명, 상품상세 설명 or 테스트")
    public void findByItemNmOrItemDetailTests(){
        List<Item>itemList=itemRepository.findByItemNmOrItemDetail("테스트 상품1", "상품 상세 설명10");
        for (Item item:itemList){
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTests(){
        List<Item>itemList=itemRepository.findByPriceLessThan(50000);
        for(Item item:itemList){
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc(){
        List<Item>itemList=itemRepository.findByPriceLessThanOrderByPriceDesc(80000);
        for (Item item:itemList){
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("@QUERY를 이용한 상품 조회 테스트")
    public void findbyItemDetailTests() {
        List<Item> itemList = itemRepository.findByItemDetail("상품");
        for (Item item:itemList){
            System.out.println(item);
        }
    }
    @Test
    @DisplayName("native속성을 이용한 상품 조회 테스트")
    public void findByItemDetailByNativeTests() {
        List<Item> itemList = itemRepository.findByItemDetailByNative("상품 상세");
        for (Item item : itemList) {
            System.out.println(item);
        }
    }


    @Test
    @DisplayName("QueryDsl테스트")
    public void queryDslTest(){
        JPAQueryFactory queryFactory=new JPAQueryFactory(em);
        QItem qItem=QItem.item;
        List<Item>list=queryFactory
                .select(qItem)
                .from(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%"+"상품"+"%"))
                .orderBy(qItem.price.desc())
                .fetch();
        for (Item item:list){
            System.out.println("item="+item);
        }
    }

    @Test
    @DisplayName("상품 querydsl 조회 test2")
    public void queryDslTest2(){
        //쿼리에 들어 갈 내용을 필드에 넣어준다.
        BooleanBuilder booleanBuilder=new BooleanBuilder();
        QItem item=QItem.item;
        String itemDetail="테스트";
        int price=11000;
        String itemSellStat="SELL";

        booleanBuilder.and(item.itemDetail.like("%"+itemDetail+"%"));
        booleanBuilder.and(item.price.gt(price));

       if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){
           booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
       }
        Pageable pageable= PageRequest.of(0,5);
        Page<Item>itemPageResult=itemRepository.findAll(booleanBuilder,pageable);
        System.out.println("total elements"+
                itemPageResult.getTotalElements());
        List<Item>resultItemList=itemPageResult.getContent();
        for(Item resultItem: resultItemList){
            System.out.println(resultItem);
        }
    }
}