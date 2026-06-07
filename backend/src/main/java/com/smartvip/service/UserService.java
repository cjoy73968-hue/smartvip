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
import com.smartvip.util.ReferralCodeUtil;
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
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<User> USER_MAPPER = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setOpenid(rs.getString("openid"));
        user.setNickname(rs.getString("nickname"));
        user.setAvatarUrl(rs.getString("avatar_url"));
        user.setPhone(rs.getString("phone"));
        user.setReferralCode(rs.getString("referral_code"));
        user.setReferrerId(rs.getObject("referrer_id") != null ? rs.getLong("referrer_id") : null);
        user.setRechargeBalance(rs.getBigDecimal("recharge_balance"));
        user.setRebateBalance(rs.getBigDecimal("rebate_balance"));
        user.setMembershipStart(rs.getTimestamp("membership_start") != null ? rs.getTimestamp("membership_start").toLocalDateTime() : null);
        user.setMembershipExpire(rs.getTimestamp("membership_expire") != null ? rs.getTimestamp("membership_expire").toLocalDateTime() : null);
        user.setAutoSubscribe(rs.getInt("auto_subscribe"));
        user.setStatus(rs.getInt("status"));
        user.setVersion(rs.getInt("version"));
        user.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
        user.setUpdateTime(rs.getTimestamp("update_time").toLocalDateTime());
        return user;
    };

    public User findByOpenid(String openid) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE openid = ?", USER_MAPPER, openid);
    }

    public User findByReferralCode(String referralCode) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE referral_code = ?", USER_MAPPER, referralCode);
    }

    public User findByPhone(String phone) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE phone = ?", USER_MAPPER, phone);
    }

    @Transactional
    public User createUser(String openid, String nickname, String avatarUrl, Long referrerId) {
        String referralCode = ReferralCodeUtil.generate();
        jdbcTemplate.update("INSERT INTO users (openid, nickname, avatar_url, referral_code, referrer_id, recharge_balance, rebate_balance, auto_subscribe, status, membership_start) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            openid, nickname, avatarUrl, referralCode, referrerId, BigDecimal.ZERO, BigDecimal.ZERO, 0, 1, LocalDateTime.now());
        return findByOpenid(openid);
    }

    @Transactional
    public void bindPhone(Long userId, String phone) {
        jdbcTemplate.update("UPDATE users SET phone = ? WHERE id = ?", phone, userId);
    }

    @Transactional
    public boolean deductBalance(Long userId, BigDecimal amount) {
        User user = getUserById(userId);
        if (user == null) return false;
        BigDecimal total = user.getRechargeBalance().add(user.getRebateBalance());
        if (total.compareTo(amount) < 0) {
            return false;
        }
        if (user.getRechargeBalance().compareTo(amount) >= 0) {
            jdbcTemplate.update("UPDATE users SET recharge_balance = recharge_balance - ? WHERE id = ?", amount, userId);
        } else {
            BigDecimal remaining = amount.subtract(user.getRechargeBalance());
            jdbcTemplate.update("UPDATE users SET recharge_balance = 0, rebate_balance = rebate_balance - ? WHERE id = ?", remaining, userId);
        }
        return true;
    }

    @Transactional
    public void addRechargeBalance(Long userId, BigDecimal amount) {
        jdbcTemplate.update("UPDATE users SET recharge_balance = recharge_balance + ? WHERE id = ?", amount, userId);
    }

    @Transactional
    public void addRebateBalance(Long userId, BigDecimal amount) {
        jdbcTemplate.update("UPDATE users SET rebate_balance = rebate_balance + ? WHERE id = ?", amount, userId);
    }

    @Transactional
    public void purchaseMembership(Long userId, BigDecimal price) {
        User user = getUserById(userId);
        if (user == null) return;
        deductBalance(userId, price);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime newExpire;
        if (user.getMembershipExpire() != null && user.getMembershipExpire().isAfter(now)) {
            newExpire = user.getMembershipExpire().plusDays(365);
        } else {
            jdbcTemplate.update("UPDATE users SET membership_start = ? WHERE id = ?", now, userId);
            newExpire = now.plusDays(365);
        }
        jdbcTemplate.update("UPDATE users SET membership_expire = ? WHERE id = ?", newExpire, userId);
    }

    public boolean isMembershipActive(Long userId) {
        User user = getUserById(userId);
        if (user == null || user.getMembershipExpire() == null) {
            return false;
        }
        return user.getMembershipExpire().isAfter(LocalDateTime.now());
    }

    @Transactional
    public void updateMembershipStatus() {
        jdbcTemplate.update("UPDATE users SET status = 0 WHERE membership_expire < ? AND status > 0", LocalDateTime.now());
    }

    public User getUserById(Long userId) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", USER_MAPPER, userId);
    }

    public List<User> getActiveMembers() {
        return jdbcTemplate.query("SELECT * FROM users WHERE status = 1 AND membership_expire IS NOT NULL AND membership_expire > ?", USER_MAPPER, LocalDateTime.now());
    }
}
