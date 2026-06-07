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
import com.smartvip.entity.MerchantConfig;
import com.smartvip.service.MerchantConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchant/config")
public class MerchantConfigController {

    @Autowired
    private MerchantConfigService merchantConfigService;

    @GetMapping
    public ApiResponse<MerchantConfig> getConfig() {
        MerchantConfig config = merchantConfigService.getCurrentConfig();
        return ApiResponse.success(config);
    }

    @PutMapping
    public ApiResponse<Void> updateConfig(@RequestBody MerchantConfig config) {
        merchantConfigService.updateConfig(config);
        return ApiResponse.success();
    }

    @PutMapping("/rebate-rate")
    public ApiResponse<Void> updateRebateRate(@RequestParam String rate) {
        merchantConfigService.updateRebateRate(new java.math.BigDecimal(rate));
        return ApiResponse.success();
    }

    @PutMapping("/membership-price")
    public ApiResponse<Void> updateMembershipPrice(@RequestParam String price) {
        merchantConfigService.updateMembershipPrice(new java.math.BigDecimal(price));
        return ApiResponse.success();
    }
}