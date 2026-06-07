## 1. Project Setup

- [x] 1.1 Initialize Spring Boot 3 project with Java 17
- [x] 1.2 Configure MyBatis-Plus and database connection
- [x] 1.3 Configure Redis for session and distributed locks
- [x] 1.4 Set up WeChat SDK dependencies
- [x] 1.5 Configure七牛云OSS file upload
- [x] 1.6 Create database schema (users, addresses, recharge_logs, consumption_orders, gifts, gift_delivery_orders, rebate_logs, merchant_config, withdrawal_logs)
- [x] 1.7 Set up Docker Compose for local development

## 2. User Account & Authentication

- [x] 2.1 Implement WeChat authorization login (openid, nickname, avatar)
- [x] 2.2 Implement phone number binding with SMS verification
- [x] 2.3 Implement referral code generation (unique alphanumeric)
- [x] 2.4 Implement referral relationship binding during registration
- [x] 2.5 Create user registration API endpoints

## 3. Dual Account Balance System

- [x] 3.1 Implement recharge_balance and rebate_balance fields
- [x] 3.2 Implement balance deduction with priority (recharge first, then rebate)
- [x] 3.3 Implement transactional balance operations with rollback
- [x] 3.4 Implement debt tracking for negative rebate balance
- [x] 3.5 Create balance query API endpoints

## 4. Membership Subscription

- [x] 4.1 Implement membership purchase flow (balance deduction)
- [x] 4.2 Implement membership expiry calculation and tracking
- [x] 4.3 Implement membership benefits (periodic delivery, referral rebate) status check
- [x] 4.4 Implement auto-subscribe enable/disable
- [x] 4.5 Implement automatic subscription renewal (cron job)
- [x] 4.6 Create membership status API endpoints

## 5. Recharge & Consumption

- [x] 5.1 Implement WeChat Pay integration for recharge
- [x] 5.2 Implement payment callback handling (idempotent)
- [x] 5.3 Implement consumption order creation
- [x] 5.4 Implement order status management (pending/delivered/completed)
- [x] 5.5 Create recharge and consumption history API endpoints

## 6. Address Management

- [x] 6.1 Implement address CRUD operations
- [x] 6.2 Implement default address setting
- [x] 6.3 Implement delivery availability check (city-based)
- [x] 6.4 Create address management API endpoints

## 7. Referral & Rebate System

- [x] 7.1 Implement referral code generation per user
- [x] 7.2 Implement referral statistics query (without exposing referree info)
- [x] 7.3 Implement rebate calculation on consumption completion
- [x] 7.4 Implement 7-day delayed rebate credit (cron job)
- [x] 7.5 Implement rebate clawback for refunds
- [x] 7.6 Implement membership expiry rebate stop
- [x] 7.7 Create referral and rebate API endpoints

## 8. Product & Gift Management

- [x] 8.1 Implement product CRUD (name, price, stock, image, category)
- [x] 8.2 Implement default gift setting
- [x] 8.3 Implement product shelf/upshelf management
- [x] 8.4 Implement inventory tracking and alerts
- [x] 8.5 Create product management API endpoints

## 9. Gift Delivery System

- [x] 9.1 Implement periodic delivery trigger (based on configured cycle)
- [x] 9.2 Implement price difference calculation (user selected - default)
- [x] 9.3 Implement insufficient balance handling (switch to default gift)
- [x] 9.4 Implement one-time delivery order creation
- [x] 9.5 Implement delivery confirmation by merchant
- [x] 9.6 Implement gift change effective timing logic
- [x] 9.7 Implement delivery order status management
- [x] 9.8 Create gift delivery API endpoints

## 10. Merchant Backend

- [x] 10.1 Implement merchant login and authentication
- [x] 10.2 Implement sub-account creation and permission management
- [x] 10.3 Implement member list with search/filter
- [x] 10.4 Implement member editing (remark, expiry, balance, freeze)
- [x] 10.5 Implement order list with type and status filter
- [x] 10.6 Implement delivery confirmation
- [x] 10.7 Implement rebate rate configuration
- [x] 10.8 Implement membership price and validity configuration
- [x] 10.9 Implement default gift and delivery cycle configuration
- [x] 10.10 Implement delivery city configuration
- [x] 10.11 Implement withdrawal request processing
- [x] 10.12 Implement data reports (membership, transaction, delivery)
- [x] 10.13 Implement marketing message sending
- [x] 10.14 Build Vue3 merchant admin UI

## 11. WeChat Notifications

- [x] 11.1 Implement template message configuration
- [x] 11.2 Implement delivery order notification to merchant
- [x] 11.3 Implement insufficient balance notification to user
- [x] 11.4 Implement rebate credit notification to user
- [x] 11.5 Implement membership expiry reminder (30 days)
- [x] 11.6 Implement gift price change notification to user
- [x] 11.7 Implement marketing message with consent check

## 12. Scheduled Tasks

- [x] 12.1 Implement membership validity check (daily)
- [x] 12.2 Implement auto-subscribe renewal check (daily)
- [x] 12.3 Implement membership expiry reminder (daily, 30 days before)
- [x] 12.4 Implement periodic gift delivery trigger
- [x] 12.5 Implement rebate credit processing (check 7-day status)
- [x] 12.6 Implement scheduled task locking (Redis distributed lock)

## 13. Mini-Program Frontend

- [x] 13.1 Implement dual-entry registration (QR code / official account redirect)
- [x] 13.2 Implement user registration flow (WeChat auth -> phone -> address)
- [x] 13.3 Implement personal center (membership status, balances, referral code)
- [x] 13.4 Implement recharge center UI
- [x] 13.5 Implement product browsing and purchase
- [x] 13.6 Implement periodic gift selection UI
- [x] 13.7 Implement one-time delivery order UI
- [x] 13.8 Implement order history view
- [x] 13.9 Implement address management UI
- [x] 13.10 Implement promotional poster generation

## 14. Integration & Testing

- [ ] 14.1 Integrate WeChat Pay sandbox testing
- [ ] 14.2 Integrate WeChat template message testing
- [x] 14.3 Perform balance transaction consistency testing
- [x] 14.4 Perform rebate idempotency testing
- [x] 14.5 Perform scheduled task concurrency testing
- [ ] 14.6 Perform end-to-end flow testing (register -> recharge -> purchase -> referral -> rebate)