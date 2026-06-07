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
import com.smartvip.dto.RechargeRequest;
import com.smartvip.entity.RechargeLog;
import com.smartvip.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recharge")
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;

    @GetMapping("/user/{userId}")
    public ApiResponse<List<RechargeLog>> getRechargeLogs(@PathVariable Long userId) {
        List<RechargeLog> logs = rechargeService.getRechargeLogsByUserId(userId);
        return ApiResponse.success(logs);
    }

    @PostMapping("/user/{userId}")
    public ApiResponse<RechargeLog> createRecharge(@PathVariable Long userId, @RequestBody RechargeRequest request) {
        // In real implementation, this would integrate with WeChat Pay
        // For now, simulate immediate success
        RechargeLog log = rechargeService.createRechargeLog(userId, request.getAmount(), "WX" + System.currentTimeMillis());
        return ApiResponse.success(log);
    }

    @PostMapping("/callback")
    public ApiResponse<Void> paymentCallback(@RequestBody Object paymentResult) {
        // WeChat Pay callback handler - would parse the XML/JSON notification
        // and call rechargeService.createRechargeLog with the actual transaction ID
        return ApiResponse.success();
    }
}