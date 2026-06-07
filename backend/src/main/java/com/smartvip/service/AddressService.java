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

import com.smartvip.entity.Address;
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
public class AddressService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Address> ADDRESS_MAPPER = (rs, rowNum) -> {
        Address address = new Address();
        address.setId(rs.getLong("id"));
        address.setUserId(rs.getLong("user_id"));
        address.setName(rs.getString("name"));
        address.setPhone(rs.getString("phone"));
        address.setProvince(rs.getString("province"));
        address.setCity(rs.getString("city"));
        address.setDistrict(rs.getString("district"));
        address.setDetailAddress(rs.getString("detail_address"));
        address.setIsDefault(rs.getInt("is_default"));
        address.setIsDeliverable(rs.getInt("is_deliverable"));
        address.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
        address.setUpdateTime(rs.getTimestamp("update_time").toLocalDateTime());
        return address;
    };

    public List<Address> getAddressesByUserId(Long userId) {
        return jdbcTemplate.query("SELECT * FROM addresses WHERE user_id = ?", ADDRESS_MAPPER, userId);
    }

    public Address getDefaultAddress(Long userId) {
        return jdbcTemplate.queryForObject("SELECT * FROM addresses WHERE user_id = ? AND is_default = 1", ADDRESS_MAPPER, userId);
    }

    @Transactional
    public Address createAddress(Long userId, String name, String phone, String province, String city, String district, String detailAddress, Boolean isDefault) {
        if (Boolean.TRUE.equals(isDefault)) {
            resetDefaultAddress(userId);
        }
        jdbcTemplate.update("INSERT INTO addresses (user_id, name, phone, province, city, district, detail_address, is_default, is_deliverable) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
            userId, name, phone, province, city, district, detailAddress, Boolean.TRUE.equals(isDefault) ? 1 : 0, 1);
        List<Address> addresses = jdbcTemplate.query("SELECT * FROM addresses WHERE user_id = ? AND phone = ? ORDER BY id DESC LIMIT 1", ADDRESS_MAPPER, userId, phone);
        return addresses.isEmpty() ? null : addresses.get(0);
    }

    @Transactional
    public void updateAddress(Address address) {
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            resetDefaultAddress(address.getUserId());
        }
        jdbcTemplate.update("UPDATE addresses SET name = ?, phone = ?, province = ?, city = ?, district = ?, detail_address = ?, is_default = ? WHERE id = ?",
            address.getName(), address.getPhone(), address.getProvince(), address.getCity(), address.getDistrict(), address.getDetailAddress(), address.getIsDefault(), address.getId());
    }

    @Transactional
    public void deleteAddress(Long addressId, Long userId) {
        jdbcTemplate.update("DELETE FROM addresses WHERE id = ? AND user_id = ?", addressId, userId);
    }

    @Transactional
    public void setDefaultAddress(Long userId, Long addressId) {
        resetDefaultAddress(userId);
        jdbcTemplate.update("UPDATE addresses SET is_default = 1 WHERE id = ? AND user_id = ?", addressId, userId);
    }

    private void resetDefaultAddress(Long userId) {
        jdbcTemplate.update("UPDATE addresses SET is_default = 0 WHERE user_id = ?", userId);
    }

    public boolean isDeliverable(String city, String deliverableCities) {
        if (deliverableCities == null || deliverableCities.isEmpty()) {
            return true;
        }
        return deliverableCities.contains(city);
    }
}
