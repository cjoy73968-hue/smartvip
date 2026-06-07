## Purpose

TBD - Document the referral and rebate system purpose and scope.

## Requirements

### Requirement: Each member has a unique referral code

The system SHALL generate a unique referral code (alphanumeric) for each member upon registration.

#### Scenario: Generate referral code
- **WHEN** user completes registration
- **THEN** system generates a unique referral code
- **AND** the code is associated with the user's account
- **AND** user can view and share their referral code

### Requirement: User can generate referral promotional materials

The system SHALL allow users to generate promotional posters and sharing links using their referral code.

#### Scenario: Generate promotional poster
- **WHEN** user requests to generate a promotional poster
- **THEN** system creates a poster image containing the user's referral code
- **AND** the poster is downloadable or shareable

#### Scenario: Share referral link
- **WHEN** user clicks share referral link
- **THEN** system generates a link containing the referral code
- **AND** the link can be shared via WeChat

### Requirement: Referral relationship is permanently bound

The system SHALL permanently bind referrer and referree once the relationship is established during registration.

#### Scenario: Bind referral relationship
- **WHEN** new user registers with a referral code
- **THEN** referrer_id is stored in the new user's record
- **AND** the relationship cannot be changed or unlinked

### Requirement: Rebate is calculated based on merchant-configured rate

The system SHALL calculate rebate amount when referree makes a consumption, using the rate configured by merchant.

#### Scenario: Calculate rebate on consumption
- **WHEN** referree completes a consumption order
- **AND** order status becomes completed (delivered, no refund)
- **THEN** system calculates rebate = consumption_amount × rebate_rate
- **AND** rebate is recorded as pending (to be delivered after 7 days)

### Requirement: Rebate is delivered 7 days after delivery confirmation

The system SHALL credit rebate to referrer's rebate_balance 7 days after the order is confirmed as delivered, provided no refund occurred.

#### Scenario: Deliver rebate after 7-day period
- **WHEN** referree's order is confirmed delivered at time T
- **AND** no refund has occurred by T + 7 days
- **THEN** system credits rebate_amount to referrer's rebate_balance
- **AND** system updates rebate record status to "delivered"

#### Scenario: Rebate not delivered if refund occurs
- **WHEN** referree's order is refunded before 7 days
- **THEN** no rebate is credited to referrer
- **AND** rebate record is marked as "cancelled"

### Requirement: Rebate clawback for refunded orders

The system SHALL automatically clawback rebate from referrer if a refund occurs after rebate has been delivered.

#### Scenario: Clawback delivered rebate
- **WHEN** referree's delivered order is refunded
- **AND** rebate has already been credited to referrer
- **THEN** system deducts the rebate amount from referrer's rebate_balance
- **AND** if rebate_balance is insufficient, the amount becomes debt (negative balance)

### Requirement: Rebate is stopped when membership expires

The system SHALL immediately stop accruing rebate when referree's membership expires.

#### Scenario: Stop rebate on membership expiry
- **WHEN** referree's membership_expire time is reached
- **THEN** referrer no longer receives rebate from referree's subsequent consumption
- **AND** this takes effect immediately, not at end of billing cycle

### Requirement: Referrer can view rebate statistics without referree details

The system SHALL display referral statistics including number of referred members, total rebate earned, and pending rebate, but NOT expose referree personal information.

#### Scenario: View rebate statistics
- **WHEN** referrer views their rebate page
- **THEN** system displays: number of referred members, total rebate earned, pending rebate
- **AND** system does NOT show referree names, phone numbers, or other personal details
- **AND** rebate records show only amount and date, not order details that could identify referree