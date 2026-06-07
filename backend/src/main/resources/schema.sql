--
-- Copyright (C) 2024 会员管理系统项目
--
-- 本项目采用 AGPL-3.0 开源协议开源。
--
-- 您可以自由使用、修改和分发本项目，但若通过网络使用（包括修改后部署），
-- 也必须按照 AGPL-3.0 协议开源您的修改版本。
--
-- 完整协议文本请参见 LICENSE 文件。
--
-- 会员管理系统数据库 schema

CREATE DATABASE IF NOT EXISTS membership_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE membership_db;

-- 用户表（会员）
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    openid VARCHAR(128) NOT NULL UNIQUE COMMENT '微信openid',
    nickname VARCHAR(64) COMMENT '微信昵称',
    avatar_url VARCHAR(256) COMMENT '头像URL',
    phone VARCHAR(20) COMMENT '手机号',
    referral_code VARCHAR(32) NOT NULL UNIQUE COMMENT '推荐码',
    referrer_id BIGINT COMMENT '推荐人ID',
    recharge_balance DECIMAL(10, 2) DEFAULT 0.00 COMMENT '充值余额',
    rebate_balance DECIMAL(10, 2) DEFAULT 0.00 COMMENT '返利余额',
    membership_start DATETIME COMMENT '会员开始时间',
    membership_expire DATETIME COMMENT '会员到期时间',
    auto_subscribe TINYINT(1) DEFAULT 0 COMMENT '自动订阅 0-关闭 1-开启',
    status TINYINT(1) DEFAULT 1 COMMENT '状态 1-正常 0-冻结',
    version INT DEFAULT 0 COMMENT '乐观锁版本',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_phone (phone),
    INDEX idx_referral_code (referral_code),
    INDEX idx_referrer_id (referrer_id),
    INDEX idx_membership_expire (membership_expire)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员表';

-- 收货地址表
CREATE TABLE addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(32) NOT NULL COMMENT '收货人姓名',
    phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    province VARCHAR(32) NOT NULL COMMENT '省',
    city VARCHAR(32) NOT NULL COMMENT '市',
    district VARCHAR(32) NOT NULL COMMENT '区',
    detail_address VARCHAR(128) NOT NULL COMMENT '详细地址',
    is_default TINYINT(1) DEFAULT 0 COMMENT '是否默认地址',
    is_deliverable TINYINT(1) DEFAULT 1 COMMENT '是否可配送',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

