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

import com.smartvip.entity.MerchantUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class MerchantUserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final RowMapper<MerchantUser> USER_MAPPER = (rs, rowNum) -> {
        MerchantUser user = new MerchantUser();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setNickname(rs.getString("nickname"));
        user.setRole(rs.getString("role"));
        user.setPermissions(rs.getString("permissions"));
        user.setStatus(rs.getInt("status"));
        return user;
    };

    public MerchantUser findByUsername(String username) {
        List<MerchantUser> users = jdbcTemplate.query("SELECT * FROM merchant_users WHERE username = ?", USER_MAPPER, username);
        return users.isEmpty() ? null : users.get(0);
    }

    public MerchantUser getById(Long id) {
        List<MerchantUser> users = jdbcTemplate.query("SELECT * FROM merchant_users WHERE id = ?", USER_MAPPER, id);
        if (!users.isEmpty()) {
            users.get(0).setPassword(null);
        }
        return users.isEmpty() ? null : users.get(0);
    }

    public List<MerchantUser> getAll() {
        return jdbcTemplate.query("SELECT * FROM merchant_users", USER_MAPPER);
    }

    @Transactional
    public MerchantUser createSubAccount(String username, String password, String nickname, String permissions) {
        jdbcTemplate.update("INSERT INTO merchant_users (username, password, nickname, role, permissions, status) VALUES (?, ?, ?, ?, ?, ?)",
            username, passwordEncoder.encode(password), nickname, "subaccount", permissions, 1);
        return findByUsername(username);
    }

    @Transactional
    public void updatePermissions(Long userId, String permissions) {
        jdbcTemplate.update("UPDATE merchant_users SET permissions = ? WHERE id = ?", permissions, userId);
    }

    @Transactional
    public void disableAccount(Long userId) {
        jdbcTemplate.update("UPDATE merchant_users SET status = 0 WHERE id = ?", userId);
    }

    @Transactional
    public void enableAccount(Long userId) {
        jdbcTemplate.update("UPDATE merchant_users SET status = 1 WHERE id = ?", userId);
    }

    public boolean hasPermission(Long userId, String permission) {
        MerchantUser user = getById(userId);
        if (user == null || user.getPermissions() == null) {
            return false;
        }
        return user.getPermissions().contains(permission) || user.getPermissions().contains("*");
    }
}
