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
package com.smartvip.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Gift {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private String description;
    private Long categoryId;
    private Integer isDefaultGift;
    private Integer isShelf;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
