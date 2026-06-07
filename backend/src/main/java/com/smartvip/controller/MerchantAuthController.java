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
import com.smartvip.entity.MerchantUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/api/merchant/auth")
public class MerchantAuthController {

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

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestParam String username, @RequestParam String password) {
        MerchantUser user = jdbcTemplate.queryForObject("SELECT * FROM merchant_users WHERE username = ?", USER_MAPPER, username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ApiResponse.error(401, "Invalid username or password");
        }
        if (user.getStatus() != 1) {
            return ApiResponse.error(401, "Account is disabled");
        }
        return ApiResponse.success(Map.of(
            "id", user.getId(),
            "username", user.getUsername(),
            "nickname", user.getNickname(),
            "role", user.getRole(),
            "permissions", user.getPermissions()
        ));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        return ApiResponse.success();
    }
}
