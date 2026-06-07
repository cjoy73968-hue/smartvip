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
import com.smartvip.entity.WithdrawalLog;
import com.smartvip.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/withdrawal")
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @GetMapping("/pending")
    public ApiResponse<List<WithdrawalLog>> getPendingWithdrawals() {
        List<WithdrawalLog> withdrawals = withdrawalService.getPendingWithdrawals();
        return ApiResponse.success(withdrawals);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<WithdrawalLog>> getUserWithdrawals(@PathVariable Long userId) {
        List<WithdrawalLog> withdrawals = withdrawalService.getWithdrawalsByUserId(userId);
        return ApiResponse.success(withdrawals);
    }

    @PutMapping("/{id}/confirm")
    public ApiResponse<Void> confirmWithdrawal(@PathVariable Long id) {
        withdrawalService.confirmWithdrawal(id);
        return ApiResponse.success();
    }

    @PutMapping("/{id}/complete")
    public ApiResponse<Void> completeWithdrawal(@PathVariable Long id) {
        withdrawalService.completeWithdrawal(id);
        return ApiResponse.success();
    }
}