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

import com.smartvip.entity.ConsumptionOrder;
import com.smartvip.entity.GiftDeliveryOrder;
import com.smartvip.service.GiftDeliveryService;
import com.smartvip.service.OrderService;
import com.smartvip.service.RebateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RebateTask {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GiftDeliveryService giftDeliveryService;

    @Autowired
    private RebateService rebateService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String LOCK_KEY = "task:rebate:lock";

    @Scheduled(cron = "0 0 0 * * ?")
    public void processRebateCredit() {
        if (!acquireLock()) {
            return;
        }
        try {
            processConsumptionOrderRebates();
            processGiftDeliveryRebates();
        } finally {
            releaseLock();
        }
    }

    private void processConsumptionOrderRebates() {
        List<ConsumptionOrder> pendingOrders = orderService.getPendingRebateOrders();
        for (ConsumptionOrder order : pendingOrders) {
            if (order.getConfirmTime() != null) {
                long daysSinceConfirm = java.time.temporal.ChronoUnit.DAYS.between(
                    order.getConfirmTime(), java.time.LocalDateTime.now());
                if (daysSinceConfirm >= 7) {
                    rebateService.creditRebateByOrderId(order.getId(), 1);
                }
            }
        }
    }

    private void processGiftDeliveryRebates() {
        List<GiftDeliveryOrder> pendingOrders = giftDeliveryService.getPendingRebateOrders();
        for (GiftDeliveryOrder order : pendingOrders) {
            if (order.getDeliverTime() != null) {
                long daysSinceDeliver = java.time.temporal.ChronoUnit.DAYS.between(
                    order.getDeliverTime(), java.time.LocalDateTime.now());
                if (daysSinceDeliver >= 7) {
                    rebateService.creditRebateByOrderId(order.getId(), 2);
                }
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