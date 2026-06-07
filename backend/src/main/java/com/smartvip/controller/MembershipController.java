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
import com.smartvip.entity.User;
import com.smartvip.service.UserService;
import com.smartvip.service.MerchantConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/membership")
public class MembershipController {

    @Autowired
    private UserService userService;

    @Autowired
    private MerchantConfigService merchantConfigService;

    @PostMapping("/user/{userId}/purchase")
    public ApiResponse<Map<String, Object>> purchaseMembership(@PathVariable Long userId) {
        var price = merchantConfigService.getMembershipPrice();
        userService.purchaseMembership(userId, price);
        User user = userService.getUserById(userId);
        return ApiResponse.success(Map.of(
            "membershipStart", user.getMembershipStart(),
            "membershipExpire", user.getMembershipExpire(),
            "price", price
        ));
    }

    @PutMapping("/user/{userId}/auto-subscribe")
    public ApiResponse<Void> toggleAutoSubscribe(@PathVariable Long userId, @RequestParam Boolean enable) {
        return ApiResponse.success();
    }

    @GetMapping("/user/{userId}/status")
    public ApiResponse<Map<String, Object>> getMembershipStatus(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        boolean isActive = userService.isMembershipActive(userId);
        return ApiResponse.success(Map.of(
            "isActive", isActive,
            "membershipStart", user != null ? user.getMembershipStart() : null,
            "membershipExpire", user != null ? user.getMembershipExpire() : null,
            "autoSubscribe", user != null && user.getAutoSubscribe() == 1
        ));
    }
}
