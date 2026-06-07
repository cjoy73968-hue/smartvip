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
import com.smartvip.entity.Gift;
import com.smartvip.service.GiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gift")
public class GiftController {

    @Autowired
    private GiftService giftService;

    @GetMapping("/list")
    public ApiResponse<List<Gift>> getShelfGifts() {
        List<Gift> gifts = giftService.getAllShelfGifts();
        return ApiResponse.success(gifts);
    }

    @GetMapping("/{id}")
    public ApiResponse<Gift> getGift(@PathVariable Long id) {
        Gift gift = giftService.getGiftById(id);
        return ApiResponse.success(gift);
    }

    @GetMapping("/default")
    public ApiResponse<Gift> getDefaultGift() {
        Gift gift = giftService.getDefaultGift();
        return ApiResponse.success(gift);
    }

    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<Gift>> getGiftsByCategory(@PathVariable Long categoryId) {
        List<Gift> gifts = giftService.getGiftsByCategory(categoryId);
        return ApiResponse.success(gifts);
    }

    @PostMapping
    public ApiResponse<Gift> createGift(@RequestBody Gift gift) {
        Gift created = giftService.createGift(gift.getName(), gift.getPrice(), gift.getStock(),
            gift.getImageUrl(), gift.getDescription(), gift.getCategoryId(),
            gift.getIsDefaultGift() == 1, gift.getIsShelf() == 1);
        return ApiResponse.success(created);
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> updateGift(@RequestBody Gift gift) {
        giftService.updateGift(gift);
        return ApiResponse.success();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteGift(@PathVariable Long id) {
        giftService.deleteGift(id);
        return ApiResponse.success();
    }
}