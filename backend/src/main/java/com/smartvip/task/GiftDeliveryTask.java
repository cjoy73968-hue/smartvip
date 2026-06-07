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
package com.smartvip.task;

import com.smartvip.entity.User;
import com.smartvip.service.MerchantConfigService;
import com.smartvip.service.GiftDeliveryService;
import com.smartvip.service.AddressService;
import com.smartvip.service.WechatNotificationService;
import com.smartvip.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class GiftDeliveryTask {

    @Autowired
    private MerchantConfigService merchantConfigService;

    @Autowired
    private GiftDeliveryService giftDeliveryService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private WechatNotificationService wechatNotificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String LOCK_KEY = "task:gift-delivery:lock";

    @Scheduled(cron = "0 0 10 * * ?")
    public void triggerPeriodicDelivery() {
        if (!acquireLock()) {
            return;
        }
        try {
            String deliveryCycle = merchantConfigService.getDeliveryCycle();
            LocalTime deliveryTime = LocalTime.parse("10:00:00");

            if (shouldTriggerToday(deliveryCycle)) {
                processAllActiveMembersPeriodicDelivery();
            }
        } finally {
            releaseLock();
        }
    }

    private boolean shouldTriggerToday(String deliveryCycle) {
        LocalDateTime now = LocalDateTime.now();
        switch (deliveryCycle) {
            case "daily":
                return true;
            case "weekly":
                return now.getDayOfWeek().getValue() == 1;
            case "monthly":
                return now.getDayOfMonth() == 1;
            case "quarterly":
                return now.getDayOfMonth() == 1 && now.getMonthValue() % 3 == 1;
            default:
                return false;
        }
    }

    private void processAllActiveMembersPeriodicDelivery() {
        List<User> activeMembers = userService.getActiveMembers();
        for (User user : activeMembers) {
            try {
                com.smartvip.entity.Address address = addressService.getDefaultAddress(user.getId());
                if (address == null) {
                    continue;
                }

                Long selectedGiftId = user.getSelectedGiftId();
                if (selectedGiftId == null) {
                    selectedGiftId = merchantConfigService.getDefaultGiftId();
                }

                giftDeliveryService.createPeriodicDelivery(user.getId(), selectedGiftId, address.getId());
            } catch (Exception e) {
                // Log error but continue with next user
            }
        }
    }

    private boolean acquireLock() {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(LOCK_KEY, "1", 30, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(result);
    }

    private void releaseLock() {
        redisTemplate.delete(LOCK_KEY);
    }
}