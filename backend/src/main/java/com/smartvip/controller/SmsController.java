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
import com.smartvip.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @PostMapping("/send-code")
    public ApiResponse<String> sendVerificationCode(@RequestParam String phone) {
        try {
            String code = smsService.sendVerificationCode(phone);
            return ApiResponse.success(code);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("发送验证码失败");
        }
    }

    @PostMapping("/verify")
    public ApiResponse<Boolean> verifyCode(@RequestParam String phone, @RequestParam String code) {
        boolean success = smsService.verifyCode(phone, code);
        if (success) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error(400, "验证码错误或已过期");
        }
    }
}