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
import com.smartvip.entity.RebateLog;
import com.smartvip.service.RebateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rebate")
public class RebateController {

    @Autowired
    private RebateService rebateService;

    @GetMapping("/referrer/{referrerId}")
    public ApiResponse<Map<String, Object>> getReferralStats(@PathVariable Long referrerId) {
        List<RebateLog> logs = rebateService.getRebateLogsByReferrerId(referrerId);
        BigDecimal pending = rebateService.getTotalPendingRebate(referrerId);
        return ApiResponse.success(Map.of(
            "logs", logs,
            "pendingAmount", pending,
            "totalCount", logs.size()
        ));
    }

    @GetMapping("/referrer/{referrerId}/pending")
    public ApiResponse<BigDecimal> getPendingRebate(@PathVariable Long referrerId) {
        BigDecimal pending = rebateService.getTotalPendingRebate(referrerId);
        return ApiResponse.success(pending);
    }
}