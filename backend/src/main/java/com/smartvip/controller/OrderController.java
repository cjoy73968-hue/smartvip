/*
 * Copyright (C) 2024 会员管理系统项目
 *
 * 本项目采用 AGPL-3.0 开源协议开源。
 *
 * 您可以自由使用、修改和分发本项目，但若通过网络使用（包括修改后部署），
 * 也必须按照 AGPL-3.0 协议开源您的修改版本。
 *
 * 完整协议文本请参见 LICENSE 文件。
 */
package com.smartvip.controller;

import com.smartvip.dto.ApiResponse;
import com.smartvip.dto.ConsumeRequest;
import com.smartvip.entity.ConsumptionOrder;
import com.smartvip.entity.Gift;
import com.smartvip.service.OrderService;
import com.smartvip.service.GiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GiftService giftService;

    @GetMapping("/user/{userId}")
    public ApiResponse<List<ConsumptionOrder>> getOrders(@PathVariable Long userId) {
        List<ConsumptionOrder> orders = orderService.getOrdersByUserId(userId);
        return ApiResponse.success(orders);
    }

    @PostMapping("/user/{userId}")
    public ApiResponse<ConsumptionOrder> createOrder(@PathVariable Long userId, @RequestBody ConsumeRequest request) {
        Gift gift = giftService.getGiftById(request.getGiftId());
        ConsumptionOrder order = orderService.createOrder(userId, gift.getName(), gift.getPrice(), null);
        return ApiResponse.success(order);
    }

    @PutMapping("/{id}/deliver")
    public ApiResponse<Void> confirmDelivery(@PathVariable Long id) {
        orderService.confirmDelivery(id);
        return ApiResponse.success();
    }

    @PostMapping("/{id}/refund")
    public ApiResponse<Void> refundOrder(@PathVariable Long id) {
        orderService.refundOrder(id);
        return ApiResponse.success();
    }
}