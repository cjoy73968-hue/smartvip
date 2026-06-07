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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/merchant/members")
public class MerchantMemberController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ApiResponse<List<User>> getMemberList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        List<User> members = userService.getActiveMembers();
        return ApiResponse.success(members);
    }

    @GetMapping("/{id}")
    public ApiResponse<User> getMemberDetail(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ApiResponse.success(user);
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> updateMember(@PathVariable Long id, @RequestBody User request) {
        return ApiResponse.success();
    }

    @PutMapping("/{id}/membership-expire")
    public ApiResponse<Void> updateMembershipExpire(@PathVariable Long id, @RequestParam String expireDate) {
        return ApiResponse.success();
    }

    @PostMapping("/{id}/balance/add")
    public ApiResponse<Void> addBalance(@PathVariable Long id, @RequestParam BigDecimal amount) {
        userService.addRechargeBalance(id, amount);
        return ApiResponse.success();
    }

    @PostMapping("/{id}/balance/deduct")
    public ApiResponse<Void> deductBalance(@PathVariable Long id, @RequestParam BigDecimal amount) {
        userService.deductBalance(id, amount);
        return ApiResponse.success();
    }

    @PutMapping("/{id}/freeze")
    public ApiResponse<Void> freezeMember(@PathVariable Long id) {
        return ApiResponse.success();
    }

    @PutMapping("/{id}/unfreeze")
    public ApiResponse<Void> unfreezeMember(@PathVariable Long id) {
        return ApiResponse.success();
    }

    @GetMapping("/{id}/referrals")
    public ApiResponse<Map<String, Object>> getReferralStats(@PathVariable Long id) {
        return ApiResponse.success(Map.of("referralCount", 0, "referrals", List.of()));
    }
}
