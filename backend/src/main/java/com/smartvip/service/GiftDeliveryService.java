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

import com.smartvip.entity.GiftDeliveryOrder;
import com.smartvip.entity.Gift;
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
public class GiftDeliveryService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GiftService giftService;

    @Autowired
    private MerchantConfigService merchantConfigService;

    @Autowired
    private UserService userService;

    private static final RowMapper<GiftDeliveryOrder> DELIVERY_MAPPER = (rs, rowNum) -> {
        GiftDeliveryOrder order = new GiftDeliveryOrder();
        order.setId(rs.getLong("id"));
        order.setUserId(rs.getLong("user_id"));
        order.setGiftId(rs.getLong("gift_id"));
        order.setAddressId(rs.getLong("address_id"));
        order.setPriceDiff(rs.getBigDecimal("price_diff"));
        order.setOrderType(rs.getInt("order_type"));
        order.setStatus(rs.getInt("status"));
        order.setDeliverTime(rs.getTimestamp("deliver_time") != null ? rs.getTimestamp("deliver_time").toLocalDateTime() : null);
        order.setRebateStatus(rs.getInt("rebate_status"));
        order.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
        return order;
    };

    public List<GiftDeliveryOrder> getOrdersByUserId(Long userId) {
        return jdbcTemplate.query("SELECT * FROM gift_delivery_orders WHERE user_id = ?", DELIVERY_MAPPER, userId);
    }

    public List<GiftDeliveryOrder> getAllOrders() {
        return jdbcTemplate.query("SELECT * FROM gift_delivery_orders ORDER BY create_time DESC", DELIVERY_MAPPER);
    }

    public int getTotalDeliveryOrders() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM gift_delivery_orders", Integer.class);
        return count != null ? count : 0;
    }

    public int getPendingDeliveryCount() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM gift_delivery_orders WHERE status = 0", Integer.class);
        return count != null ? count : 0;
    }

    public List<GiftDeliveryOrder> getPendingOrders() {
        return jdbcTemplate.query("SELECT * FROM gift_delivery_orders WHERE status = 0", DELIVERY_MAPPER);
    }

    @Transactional
    public GiftDeliveryOrder createPeriodicDelivery(Long userId, Long giftId, Long addressId) {
        Gift defaultGift = giftService.getDefaultGift();
        Gift selectedGift = giftService.getGiftById(giftId);
        BigDecimal priceDiff = BigDecimal.ZERO;
        if (defaultGift != null && selectedGift != null) {
            priceDiff = selectedGift.getPrice().subtract(defaultGift.getPrice());
            if (priceDiff.compareTo(BigDecimal.ZERO) < 0) {
                priceDiff = BigDecimal.ZERO;
            }
        }
        jdbcTemplate.update("INSERT INTO gift_delivery_orders (user_id, gift_id, address_id, price_diff, order_type, status, rebate_status) VALUES (?, ?, ?, ?, ?, ?, ?)",
            userId, giftId, addressId, priceDiff, 1, 0, 0);
        List<GiftDeliveryOrder> orders = jdbcTemplate.query("SELECT * FROM gift_delivery_orders WHERE user_id = ? AND gift_id = ? ORDER BY id DESC LIMIT 1", DELIVERY_MAPPER, userId, giftId);
        GiftDeliveryOrder order = orders.isEmpty() ? null : orders.get(0);
        if (order != null && priceDiff.compareTo(BigDecimal.ZERO) > 0) {
            boolean success = userService.deductBalance(userId, priceDiff);
            if (!success && defaultGift != null) {
                jdbcTemplate.update("UPDATE gift_delivery_orders SET gift_id = ?, price_diff = 0 WHERE id = ?", defaultGift.getId(), order.getId());
            }
        }
        return order;
    }

    @Transactional
    public GiftDeliveryOrder createOneTimeDelivery(Long userId, Long giftId, Long addressId) {
        Gift gift = giftService.getGiftById(giftId);
        if (gift == null) {
            throw new RuntimeException("Gift not found");
        }
        boolean success = userService.deductBalance(userId, gift.getPrice());
        if (!success) {
            throw new RuntimeException("Insufficient balance");
        }
        jdbcTemplate.update("INSERT INTO gift_delivery_orders (user_id, gift_id, address_id, price_diff, order_type, status, rebate_status) VALUES (?, ?, ?, ?, ?, ?, ?)",
            userId, giftId, addressId, gift.getPrice(), 2, 0, 0);
        List<GiftDeliveryOrder> orders = jdbcTemplate.query("SELECT * FROM gift_delivery_orders WHERE user_id = ? AND gift_id = ? ORDER BY id DESC LIMIT 1", DELIVERY_MAPPER, userId, giftId);
        return orders.isEmpty() ? null : orders.get(0);
    }

    @Transactional
    public void confirmDelivery(Long orderId) {
        jdbcTemplate.update("UPDATE gift_delivery_orders SET status = 2, deliver_time = ? WHERE id = ?", LocalDateTime.now(), orderId);
    }

    public List<GiftDeliveryOrder> getPendingRebateOrders() {
        return jdbcTemplate.query("SELECT * FROM gift_delivery_orders WHERE rebate_status = 0 AND order_type = 1", DELIVERY_MAPPER);
    }
}
