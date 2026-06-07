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
import com.smartvip.entity.Address;
import com.smartvip.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/user/{userId}")
    public ApiResponse<List<Address>> getAddresses(@PathVariable Long userId) {
        List<Address> addresses = addressService.getAddressesByUserId(userId);
        return ApiResponse.success(addresses);
    }

    @GetMapping("/user/{userId}/default")
    public ApiResponse<Address> getDefaultAddress(@PathVariable Long userId) {
        Address address = addressService.getDefaultAddress(userId);
        return ApiResponse.success(address);
    }

    @PostMapping("/user/{userId}")
    public ApiResponse<Address> createAddress(@PathVariable Long userId, @RequestBody Address request) {
        Address address = addressService.createAddress(userId, request.getName(), request.getPhone(),
            request.getProvince(), request.getCity(), request.getDistrict(),
            request.getDetailAddress(), request.getIsDefault() == 1);
        return ApiResponse.success(address);
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> updateAddress(@RequestBody Address address) {
        addressService.updateAddress(address);
        return ApiResponse.success();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAddress(@PathVariable Long id, @RequestParam Long userId) {
        addressService.deleteAddress(id, userId);
        return ApiResponse.success();
    }

    @PutMapping("/user/{userId}/default/{addressId}")
    public ApiResponse<Void> setDefaultAddress(@PathVariable Long userId, @PathVariable Long addressId) {
        addressService.setDefaultAddress(userId, addressId);
        return ApiResponse.success();
    }
}