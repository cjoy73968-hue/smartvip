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

import com.smartvip.entity.RechargeLog;
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
public class RechargeService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService userService;

    private static final RowMapper<RechargeLog> RECHARGE_MAPPER = (rs, rowNum) -> {
        RechargeLog log = new RechargeLog();
        log.setId(rs.getLong("id"));
        log.setUserId(rs.getLong("user_id"));
        log.setAmount(rs.getBigDecimal("amount"));
        log.setWxTransactionId(rs.getString("wx_transaction_id"));
        log.setStatus(rs.getInt("status"));
        log.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
        return log;
    };

    public List<RechargeLog> getRechargeLogsByUserId(Long userId) {
        return jdbcTemplate.query("SELECT * FROM recharge_logs WHERE user_id = ?", RECHARGE_MAPPER, userId);
    }

    @Transactional
    public RechargeLog createRechargeLog(Long userId, BigDecimal amount, String wxTransactionId) {
        List<RechargeLog> existing = jdbcTemplate.query("SELECT * FROM recharge_logs WHERE wx_transaction_id = ?", RECHARGE_MAPPER, wxTransactionId);
        if (!existing.isEmpty()) {
            return existing.get(0);
        }
        jdbcTemplate.update("INSERT INTO recharge_logs (user_id, amount, wx_transaction_id, status) VALUES (?, ?, ?, ?)",
            userId, amount, wxTransactionId, 1);
        List<RechargeLog> logs = jdbcTemplate.query("SELECT * FROM recharge_logs WHERE wx_transaction_id = ?", RECHARGE_MAPPER, wxTransactionId);
        if (!logs.isEmpty()) {
            userService.addRechargeBalance(userId, amount);
            return logs.get(0);
        }
        return null;
    }
}
