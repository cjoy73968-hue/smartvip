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

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class SmsService {

    private Map<String, String> verificationCodes = new HashMap<>();
    private static final long CODE_EXPIRE_MS = 5 * 60 * 1000;
    private static final long CODE_TIMESTAMP_KEY = 0;

    public String sendVerificationCode(String phone) {
        if (!isValidPhone(phone)) {
            throw new IllegalArgumentException("Invalid phone number format");
        }

        String code = generateCode();
        long timestamp = System.currentTimeMillis();

        verificationCodes.put(phone, code + ":" + timestamp);

        sendSmsViaProvider(phone, code);

        return code;
    }

    public boolean verifyCode(String phone, String code) {
        if (phone == null || code == null) {
            return false;
        }

        String stored = verificationCodes.get(phone);
        if (stored == null) {
            return false;
        }

        String[] parts = stored.split(":");
        if (parts.length != 2) {
            return false;
        }

        String storedCode = parts[0];
        long timestamp = Long.parseLong(parts[1]);

        if (System.currentTimeMillis() - timestamp > CODE_EXPIRE_MS) {
            verificationCodes.remove(phone);
            return false;
        }

        if (storedCode.equals(code)) {
            verificationCodes.remove(phone);
            return true;
        }

        return false;
    }

    private String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    private void sendSmsViaProvider(String phone, String code) {
        // Integration points for SMS providers:
        // 1. Aliyun: https://help.aliyun.com/document_detail/112828.html
        // 2. Tencent Cloud: https://cloud.tencent.com/document/product/382/37759
        // 3. Huawei Cloud: https://support.huaweicloud.com/api-msgsms/sms-05-0011.html

        // Placeholder - in production, call actual SMS API:
        // String templateId = "SMS_xxxxx";
        // Map<String, String> params = new HashMap<>();
        // params.put("code", code);
        // smsClient.send(templateId, phone, params);
    }

    private boolean isValidPhone(String phone) {
        if (phone == null || phone.length() < 11) {
            return false;
        }
        return phone.matches("^1[3-9]\\d{9}$");
    }

    public void setVerificationCode(String phone, String code) {
        long timestamp = System.currentTimeMillis();
        verificationCodes.put(phone, code + ":" + timestamp);
    }
}