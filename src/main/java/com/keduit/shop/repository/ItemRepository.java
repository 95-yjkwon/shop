package com.keduit.shop.repository;

import com.keduit.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

//<entity의 이름, pk의 타입>

//QuerydslPredicateExecutor: querydsl사용시 조건에 맞는 조회를 위해 추가
public interface ItemRepository extends JpaRepository<Item, Long>,
        QuerydslPredicateExecutor<Item>,
        ItemRepositoryCustom{
    //쿼리 메서드 또는 기본으로 가지고 있는 crud
    List<Item> findByItemNm(String itemNm);

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    @Query("select i from Item i where i.itemDetail like"+
            " %:itemDetail% order by i.price desc")
    List<Item>findByItemDetail(@Param("itemDetail") String itemDetail);


    @Query(value = "select * from item i where i.item_detail like "+
            "%:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);




}

