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
package com.smartvip.util;

import java.util.UUID;

public class ReferralCodeUtil {
    private static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String generate() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static boolean isValid(String code) {
        if (code == null || code.length() != 8) {
            return false;
        }
        for (char c : code.toCharArray()) {
            if (!CHARS.contains(String.valueOf(c))) {
                return false;
            }
        }
        return true;
    }
}