-- 充值记录表
CREATE TABLE recharge_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL COMMENT '充值金额',
    wx_transaction_id VARCHAR(64) COMMENT '微信交易单号',
    status TINYINT(1) DEFAULT 1 COMMENT '状态 1-成功 0-失败',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_wx_transaction_id (wx_transaction_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充值记录表';

-- 消费订单表
CREATE TABLE consumption_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_name VARCHAR(128) NOT NULL COMMENT '商品名称',
    amount DECIMAL(10, 2) NOT NULL COMMENT '消费金额',
    referrer_id BIGINT COMMENT '推荐人ID',
    rebate_amount DECIMAL(10, 2) DEFAULT 0.00 COMMENT '返利金额',
    rebate_status TINYINT(1) DEFAULT 0 COMMENT '返利状态 0-待到账 1-已到账 2-已追回',
    deliver_status TINYINT(1) DEFAULT 0 COMMENT '发货状态 0-待发货 1-已发货 2-已完成',
    confirm_time DATETIME COMMENT '确认收货时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_referrer_id (referrer_id),
    INDEX idx_rebate_status (rebate_status),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (referrer_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消费订单表';

-- 商品/礼品表
CREATE TABLE gifts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL COMMENT '商品名称',
    price DECIMAL(10, 2) NOT NULL COMMENT '价格',
    stock INT DEFAULT 0 COMMENT '库存',
    image_url VARCHAR(256) COMMENT '图片URL',
    description TEXT COMMENT '商品描述',
    category_id BIGINT COMMENT '分类ID',
    is_default_gift TINYINT(1) DEFAULT 0 COMMENT '是否默认礼品',
    is_shelf TINYINT(1) DEFAULT 1 COMMENT '是否上架 1-上架 0-下架',
    status TINYINT(1) DEFAULT 1 COMMENT '状态 1-正常 0-删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category_id (category_id),
    INDEX idx_is_default_gift (is_default_gift)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品/礼品表';

-- 商品分类表
CREATE TABLE gift_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(32) NOT NULL COMMENT '分类名称',
    sort_order INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 礼品派送订单表
CREATE TABLE gift_delivery_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    gift_id BIGINT NOT NULL COMMENT '礼品ID',
    address_id BIGINT NOT NULL COMMENT '地址ID',
    price_diff DECIMAL(10, 2) DEFAULT 0.00 COMMENT '差价（周期性派送）',
    order_type TINYINT(1) NOT NULL COMMENT '订单类型 1-周期派送 2-一次性派送',
    status TINYINT(1) DEFAULT 0 COMMENT '状态 0-待发货 1-已发货 2-已完成',
    deliver_time DATETIME COMMENT '确认送达时间',
    rebate_status TINYINT(1) DEFAULT 0 COMMENT '返利状态 0-待到账 1-已到账 2-已追回',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_order_type (order_type),
    INDEX idx_status (status),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (gift_id) REFERENCES gifts(id) ON DELETE CASCADE,
    FOREIGN KEY (address_id) REFERENCES addresses(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='礼品派送订单表';

-- 返利记录表
CREATE TABLE rebate_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    referrer_id BIGINT NOT NULL COMMENT '推荐人ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_type TINYINT(1) NOT NULL COMMENT '订单类型 1-消费订单 2-礼品派送',
    amount DECIMAL(10, 2) NOT NULL COMMENT '返利金额',
    status TINYINT(1) DEFAULT 0 COMMENT '状态 0-待到账 1-已到账 2-已追回',
    arrive_time DATETIME COMMENT '到账时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_referrer_id (referrer_id),
    INDEX idx_status (status),
    FOREIGN KEY (referrer_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='返利记录表';

-- 商户配置表
CREATE TABLE merchant_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rebate_rate DECIMAL(5, 4) DEFAULT 0.0500 COMMENT '返利比例 5%',
    membership_price DECIMAL(10, 2) DEFAULT 299.00 COMMENT '会员价格',
    membership_days INT DEFAULT 365 COMMENT '默认会员有效期天数',
    delivery_city VARCHAR(256) COMMENT '可配送城市JSON数组',
    delivery_cycle VARCHAR(32) DEFAULT 'monthly' COMMENT '派送周期 daily/weekly/monthly/quarterly',
    delivery_time TIME DEFAULT '10:00:00' COMMENT '每日派送时间',
    default_gift_id BIGINT COMMENT '默认礼品ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户配置表';

-- 商家用户表（后台管理）
CREATE TABLE merchant_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(32) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(128) NOT NULL COMMENT '密码（加密存储）',
    nickname VARCHAR(64) COMMENT '昵称',
    role VARCHAR(16) DEFAULT 'admin' COMMENT '角色 admin/subaccount',
    permissions VARCHAR(256) COMMENT '权限JSON',
    status TINYINT(1) DEFAULT 1 COMMENT '状态 1-正常 0-禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商家用户表';

-- 提现记录表
CREATE TABLE withdrawal_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL COMMENT '提现金额',
    status TINYINT(1) DEFAULT 0 COMMENT '状态 0-申请 1-已确认 2-已打款',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    confirm_time DATETIME COMMENT '确认时间',
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提现记录表';

-- 微信消息模板配置表
CREATE TABLE wechat_template_configs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    template_type VARCHAR(32) NOT NULL COMMENT '模板类型 delivery_order/balance_insufficient/rebate_credit/membership_expiry/gift_price_change/marketing',
    template_id VARCHAR(64) NOT NULL COMMENT '微信模板ID',
    title VARCHAR(64) COMMENT '模板标题',
    content_pattern TEXT COMMENT '内容模式（JSON）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE INDEX idx_template_type (template_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信消息模板配置表';

-- 用户消息订阅记录表
CREATE TABLE user_subscriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    template_type VARCHAR(32) NOT NULL COMMENT '订阅类型',
    subscribed TINYINT(1) DEFAULT 1 COMMENT '是否订阅 1-是 0-否',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    UNIQUE INDEX idx_user_template (user_id, template_type),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户消息订阅记录表';

-- 初始化商户配置
INSERT INTO merchant_config (rebate_rate, membership_price, membership_days, delivery_cycle) VALUES (0.0500, 299.00, 365, 'monthly');

-- 初始化商家管理员账户 (密码: admin123)
INSERT INTO merchant_users (username, password, nickname, role, permissions) VALUES ('admin', '$2a$10$D0oR8FYMNuyRwZa3omQdiu1JHZfhfYxRMlPo8rc68GaRDr8EYMXoG', '系统管理员', 'admin', '*');