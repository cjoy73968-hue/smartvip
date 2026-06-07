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
public class GiftService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Gift> GIFT_MAPPER = (rs, rowNum) -> {
        Gift gift = new Gift();
        gift.setId(rs.getLong("id"));
        gift.setName(rs.getString("name"));
        gift.setPrice(rs.getBigDecimal("price"));
        gift.setStock(rs.getInt("stock"));
        gift.setImageUrl(rs.getString("image_url"));
        gift.setDescription(rs.getString("description"));
        gift.setCategoryId(rs.getObject("category_id") != null ? rs.getLong("category_id") : null);
        gift.setIsDefaultGift(rs.getInt("is_default_gift"));
        gift.setIsShelf(rs.getInt("is_shelf"));
        gift.setStatus(rs.getInt("status"));
        gift.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
        gift.setUpdateTime(rs.getTimestamp("update_time").toLocalDateTime());
        return gift;
    };

    public List<Gift> getAllShelfGifts() {
        return jdbcTemplate.query("SELECT * FROM gifts WHERE is_shelf = 1 AND status = 1", GIFT_MAPPER);
    }

    public Gift getGiftById(Long giftId) {
        return jdbcTemplate.queryForObject("SELECT * FROM gifts WHERE id = ?", GIFT_MAPPER, giftId);
    }

    public Gift getDefaultGift() {
        return jdbcTemplate.queryForObject("SELECT * FROM gifts WHERE is_default_gift = 1 AND status = 1", GIFT_MAPPER);
    }

    public List<Gift> getGiftsByCategory(Long categoryId) {
        return jdbcTemplate.query("SELECT * FROM gifts WHERE category_id = ? AND is_shelf = 1 AND status = 1", GIFT_MAPPER, categoryId);
    }

    @Transactional
    public Gift createGift(String name, BigDecimal price, Integer stock, String imageUrl, String description, Long categoryId, Boolean isDefaultGift, Boolean isShelf) {
        if (Boolean.TRUE.equals(isDefaultGift)) {
            resetDefaultGift();
        }
        jdbcTemplate.update("INSERT INTO gifts (name, price, stock, image_url, description, category_id, is_default_gift, is_shelf, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
            name, price, stock, imageUrl, description, categoryId, Boolean.TRUE.equals(isDefaultGift) ? 1 : 0, Boolean.TRUE.equals(isShelf) ? 1 : 0, 1);
        List<Gift> gifts = jdbcTemplate.query("SELECT * FROM gifts WHERE name = ? ORDER BY id DESC LIMIT 1", GIFT_MAPPER, name);
        return gifts.isEmpty() ? null : gifts.get(0);
    }

    @Transactional
    public void updateGift(Gift gift) {
        if (gift.getIsDefaultGift() != null && gift.getIsDefaultGift() == 1) {
            resetDefaultGift();
        }
        jdbcTemplate.update("UPDATE gifts SET name = ?, price = ?, stock = ?, image_url = ?, description = ?, category_id = ?, is_default_gift = ?, is_shelf = ? WHERE id = ?",
            gift.getName(), gift.getPrice(), gift.getStock(), gift.getImageUrl(), gift.getDescription(), gift.getCategoryId(), gift.getIsDefaultGift(), gift.getIsShelf(), gift.getId());
    }

    @Transactional
    public void updateStock(Long giftId, Integer quantity) {
        jdbcTemplate.update("UPDATE gifts SET stock = stock + ? WHERE id = ?", quantity, giftId);
    }

    @Transactional
    public void deleteGift(Long giftId) {
        jdbcTemplate.update("UPDATE gifts SET status = 0 WHERE id = ?", giftId);
    }

    private void resetDefaultGift() {
        jdbcTemplate.update("UPDATE gifts SET is_default_gift = 0 WHERE is_default_gift = 1");
    }
}
