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

import com.smartvip.entity.ConsumptionOrder;
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
public class OrderService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private MerchantConfigService merchantConfigService;

    private static final RowMapper<ConsumptionOrder> ORDER_MAPPER = (rs, rowNum) -> {
        ConsumptionOrder order = new ConsumptionOrder();
        order.setId(rs.getLong("id"));
        order.setUserId(rs.getLong("user_id"));
        order.setProductName(rs.getString("product_name"));
        order.setAmount(rs.getBigDecimal("amount"));
        order.setReferrerId(rs.getObject("referrer_id") != null ? rs.getLong("referrer_id") : null);
        order.setRebateAmount(rs.getBigDecimal("rebate_amount"));
        order.setRebateStatus(rs.getInt("rebate_status"));
        order.setDeliverStatus(rs.getInt("deliver_status"));
        order.setConfirmTime(rs.getTimestamp("confirm_time") != null ? rs.getTimestamp("confirm_time").toLocalDateTime() : null);
        order.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
        return order;
    };

    public List<ConsumptionOrder> getOrdersByUserId(Long userId) {
        return jdbcTemplate.query("SELECT * FROM consumption_orders WHERE user_id = ?", ORDER_MAPPER, userId);
    }

    public List<ConsumptionOrder> getAllOrders() {
        return jdbcTemplate.query("SELECT * FROM consumption_orders ORDER BY create_time DESC", ORDER_MAPPER);
    }

    public int getTotalConsumptionOrders() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM consumption_orders", Integer.class);
        return count != null ? count : 0;
    }

    @Transactional
    public ConsumptionOrder createOrder(Long userId, String productName, BigDecimal amount, Long referrerId) {
        BigDecimal rebateAmount = null;
        if (referrerId != null && merchantConfigService.getRebateRate() != null) {
            rebateAmount = amount.multiply(merchantConfigService.getRebateRate());
        }
        jdbcTemplate.update("INSERT INTO consumption_orders (user_id, product_name, amount, referrer_id, rebate_amount, rebate_status, deliver_status) VALUES (?, ?, ?, ?, ?, ?, ?)",
            userId, productName, amount, referrerId, rebateAmount, 0, 0);
        List<ConsumptionOrder> orders = jdbcTemplate.query("SELECT * FROM consumption_orders WHERE user_id = ? AND product_name = ? ORDER BY id DESC LIMIT 1", ORDER_MAPPER, userId, productName);
        return orders.isEmpty() ? null : orders.get(0);
    }

    @Transactional
    public void confirmDelivery(Long orderId) {
        jdbcTemplate.update("UPDATE consumption_orders SET deliver_status = 2, confirm_time = ? WHERE id = ?", LocalDateTime.now(), orderId);
    }

    @Transactional
    public void refundOrder(Long orderId) {
        List<ConsumptionOrder> orders = jdbcTemplate.query("SELECT * FROM consumption_orders WHERE id = ?", ORDER_MAPPER, orderId);
        if (orders.isEmpty()) return;
        ConsumptionOrder order = orders.get(0);
        userService.addRechargeBalance(order.getUserId(), order.getAmount());
        if (order.getReferrerId() != null && order.getRebateAmount() != null) {
            userService.addRebateBalance(order.getReferrerId(), order.getRebateAmount().negate());
        }
        jdbcTemplate.update("UPDATE consumption_orders SET rebate_status = 2 WHERE id = ?", orderId);
    }

    public List<ConsumptionOrder> getPendingRebateOrders() {
        return jdbcTemplate.query("SELECT * FROM consumption_orders WHERE rebate_status = 0 AND referrer_id IS NOT NULL", ORDER_MAPPER);
    }
}
