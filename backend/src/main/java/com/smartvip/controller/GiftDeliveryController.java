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
import com.smartvip.entity.GiftDeliveryOrder;
import com.smartvip.service.GiftDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
public class GiftDeliveryController {

    @Autowired
    private GiftDeliveryService giftDeliveryService;

    @GetMapping("/user/{userId}")
    public ApiResponse<List<GiftDeliveryOrder>> getOrders(@PathVariable Long userId) {
        List<GiftDeliveryOrder> orders = giftDeliveryService.getOrdersByUserId(userId);
        return ApiResponse.success(orders);
    }

    @GetMapping("/pending")
    public ApiResponse<List<GiftDeliveryOrder>> getPendingOrders() {
        List<GiftDeliveryOrder> orders = giftDeliveryService.getPendingOrders();
        return ApiResponse.success(orders);
    }

    @PostMapping("/periodic/user/{userId}")
    public ApiResponse<GiftDeliveryOrder> createPeriodicDelivery(
            @PathVariable Long userId,
            @RequestParam Long giftId,
            @RequestParam Long addressId) {
        GiftDeliveryOrder order = giftDeliveryService.createPeriodicDelivery(userId, giftId, addressId);
        return ApiResponse.success(order);
    }

    @PostMapping("/one-time/user/{userId}")
    public ApiResponse<GiftDeliveryOrder> createOneTimeDelivery(
            @PathVariable Long userId,
            @RequestParam Long giftId,
            @RequestParam Long addressId) {
        GiftDeliveryOrder order = giftDeliveryService.createOneTimeDelivery(userId, giftId, addressId);
        return ApiResponse.success(order);
    }

    @PutMapping("/{id}/deliver")
    public ApiResponse<Void> confirmDelivery(@PathVariable Long id) {
        giftDeliveryService.confirmDelivery(id);
        return ApiResponse.success();
    }
}