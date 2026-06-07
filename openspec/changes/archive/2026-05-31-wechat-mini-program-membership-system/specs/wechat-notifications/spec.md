## ADDED Requirements

### Requirement: System sends WeChat template notifications for delivery orders to merchant

The system SHALL send WeChat template message to merchant when a new delivery order is created.

#### Scenario: Notify merchant of new delivery order
- **WHEN** a periodic delivery order is triggered OR user places a one-time delivery order
- **THEN** system sends WeChat template message to merchant
- **AND** message contains: order type, user address, gift name, amount

### Requirement: System sends WeChat template notification for insufficient balance to user

The system SHALL send WeChat template message to user when balance is insufficient for price difference deduction.

#### Scenario: Notify user of insufficient balance
- **WHEN** periodic delivery is triggered
- **AND** user has insufficient balance for price difference
- **AND** system delivers default gift instead
- **THEN** system sends WeChat template message to user
- **AND** message contains: default gift name, balance reminder

### Requirement: System sends WeChat template notification when rebate is credited to user

The system SHALL send WeChat template message to user when rebate is credited to their account.

#### Scenario: Notify user of rebate credit
- **WHEN** rebate becomes due (7 days after delivery confirmation)
- **AND** no refund has occurred
- **THEN** system credits rebate to user's rebate_balance
- **AND** system sends WeChat template message to user
- **AND** message contains: rebate amount, current balance

### Requirement: System sends WeChat template notification for membership expiry reminder

The system SHALL send WeChat template message to user 30 days before membership expires.

#### Scenario: Send membership expiry reminder
- **WHEN** user membership will expire in 30 days
- **AND** user has auto-subscribe disabled or insufficient balance
- **THEN** system sends WeChat template message to user
- **AND** message contains: expiry date, remaining balance, renewal suggestion

### Requirement: System sends WeChat template notification for gift price change to user

The system SHALL send WeChat template message to user when their selected periodic gift price changes.

#### Scenario: Notify user of gift price change
- **WHEN** merchant changes price of a product that is selected as user's periodic gift
- **THEN** system sends WeChat template message to user
- **AND** message contains: gift name, old price, new price, effective date

### Requirement: System sends marketing messages based on user consent

The system SHALL only send marketing messages to users who have explicitly consented.

#### Scenario: Send marketing message with consent
- **WHEN** merchant sends marketing message
- **AND** user has opted in to marketing messages
- **THEN** system sends WeChat template message to user

#### Scenario: Do not send marketing message without consent
- **WHEN** merchant sends marketing message
- **AND** user has not opted in
- **THEN** system does not send message to that user

### Requirement: User subscribes to notifications during order placement

The system SHALL require user to subscribe to relevant notifications when placing orders.

#### Scenario: Subscribe to notifications
- **WHEN** user places an order
- **THEN** system presents subscription options for order-related notifications
- **AND** user must confirm subscription before order is completed