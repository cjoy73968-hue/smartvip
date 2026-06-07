## ADDED Requirements

### Requirement: User can purchase membership

The system SHALL allow users to purchase membership using their account balance (recharge balance first, then rebate balance if insufficient).

#### Scenario: Purchase membership with sufficient recharge balance
- **WHEN** user has recharge balance >= membership price
- **THEN** system deducts the amount from recharge balance
- **AND** membership start date is set to current timestamp
- **AND** membership expiry date is calculated based on merchant configuration
- **AND** user gains all membership benefits

#### Scenario: Purchase membership with combined balance
- **WHEN** user has recharge balance < membership price AND recharge balance + rebate balance >= membership price
- **THEN** system deducts remaining recharge balance first
- **AND** system deducts remaining amount from rebate balance
- **AND** membership is activated

### Requirement: Membership validity period is calculated from purchase moment

The system SHALL start calculating membership validity immediately upon successful payment.

#### Scenario: Membership period calculation
- **WHEN** user purchases membership at time T
- **THEN** membership_start is set to T
- **AND** membership_expire is calculated as T + configured days

### Requirement: User can view membership status in personal center

The system SHALL display remaining validity, expiry date, and subscription status in the user's personal center.

#### Scenario: View membership status
- **WHEN** user opens personal center
- **THEN** system displays remaining days of membership
- **AND** system displays exact expiry date and time
- **AND** system displays whether auto-subscribe is enabled

### Requirement: Membership benefits cease upon expiry

The system SHALL immediately stop all membership benefits when membership expires.

#### Scenario: Benefits stop at expiry
- **WHEN** membership_expire time is reached
- **THEN** periodic gift delivery stops immediately
- **AND** referral rebate stops (referrer no longer receives rebate from this user)
- **AND** user can still consume remaining balance but at non-member pricing if applicable

### Requirement: User can renew membership

The system SHALL allow users to renew membership to extend the validity period.

#### Scenario: Renew membership
- **WHEN** user purchases membership while already a member
- **THEN** membership_expire is extended from current expiry date
- **AND** all membership benefits are restored

### Requirement: User can enable auto-subscribe

The system SHALL allow users to enable auto-subscribe, which automatically deducts membership fee from balance when membership is about to expire.

#### Scenario: Enable auto-subscribe
- **WHEN** user enables auto-subscribe
- **AND** membership is about to expire (30 days before)
- **AND** user has sufficient balance
- **THEN** system automatically deducts membership fee on expiry date
- **AND** membership is renewed

#### Scenario: Auto-subscribe fails due to insufficient balance
- **WHEN** user has auto-subscribe enabled
- **AND** membership is about to expire
- **AND** user does not have sufficient balance
- **THEN** auto-subscribe fails
- **AND** system sends WeChat notification to the user
- **AND** user must manually renew

### Requirement: User can disable auto-subscribe

The system SHALL allow users to disable auto-subscribe at any time.

#### Scenario: Disable auto-subscribe
- **WHEN** user clicks "Disable Auto-Subscribe"
- **THEN** auto_subscribe flag is set to false
- **AND** no automatic deduction will occur on expiry