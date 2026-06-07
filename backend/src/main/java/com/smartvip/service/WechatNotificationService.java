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

import com.smartvip.entity.UserSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WechatNotificationService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<UserSubscription> SUB_MAPPER = (rs, rowNum) -> {
        UserSubscription sub = new UserSubscription();
        sub.setId(rs.getLong("id"));
        sub.setUserId(rs.getLong("user_id"));
        sub.setTemplateType(rs.getString("template_type"));
        sub.setSubscribed(rs.getInt("subscribed"));
        sub.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
        sub.setUpdateTime(rs.getTimestamp("update_time").toLocalDateTime());
        return sub;
    };

    @Transactional
    public void subscribe(Long userId, String templateType) {
        List<UserSubscription> subs = jdbcTemplate.query("SELECT * FROM user_subscriptions WHERE user_id = ? AND template_type = ?", SUB_MAPPER, userId, templateType);
        if (subs.isEmpty()) {
            jdbcTemplate.update("INSERT INTO user_subscriptions (user_id, template_type, subscribed) VALUES (?, ?, ?)",
                userId, templateType, 1);
        } else {
            jdbcTemplate.update("UPDATE user_subscriptions SET subscribed = 1 WHERE user_id = ? AND template_type = ?", userId, templateType);
        }
    }

    @Transactional
    public void unsubscribe(Long userId, String templateType) {
        jdbcTemplate.update("UPDATE user_subscriptions SET subscribed = 0 WHERE user_id = ? AND template_type = ?", userId, templateType);
    }

    public boolean isSubscribed(Long userId, String templateType) {
        List<UserSubscription> subs = jdbcTemplate.query("SELECT * FROM user_subscriptions WHERE user_id = ? AND template_type = ?", SUB_MAPPER, userId, templateType);
        return !subs.isEmpty() && subs.get(0).getSubscribed() == 1;
    }

    public List<UserSubscription> getSubscribedUsers(String templateType) {
        return jdbcTemplate.query("SELECT * FROM user_subscriptions WHERE template_type = ? AND subscribed = 1", SUB_MAPPER, templateType);
    }

    public void sendTemplateMessage(Long userId, String templateType, String[] params) {
        // WeChat template message sending logic would go here
        // This is a placeholder for the actual WeChat API integration
    }

    public void sendDeliveryOrderNotification(Long userId, String giftName, String address) {
        sendTemplateMessage(userId, "delivery_order", new String[]{giftName, address});
    }

    public void sendBalanceInsufficientNotification(Long userId, String defaultGiftName) {
        sendTemplateMessage(userId, "balance_insufficient", new String[]{defaultGiftName});
    }

    public void sendRebateCreditNotification(Long userId, String amount) {
        sendTemplateMessage(userId, "rebate_credit", new String[]{amount});
    }

    public void sendMembershipExpiryReminder(Long userId, String expiryDate, String balance) {
        sendTemplateMessage(userId, "membership_expiry", new String[]{expiryDate, balance});
    }

    public void sendGiftPriceChangeNotification(Long userId, String giftName, String oldPrice, String newPrice) {
        sendTemplateMessage(userId, "gift_price_change", new String[]{giftName, oldPrice, newPrice});
    }
}
