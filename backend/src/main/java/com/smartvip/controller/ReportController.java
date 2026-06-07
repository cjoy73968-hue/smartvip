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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

@RestController
@RequestMapping("/api/merchant/reports")
public class ReportController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/membership")
    public ApiResponse<Map<String, Object>> getMembershipReport(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        int totalMembers = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
        int activeMembers = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users WHERE status = 1 AND membership_expire > NOW()", Integer.class);
        int expiredMembers = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users WHERE membership_expire < NOW() AND status = 1", Integer.class);
        LocalDate firstDayOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        int newMembersThisMonth = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users WHERE create_time >= ?", Integer.class, firstDayOfMonth.atStartOfDay());
        return ApiResponse.success(Map.of(
            "totalMembers", totalMembers,
            "activeMembers", activeMembers,
            "expiredMembers", expiredMembers,
            "newMembersThisMonth", newMembersThisMonth
        ));
    }

    @GetMapping("/transaction")
    public ApiResponse<Map<String, Object>> getTransactionReport(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        BigDecimal totalRecharge = jdbcTemplate.queryForObject("SELECT COALESCE(SUM(amount), 0) FROM recharge_logs WHERE status = 1", BigDecimal.class);
        BigDecimal totalConsumption = jdbcTemplate.queryForObject("SELECT COALESCE(SUM(amount), 0) FROM consumption_orders", BigDecimal.class);
        BigDecimal totalRebate = jdbcTemplate.queryForObject("SELECT COALESCE(SUM(rebate_amount), 0) FROM consumption_orders WHERE rebate_amount IS NOT NULL", BigDecimal.class);
        int rechargeCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM recharge_logs WHERE status = 1", Integer.class);
        int consumptionCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM consumption_orders", Integer.class);
        return ApiResponse.success(Map.of(
            "totalRechargeAmount", totalRecharge != null ? totalRecharge : BigDecimal.ZERO,
            "totalConsumptionAmount", totalConsumption != null ? totalConsumption : BigDecimal.ZERO,
            "totalRebatePaid", totalRebate != null ? totalRebate : BigDecimal.ZERO,
            "rechargeCount", rechargeCount,
            "consumptionCount", consumptionCount
        ));
    }

    @GetMapping("/delivery")
    public ApiResponse<Map<String, Object>> getDeliveryReport(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        int totalDeliveries = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM gift_delivery_orders", Integer.class);
        int periodicDeliveries = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM gift_delivery_orders WHERE order_type = 1", Integer.class);
        int oneTimeDeliveries = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM gift_delivery_orders WHERE order_type = 2", Integer.class);
        BigDecimal totalPriceDiff = jdbcTemplate.queryForObject("SELECT COALESCE(SUM(price_diff), 0) FROM gift_delivery_orders", BigDecimal.class);
        return ApiResponse.success(Map.of(
            "totalDeliveries", totalDeliveries,
            "periodicDeliveries", periodicDeliveries,
            "oneTimeDeliveries", oneTimeDeliveries,
            "totalPriceDiffCollected", totalPriceDiff != null ? totalPriceDiff : BigDecimal.ZERO
        ));
    }

    @GetMapping("/summary")
    public ApiResponse<Map<String, Object>> getSummary() {
        int totalMembers = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
        int activeMembers = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users WHERE status = 1 AND membership_expire > NOW()", Integer.class);
        int pendingDeliveries = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM gift_delivery_orders WHERE status = 0", Integer.class);
        BigDecimal totalRecharge = jdbcTemplate.queryForObject("SELECT COALESCE(SUM(amount), 0) FROM recharge_logs WHERE status = 1", BigDecimal.class);
        return ApiResponse.success(Map.of(
            "totalMembers", totalMembers,
            "activeMembers", activeMembers,
            "pendingDeliveries", pendingDeliveries,
            "totalRechargeAmount", totalRecharge != null ? totalRecharge : BigDecimal.ZERO
        ));
    }

    @GetMapping("/export")
    public ApiResponse<Map<String, Object>> exportReport(@RequestParam String type) {
        return ApiResponse.success(Map.of("url", "/api/merchant/reports/export/" + type));
    }
}
