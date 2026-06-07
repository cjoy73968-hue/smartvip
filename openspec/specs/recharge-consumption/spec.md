## Purpose

TBD - Document the recharge and consumption purpose and scope.

## Requirements

### Requirement: User can recharge balance via WeChat Pay

The system SHALL allow users to recharge their balance through WeChat payment with real-time credit to account.

#### Scenario: Recharge via WeChat Pay
- **WHEN** user selects recharge amount or enters custom amount
- **THEN** system generates WeChat payment order
- **AND** user completes payment in WeChat
- **AND** upon payment success, system credits recharge_balance immediately
- **AND** system creates recharge log record

#### Scenario: Recharge amount goes to merchant's WeChat merchant account
- **WHEN** user completes recharge payment
- **THEN** funds are transferred to the merchant's registered WeChat merchant account
- **AND** system records the transaction ID from WeChat

### Requirement: User can view recharge history

The system SHALL maintain and display all recharge records for the user.

#### Scenario: View recharge records
- **WHEN** user opens recharge history page
- **THEN** system displays all recharge records with amount, time, and status
- **AND** records are sortable by date

### Requirement: User can consume balance to purchase goods

The system SHALL allow users to use their balance (recharge first, then rebate) to purchase products.

#### Scenario: Purchase goods with balance
- **WHEN** user selects product and confirms purchase
- **AND** user has sufficient combined balance
- **THEN** system deducts balance according to priority rules
- **AND** system creates consumption order
- **AND** system records the transaction

### Requirement: User can view consumption history

The system SHALL maintain and display all consumption records for the user.

#### Scenario: View consumption records
- **WHEN** user opens consumption history page
- **THEN** system displays all consumption orders with product name, amount, time, and status

### Requirement: Recharge transactions are idempotent

The system SHALL ensure that duplicate WeChat payment callbacks do not result in double credit.

#### Scenario: Handle duplicate payment callback
- **WHEN** WeChat sends payment success callback with transaction ID already processed
- **THEN** system ignores the duplicate callback
- **AND** no additional balance is credited