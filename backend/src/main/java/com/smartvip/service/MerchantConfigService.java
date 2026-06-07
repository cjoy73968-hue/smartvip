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

import com.smartvip.entity.MerchantConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class MerchantConfigService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<MerchantConfig> CONFIG_MAPPER = (rs, rowNum) -> {
        MerchantConfig config = new MerchantConfig();
        config.setId(rs.getLong("id"));
        config.setRebateRate(rs.getBigDecimal("rebate_rate"));
        config.setMembershipPrice(rs.getBigDecimal("membership_price"));
        config.setMembershipDays(rs.getInt("membership_days"));
        config.setDeliveryCity(rs.getString("delivery_city"));
        config.setDeliveryCycle(rs.getString("delivery_cycle"));
        config.setDeliveryTime(rs.getString("delivery_time"));
        config.setDefaultGiftId(rs.getObject("default_gift_id") != null ? rs.getLong("default_gift_id") : null);
        return config;
    };

    public MerchantConfig getCurrentConfig() {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM merchant_config LIMIT 1", CONFIG_MAPPER);
        } catch (Exception e) {
            return null;
        }
    }

    public BigDecimal getRebateRate() {
        MerchantConfig config = getCurrentConfig();
        return config != null ? config.getRebateRate() : new BigDecimal("0.05");
    }

    public BigDecimal getMembershipPrice() {
        MerchantConfig config = getCurrentConfig();
        return config != null ? config.getMembershipPrice() : new BigDecimal("299.00");
    }

    public Integer getMembershipDays() {
        MerchantConfig config = getCurrentConfig();
        return config != null ? config.getMembershipDays() : 365;
    }

    public String getDeliveryCity() {
        MerchantConfig config = getCurrentConfig();
        return config != null ? config.getDeliveryCity() : "";
    }

    public String getDeliveryCycle() {
        MerchantConfig config = getCurrentConfig();
        return config != null ? config.getDeliveryCycle() : "monthly";
    }

    public Long getDefaultGiftId() {
        MerchantConfig config = getCurrentConfig();
        return config != null ? config.getDefaultGiftId() : null;
    }

    @Transactional
    public void updateConfig(MerchantConfig config) {
        MerchantConfig current = getCurrentConfig();
        if (current != null) {
            jdbcTemplate.update("UPDATE merchant_config SET rebate_rate=?, membership_price=?, membership_days=?, delivery_city=?, delivery_cycle=?, delivery_time=?, default_gift_id=? WHERE id=?",
                config.getRebateRate(), config.getMembershipPrice(), config.getMembershipDays(),
                config.getDeliveryCity(), config.getDeliveryCycle(), config.getDeliveryTime(),
                config.getDefaultGiftId(), current.getId());
        } else {
            jdbcTemplate.update("INSERT INTO merchant_config (rebate_rate, membership_price, membership_days, delivery_city, delivery_cycle, delivery_time, default_gift_id) VALUES (?,?,?,?,?,?,?)",
                config.getRebateRate(), config.getMembershipPrice(), config.getMembershipDays(),
                config.getDeliveryCity(), config.getDeliveryCycle(), config.getDeliveryTime(),
                config.getDefaultGiftId());
        }
    }

    @Transactional
    public void updateRebateRate(BigDecimal rate) {
        MerchantConfig config = getCurrentConfig();
        if (config != null) {
            jdbcTemplate.update("UPDATE merchant_config SET rebate_rate=? WHERE id=?", rate, config.getId());
        }
    }

    @Transactional
    public void updateMembershipPrice(BigDecimal price) {
        MerchantConfig config = getCurrentConfig();
        if (config != null) {
            jdbcTemplate.update("UPDATE merchant_config SET membership_price=? WHERE id=?", price, config.getId());
        }
    }
}
