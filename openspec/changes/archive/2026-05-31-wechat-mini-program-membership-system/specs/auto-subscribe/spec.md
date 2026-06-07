## ADDED Requirements

### Requirement: User can enable auto-subscribe for membership renewal

The system SHALL allow users to enable auto-subscribe, which automatically deducts membership fee from their balance when membership is about to expire.

#### Scenario: Enable auto-subscribe
- **WHEN** user toggles auto-subscribe to ON
- **THEN** auto_subscribe flag is set to true
- **AND** system will check balance 30 days before expiry

#### Scenario: Auto-subscribe triggers when enabled and balance sufficient
- **WHEN** membership will expire in 30 days
- **AND** user has auto_subscribe enabled
- **AND** user has sufficient combined balance
- **THEN** system automatically deducts membership fee
- **AND** membership is renewed

#### Scenario: Auto-subscribe fails when balance insufficient
- **WHEN** membership will expire in 30 days
- **AND** user has auto_subscribe enabled
- **AND** user has insufficient balance
- **THEN** auto-subscribe fails
- **AND** system sends WeChat notification to user
- **AND** user must manually renew

### Requirement: User can disable auto-subscribe at any time

The system SHALL allow users to turn off auto-subscribe so no automatic deduction occurs.

#### Scenario: Disable auto-subscribe
- **WHEN** user toggles auto-subscribe to OFF
- **THEN** auto_subscribe flag is set to false
- **AND** no automatic deduction will occur on expiry

### Requirement: System sends reminder notification 30 days before expiry

The system SHALL send a WeChat template message to user when membership is about to expire in 30 days, regardless of auto-subscribe status.

#### Scenario: Send 30-day expiry reminder
- **WHEN** membership will expire in 30 days
- **THEN** system sends WeChat template message to user
- **AND** message contains: expiry date, remaining balance, auto-subscribe status

### Requirement: Auto-subscribe renewal extends from current expiry date

The system SHALL extend membership from the current expiry date, not from the renewal date.

#### Scenario: Extend from current expiry
- **WHEN** auto-subscribe triggers on expiry date
- **THEN** new expiry is calculated from current expiry, not from today
- **AND** membership is continuous without gap