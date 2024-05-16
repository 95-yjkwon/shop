package com.keduit.shop.controller;

import com.keduit.shop.dto.OrderDTO;
import com.keduit.shop.dto.OrderHistDTO;
import com.keduit.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDTO orderDTO,
                                            BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            StringBuilder sb=new StringBuilder();
            List<FieldError> fieldErrors=bindingResult.getFieldErrors();
            for(FieldError fieldError:fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        //principal:@controller어노테이션이 선언된 클래스에서 메소드 인자로 principal을 넘겨 줄 경우 해당 객체에 직접 접근이 가능하며,
            //principal객체에서 현재 로그인한 회원의 이메일 정보를 조회함.
        }
        String email=principal.getName();
        Long orderId;
        try{
            //화면에서 넘어온 주문 정보와 이메일 정보를 이용하여 주문 로직을 가진 service를 호출함.
            orderId= orderService.order(orderDTO, email);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        //결과 값으로 생성된 주문 번호와 요청이 성공임을 나타내는 http응답을 반환
        return new ResponseEntity(orderId, HttpStatus.OK);

    }
    @GetMapping({"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page")Optional<Integer> page, Principal principal, Model model){
        Pageable pageable= PageRequest.of(page.isPresent()?page.get():0, 4);
        Page<OrderHistDTO>orderHistDTOList=orderService.getOrderList(principal.getName(), pageable);
        model.addAttribute("orders", orderHistDTOList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);
        return "order/orderHist";
    }
    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId")Long orderId, Principal principal){
        if(!orderService.validateOrder(orderId, principal.getName())){
            return new ResponseEntity("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
