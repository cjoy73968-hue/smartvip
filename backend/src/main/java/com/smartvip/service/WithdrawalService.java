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

import com.smartvip.entity.User;
import com.smartvip.entity.WithdrawalLog;
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
public class WithdrawalService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService userService;

    private static final RowMapper<WithdrawalLog> WITHDRAWAL_MAPPER = (rs, rowNum) -> {
        WithdrawalLog log = new WithdrawalLog();
        log.setId(rs.getLong("id"));
        log.setUserId(rs.getLong("user_id"));
        log.setAmount(rs.getBigDecimal("amount"));
        log.setStatus(rs.getInt("status"));
        log.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
        log.setConfirmTime(rs.getTimestamp("confirm_time") != null ? rs.getTimestamp("confirm_time").toLocalDateTime() : null);
        return log;
    };

    public List<WithdrawalLog> getWithdrawalsByUserId(Long userId) {
        return jdbcTemplate.query("SELECT * FROM withdrawal_logs WHERE user_id = ?", WITHDRAWAL_MAPPER, userId);
    }

    public List<WithdrawalLog> getPendingWithdrawals() {
        return jdbcTemplate.query("SELECT * FROM withdrawal_logs WHERE status = 0", WITHDRAWAL_MAPPER);
    }

    @Transactional
    public WithdrawalLog createWithdrawal(Long userId, BigDecimal amount) {
        User user = userService.getUserById(userId);
        if (user.getRechargeBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient recharge balance");
        }
        jdbcTemplate.update("INSERT INTO withdrawal_logs (user_id, amount, status) VALUES (?, ?, ?)",
            userId, amount, 0);
        userService.deductBalance(userId, amount);
        List<WithdrawalLog> logs = jdbcTemplate.query("SELECT * FROM withdrawal_logs WHERE user_id = ? ORDER BY id DESC LIMIT 1", WITHDRAWAL_MAPPER, userId);
        return logs.isEmpty() ? null : logs.get(0);
    }

    @Transactional
    public void confirmWithdrawal(Long withdrawalId) {
        jdbcTemplate.update("UPDATE withdrawal_logs SET status = 1 WHERE id = ?", withdrawalId);
    }

    @Transactional
    public void completeWithdrawal(Long withdrawalId) {
        jdbcTemplate.update("UPDATE withdrawal_logs SET status = 2, confirm_time = ? WHERE id = ?", LocalDateTime.now(), withdrawalId);
    }
}
