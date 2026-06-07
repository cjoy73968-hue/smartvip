# 会员管理系统 · Membership Management System

![Java](https://img.shields.io/badge/Java-17-brightgreen?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen?style=flat-square&logo=spring)
![Vue3](https://img.shields.io/badge/Vue3-3.3.4-brightgreen?style=flat-square&logo=vuedotjs)
![微信小程序](https://img.shields.io/badge/微信小程序-原生-blue?style=flat-square&logo=wechat)
![MySQL](https://img.shields.io/badge/MySQL-8.0-brightgreen?style=flat-square&logo=mysql)
![License](https://img.shields.io/badge/License-AGPL--3.0-yellow?style=flat-square)

商家专属的会员运营管理小程序，支持会员入驻、余额充值消费、推荐裂变、周期礼品派送、自动化履约等完整功能。已稳定运行，提供完整开源版本。

---

## 功能概览

### 用户端（小程序）

- 微信授权登录入驻，双入口（小程序码 + 公众号）
- 会员订阅（自动续费 + 到期提醒）
- 双子账户：充值余额（可提现可退款）+ 返利余额（仅消费）
- 余额充值（微信支付）/ 余额消费
- 推荐裂变（专属推荐码 / 海报分享）
- 商品浏览 / 购买
- 周期礼品派送（差价模式）+ 一次性礼品下单（全额模式）
- 收货地址管理（多地址 + 默认地址）
- 微信服务通知

### 商家端（PC后台）

- 会员管理（筛选 / 编辑 / 冻结 / 手动充值）
- 订单与派送管理（确认送达 / 周期派送 / 一次性派送）
- 商品管理（增删改 / 上下架 / 库存管理）
- 返利规则配置（自定义返利比例）
- 会员价格 / 有效期配置
- 礼品派送配置（默认礼品 / 派送周期 / 配送城市）
- 充值提现审批
- 数据报表统计
- 营销消息推送
- 子账号权限管理

---

## 技术栈

| 层级 | 技术 | 说明 |
|------|------|------|
| 小程序 | 微信小程序（原生 + JavaScript） | 微信生态最深度集成 |
| 管理后台 | Vue3 + TypeScript + Element Plus | PC浏览器访问 |
| 后端服务 | Java 17 + Spring Boot 3 + MyBatis-Plus | 成熟稳定，复杂业务友好 |
| 数据库 | MySQL 8.0 | 事务支持好 |
| 缓存 | Redis 7 | Session/分布式锁/热数据缓存 |
| 文件存储 | 七牛云OSS | 礼品图片存储 |
| 部署 | Docker + Nginx | 腾讯云轻量应用服务器 |

---

## 项目架构

```
微信生态
  |--- 小程序（用户端） 公众号（跳转入口）  微信支付

        Java Spring Boot 后端 REST API
        /api/user  /api/admin  /api/pay
        用户服务 · 订单服务 · 定时任务服务

              |                      |
        ┌─────────┐  ┌──────────┐  ┌─────────┐
          MySQL    |  Redis    |  七牛云   |
          持久化    |  缓存/锁   |  对象存储  |

        Vue3 PC 管理后台（商家端）
        会员管理 / 订单管理 / 商品管理 / 数据报表 / 系统配置
```

---

## 目录结构

```
/data/smartvip/
├── README.md                     # 项目说明
├── LICENSE                      # AGPL-3.0 开源协议
├── AGENTS.md                    # 项目 AI 协作规范
├── GenHash.java                 # 工具类
│
├── backend/                     # Java 后端（55个Java文件）
│   ├── pom.xml                  # Maven 配置（Spring Boot 3.2.0）
│   ├── docker-compose.yml # Docker 部署配置
│   └── src/main/java/com/smartvip/
│       ├── MembershipSystemApplication.java  # 启动类
│       ├── config/              # RedisConfig, WebConfig, CORS
│       ├── controller/          # REST API（13个控制器）
│       ├── service/             # 业务逻辑层
│       ├── entity/              # 数据实体（15个）
│       ├── dto/                 # 数据传输对象
│       ├── mapper/              # MyBatis-Plus Mapper
│       ├── task/                # 定时任务（礼品派送/会员有效期/返利）
│       └── util/                # 工具类
│
├── frontend/ # Vue3 管理后台（10个Vue组件）
│   ├── package.json            # vue@3.3.4 + vite@4.4.9 + element-plus@2.3.14
│   ├── vite.config.js
│   ├── index.html
│   └── src/
│       ├── views/              # 页面组件
│       ├── router/            # 路由配置
│       ├── api/               # API 调用层
│       └── utils/             # 工具函数
│
├── miniprogram/ # 微信小程序（原生，10个页面）
│   ├── app.js / app.json / project.config.json
│   ├── pages/
│   │   ├── register/           # 会员入驻
│   │   ├── personal/           # 个人中心
│   │   ├── recharge/           # 余额充值
│   │   ├── orders/             # 订单管理
│   │   ├── products/           # 商品列表
│   │   ├── product-detail/    # 商品详情
│   │   ├── referral/           # 推荐裂变
│   │   ├── poster/             # 海报分享
│   │   ├── periodic-gift/      # 周期礼品派送
│   │   └── addresses/         # 收货地址
│   ├── api/                   # API 封装
│   └── utils/                # 工具函数
│
├── docs/                      # 项目文档
│   ├── assets/               # 项目截图
│   ├── 需求文档补充说明.md    # 业务逻辑补充
│   ├── 架构设计.md           # 技术架构说明
│   ├── 小程序发布指南.md      # 小程序发布流程
│   └── sample_data.sql        # 示例数据
│
├── sql/                       # 数据库脚本
│   └── schema.sql            # 完整建表脚本
│
└── openspec/                 # 需求变更管理
    └── config.yaml
```

---

## 界面预览

PC管理后台截图见 `docs/assets/` 目录：

- `image1.png` — 登录页面
- `image2.png` — 首页统计
- `image3.png` — 会员管理
- `image4.png` — 订单管理
- `image.png`  — 架构图

---

## 快速开始

### 环境要求

- JDK 17+
- Node.js 18+
- MySQL 8.0
- Redis 7
- 微信小程序开发者工具

### 后端启动

```bash
cd backend
# 配置数据库连接
vim src/main/resources/application.yml
# 启动
mvn spring-boot:run
```

### 前端启动

```bash
cd frontend
npm install
npm run dev      # 开发模式 → http://localhost:3000
npm run build    # 生产构建
```

### 小程序开发

用微信开发者工具打开 `miniprogram/` 目录，填入 AppID 即可本地调试。

### 数据库初始化

```bash
mysql -u root -p < sql/schema.sql
```

---

## 部署（Docker）

```bash
cd backend
docker-compose up -d
```

服务端口：
- 后端 API：`8080`
- MySQL：`3306`
- Redis：`6379`

Nginx 反向代理 `80/443` 端口，分发 `/api/*` 到后端、`/admin/*` 到前端静态资源。

---

## 核心业务逻辑

### 双子账户

| 账户 | 来源 | 用途 | 可提现 | 可退款 |
|------|------|------|--------|--------|
| 充值余额 | 用户主动充值 | 消费、提现 | 是 | 是 |
| 返利余额 | 推荐返利 | 仅限消费 | 否 | 否 |

消费优先扣除充值余额，不足时自动用返利余额。

### 返利机制

- 被推荐人消费确认收货后 **7天**，返利自动到账推荐人
- 退款时已发返利自动追回
- 推荐关系永久绑定，不可变更

### 礼品派送两种模式

| 模式 | 费用 | 触发方式 |
|------|------|---------|
| 周期自动派送 | 差价（选品价格 - 默认礼品价格）| 系统按商家配置周期自动触发 |
| 一次性下单派送 | 全额（所选礼品价格）| 用户随时下单 |

---

## 相关文档

- [需求补充说明](./docs/需求文档补充说明.md) — 业务逻辑细节
- [架构设计](./docs/架构设计.md) — 技术选型与模块设计
- [小程序发布指南](./docs/小程序发布指南.md) — 发布流程

---

## 开源协议：AGPL-3.0

- 可自由使用、学习、修改
- 若通过网络使用（包括修改后部署），也必须开源
- 禁止直接闭源或嵌入闭源产品

详见 [LICENSE](./LICENSE)。

---

> 项目更新时间：2026-06-07
---

## 联系方式

有问题或合作意向，欢迎扫码联系：

![微信](./docs/assets/wechat1.png)
