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
-- 会员管理系统测试数据
-- 执行方式: mysql -h 192.168.1.25 -u a_app -pAbcd1234 membership_db < sample_data.sql

USE membership_db;

-- ============================================
-- 1. 商户配置表 (merchant_config)
-- ============================================
INSERT INTO merchant_config (rebate_rate, membership_price, membership_days, delivery_cycle, delivery_time)
VALUES (0.0500, 299.00, 365, 'monthly', '10:00:00');

-- ============================================
-- 2. 商家用户表 (merchant_users)
-- ============================================
INSERT INTO merchant_users (username, password, nickname, role, permissions, status)
VALUES ('admin', '$2a$10$D0oR8FYMNuyRwZa3omQdiu1JHZfhfYxRMlPo8rc68GaRDr8EYMXoG', '系统管理员', 'admin', '*', 1);

-- 密码为 admin123

-- ============================================
-- 3. 会员用户表 (users)
-- ============================================
INSERT INTO users (openid, nickname, avatar_url, phone, referral_code, referrer_id, recharge_balance, rebate_balance, membership_start, membership_expire, auto_subscribe, status)
VALUES
('openid001', '张三', 'https://example.com/avatar1.png', '13800138001', 'REF001', NULL, 1000.00, 50.00, '2026-01-01 10:00:00', '2027-01-01 10:00:00', 1, 1),
('openid002', '李四', 'https://example.com/avatar2.png', '13800138002', 'REF002', 1, 500.00, 30.00, '2026-02-15 14:30:00', '2027-02-15 14:30:00', 0, 1),
('openid003', '王五', 'https://example.com/avatar3.png', '13800138003', 'REF003', 1, 200.00, 100.00, '2026-03-20 09:00:00', '2027-03-20 09:00:00', 1, 1),
('openid004', '赵六', 'https://example.com/avatar4.png', '13800138004', 'REF004', 2, 0.00, 20.00, '2026-04-10 16:00:00', '2026-04-10 16:00:00', 0, 0),
('openid005', '钱七', 'https://example.com/avatar5.png', '13800138005', 'REF005', NULL, 800.00, 80.00, '2026-05-01 11:00:00', '2027-05-01 11:00:00', 1, 1);

-- ============================================
-- 4. 礼品分类表 (gift_categories)
-- ============================================
INSERT INTO gift_categories (name, sort_order)
VALUES
('保健品', 1),
('家居用品', 2),
('数码配件', 3);

-- ============================================
-- 5. 礼品表 (gifts)
-- ============================================
INSERT INTO gifts (name, price, stock, image_url, description, category_id, is_default_gift, is_shelf, status)
VALUES
('蜂蜜礼盒', 99.00, 100, 'https://example.com/gift1.jpg', '优质蜂蜜礼盒装', 1, 1, 1, 1),
('小米手环', 199.00, 50, 'https://example.com/gift2.jpg', '小米手环6 NFC版', 3, 0, 1, 1),
('床上四件套', 299.00, 30, 'https://example.com/gift3.jpg', '纯棉床上四件套', 2, 0, 1, 1),
('蓝牙耳机', 399.00, 20, 'https://example.com/gift4.jpg', '无线蓝牙耳机', 3, 0, 1, 1),
('保温杯', 59.00, 200, 'https://example.com/gift5.jpg', '不锈钢保温杯', 2, 0, 1, 1);

-- ============================================
-- 6. 收货地址表 (addresses)
-- ============================================
INSERT INTO addresses (user_id, name, phone, province, city, district, detail_address, is_default, is_deliverable)
VALUES
(1, '张三', '13800138001', '广东省', '深圳市', '南山区', '科技园路1号', 1, 1),
(2, '李四', '13800138002', '广东省', '广州市', '天河区', '天河路100号', 1, 1),
(3, '王五', '13800138003', '浙江省', '杭州市', '西湖区', '文一路200号', 1, 1),
(5, '钱七', '13800138005', '上海市', '浦东新区', '张江镇', '祖冲之路300号', 1, 1);

-- ============================================
-- 7. 充值记录表 (recharge_logs)
-- ============================================
INSERT INTO recharge_logs (user_id, amount, wx_transaction_id, status)
VALUES
(1, 500.00, 'WX_RECHARGE_001', 1),
(1, 500.00, 'WX_RECHARGE_002', 1),
(2, 300.00, 'WX_RECHARGE_003', 1),
(3, 200.00, 'WX_RECHARGE_004', 1),
(5, 800.00, 'WX_RECHARGE_005', 1);

-- ============================================
-- 8. 消费订单表 (consumption_orders)
-- ============================================
INSERT INTO consumption_orders (user_id, product_name, amount, referrer_id, rebate_amount, rebate_status, deliver_status, confirm_time)
VALUES
(1, '会员年费', 299.00, NULL, NULL, 0, 2, '2026-01-01 10:30:00'),
(2, '会员年费', 299.00, 1, 14.95, 1, 2, '2026-02-15 15:00:00'),
(3, '会员年费', 299.00, 1, 14.95, 0, 2, '2026-03-20 09:30:00'),
(5, '会员年费', 299.00, NULL, NULL, 1, 2, '2026-05-01 11:30:00');

-- ============================================
-- 9. 礼品派送订单表 (gift_delivery_orders)
-- ============================================
INSERT INTO gift_delivery_orders (user_id, gift_id, address_id, price_diff, order_type, status, rebate_status)
VALUES
(1, 1, 1, 0.00, 1, 2, 0),
(2, 2, 2, 100.00, 1, 2, 0),
(3, 3, 3, 200.00, 1, 1, 0),
(5, 4, 4, 300.00, 2, 2, 0);

-- ============================================
-- 10. 返利记录表 (rebate_logs)
-- ============================================
INSERT INTO rebate_logs (referrer_id, order_id, order_type, amount, status, arrive_time)
VALUES
(1, 2, 1, 14.95, 1, '2026-02-20 10:00:00'),
(1, 3, 1, 14.95, 0, NULL);

-- ============================================
-- 11. 提现记录表 (withdrawal_logs)
-- ============================================
INSERT INTO withdrawal_logs (user_id, amount, status, confirm_time)
VALUES
(1, 50.00, 2, '2026-06-01 12:00:00'),
(3, 30.00, 1, NULL);

-- ============================================
-- 12. 用户消息订阅记录表 (user_subscriptions)
-- ============================================
INSERT INTO user_subscriptions (user_id, template_type, subscribed)
VALUES
(1, 'delivery_order', 1),
(1, 'balance_insufficient', 1),
(2, 'delivery_order', 1),
(3, 'membership_expiry', 1);

-- ============================================
-- 数据验证查询
-- ============================================
SELECT '数据初始化完成!' AS result;

SELECT COUNT(*) AS user_count FROM users;
SELECT COUNT(*) AS gift_count FROM gifts;
SELECT COUNT(*) AS order_count FROM consumption_orders;
SELECT COUNT(*) AS delivery_order_count FROM gift_delivery_orders;
