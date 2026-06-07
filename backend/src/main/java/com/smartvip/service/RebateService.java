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
package com.smartvip.service;

import com.smartvip.entity.RebateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RebateService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService userService;

    private static final RowMapper<RebateLog> REBATE_MAPPER = (rs, rowNum) -> {
        RebateLog log = new RebateLog();
        log.setId(rs.getLong("id"));
        log.setReferrerId(rs.getLong("referrer_id"));
        log.setOrderId(rs.getLong("order_id"));
        log.setOrderType(rs.getInt("order_type"));
        log.setAmount(rs.getBigDecimal("amount"));
        log.setStatus(rs.getInt("status"));
        log.setArriveTime(rs.getTimestamp("arrive_time") != null ? rs.getTimestamp("arrive_time").toLocalDateTime() : null);
        log.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
        return log;
    };

    public List<RebateLog> getRebateLogsByReferrerId(Long referrerId) {
        return jdbcTemplate.query("SELECT * FROM rebate_logs WHERE referrer_id = ?", REBATE_MAPPER, referrerId);
    }

    public BigDecimal getTotalPendingRebate(Long referrerId) {
        BigDecimal total = jdbcTemplate.queryForObject("SELECT SUM(amount) FROM rebate_logs WHERE referrer_id = ? AND status = 0", BigDecimal.class, referrerId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional
    public void createRebateLog(Long referrerId, Long orderId, Integer orderType, BigDecimal amount) {
        List<RebateLog> existing = jdbcTemplate.query("SELECT * FROM rebate_logs WHERE order_id = ? AND order_type = ?", REBATE_MAPPER, orderId, orderType);
        if (!existing.isEmpty()) {
            return;
        }
        jdbcTemplate.update("INSERT INTO rebate_logs (referrer_id, order_id, order_type, amount, status) VALUES (?, ?, ?, ?, ?)",
            referrerId, orderId, orderType, amount, 0);
    }

    @Transactional
    public void creditRebate(Long rebateLogId) {
        List<RebateLog> logs = jdbcTemplate.query("SELECT * FROM rebate_logs WHERE id = ?", REBATE_MAPPER, rebateLogId);
        if (logs.isEmpty()) return;
        RebateLog log = logs.get(0);
        if (log.getStatus() == 0) {
            userService.addRebateBalance(log.getReferrerId(), log.getAmount());
            jdbcTemplate.update("UPDATE rebate_logs SET status = 1, arrive_time = ? WHERE id = ?", LocalDateTime.now(), rebateLogId);
        }
    }

    @Transactional
    public void clawbackRebate(Long orderId, Integer orderType) {
        List<RebateLog> logs = jdbcTemplate.query("SELECT * FROM rebate_logs WHERE order_id = ? AND order_type = ?", REBATE_MAPPER, orderId, orderType);
        for (RebateLog log : logs) {
            if (log.getStatus() == 1) {
                userService.addRebateBalance(log.getReferrerId(), log.getAmount().negate());
            }
            jdbcTemplate.update("UPDATE rebate_logs SET status = 2 WHERE id = ?", log.getId());
        }
    }

    public List<RebateLog> getPendingRebateLogs() {
        return jdbcTemplate.query("SELECT * FROM rebate_logs WHERE status = 0", REBATE_MAPPER);
    }

    @Transactional
    public void creditRebateByOrderId(Long orderId, Integer orderType) {
        List<RebateLog> logs = jdbcTemplate.query("SELECT * FROM rebate_logs WHERE order_id = ? AND order_type = ?", REBATE_MAPPER, orderId, orderType);
        for (RebateLog log : logs) {
            if (log.getStatus() == 0) {
                userService.addRebateBalance(log.getReferrerId(), log.getAmount());
                jdbcTemplate.update("UPDATE rebate_logs SET status = 1, arrive_time = ? WHERE id = ?", LocalDateTime.now(), log.getId());
            }
        }
    }
}
