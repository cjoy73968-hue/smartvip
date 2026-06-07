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
import com.smartvip.dto.LoginRequest;
import com.smartvip.dto.LoginResponse;
import com.smartvip.entity.User;
import com.smartvip.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        // WeChat login logic - simplified for now
        String openid = request.getCode();
        User user = userService.findByOpenid(openid);

        LoginResponse response = new LoginResponse();
        if (user == null) {
            user = userService.createUser(openid, "User", null, null);
            response.setIsNewUser(true);
        } else {
            response.setIsNewUser(false);
        }

        response.setOpenid(user.getOpenid());
        response.setNickname(user.getNickname());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setReferralCode(user.getReferralCode());
        response.setMembershipStatus(userService.isMembershipActive(user.getId()) ? 1 : 0);

        return ApiResponse.success(response);
    }

    @GetMapping("/{id}")
    public ApiResponse<User> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ApiResponse.success(user);
    }

    @PostMapping("/{id}/phone")
    public ApiResponse<Void> bindPhone(@PathVariable Long id, @RequestParam String phone) {
        userService.bindPhone(id, phone);
        return ApiResponse.success();
    }

    @GetMapping("/{id}/balance")
    public ApiResponse<Object> getBalance(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ApiResponse.success(java.util.Map.of(
            "rechargeBalance", user.getRechargeBalance(),
            "rebateBalance", user.getRebateBalance(),
            "totalBalance", user.getRechargeBalance().add(user.getRebateBalance())
        ));
    }
}