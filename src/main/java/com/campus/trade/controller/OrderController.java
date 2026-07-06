package com.campus.trade.controller;

import com.campus.trade.common.ApiResponse;
import com.campus.trade.dto.OrderDtos.CreateOrderRequest;
import com.campus.trade.dto.OrderDtos.OrderResponse;
import com.campus.trade.security.SecurityUtils;
import com.campus.trade.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ApiResponse<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        return ApiResponse.ok(orderService.create(SecurityUtils.currentUserId(), request));
    }

    @GetMapping
    public ApiResponse<Page<OrderResponse>> mine(@PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ApiResponse.ok(orderService.mine(SecurityUtils.currentUserId(), pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok(orderService.detail(id, SecurityUtils.currentUserId()));
    }

    @PostMapping("/{id}/confirm")
    public ApiResponse<OrderResponse> confirm(@PathVariable Long id) {
        return ApiResponse.ok(orderService.confirm(id, SecurityUtils.currentUserId()));
    }

    @PostMapping("/{id}/complete")
    public ApiResponse<OrderResponse> complete(@PathVariable Long id) {
        return ApiResponse.ok(orderService.complete(id, SecurityUtils.currentUserId()));
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<OrderResponse> cancel(@PathVariable Long id) {
        return ApiResponse.ok(orderService.cancel(id, SecurityUtils.currentUserId()));
    }
}
