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
import com.smartvip.entity.ConsumptionOrder;
import com.smartvip.entity.GiftDeliveryOrder;
import com.smartvip.service.GiftDeliveryService;
import com.smartvip.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/merchant/orders")
public class MerchantOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GiftDeliveryService giftDeliveryService;

    @GetMapping("/consumption/list")
    public ApiResponse<List<ConsumptionOrder>> getConsumptionOrders(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        return ApiResponse.success(orderService.getAllOrders());
    }

    @GetMapping("/delivery/list")
    public ApiResponse<List<GiftDeliveryOrder>> getDeliveryOrders(
            @RequestParam(required = false) Integer orderType,
            @RequestParam(required = false) Integer status) {
        if (status != null && status == 0) {
            return ApiResponse.success(giftDeliveryService.getPendingOrders());
        }
        return ApiResponse.success(giftDeliveryService.getAllOrders());
    }

    @PutMapping("/delivery/{id}/deliver")
    public ApiResponse<Void> confirmDelivery(@PathVariable Long id) {
        giftDeliveryService.confirmDelivery(id);
        return ApiResponse.success();
    }

    @GetMapping("/statistics")
    public ApiResponse<Map<String, Object>> getOrderStatistics() {
        return ApiResponse.success(Map.of(
            "totalConsumptionOrders", orderService.getTotalConsumptionOrders(),
            "totalDeliveryOrders", giftDeliveryService.getTotalDeliveryOrders(),
            "pendingDelivery", giftDeliveryService.getPendingDeliveryCount()
        ));
    }
}
