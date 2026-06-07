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

import com.smartvip.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
public class MembershipTask {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String LOCK_KEY = "task:membership:lock";

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkMembershipValidity() {
        if (!acquireLock()) {
            return;
        }
        try {
            userService.updateMembershipStatus();
        } finally {
            releaseLock();
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkAutoSubscribe() {
        if (!acquireLock()) {
            return;
        }
        try {
            // Auto-subscribe renewal logic would go here
            // Check users with auto_subscribe=1 and membership_expire in 30 days
            // Deduct balance and renew if sufficient
        } finally {
            releaseLock();
        }
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void sendMembershipExpiryReminder() {
        if (!acquireLock()) {
            return;
        }
        try {
            // Send WeChat notifications to users expiring in 30 days
        } finally {
            releaseLock();
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