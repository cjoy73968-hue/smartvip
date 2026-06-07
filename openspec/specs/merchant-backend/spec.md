## Purpose

TBD - Document the merchant backend purpose and scope.

## Requirements

### Requirement: Merchant can view and manage member list

The system SHALL allow merchant to view all members with detailed information and perform management actions.

#### Scenario: View member list
- **WHEN** merchant opens member management page
- **THEN** system displays list of all members with: nickname, phone, registration date, membership expiry, recharge balance, rebate balance, referrer, referral count

#### Scenario: Search and filter members
- **WHEN** merchant applies filters (registration date range, balance range, expiry status, referral status)
- **THEN** system displays matching members

#### Scenario: Edit member information
- **WHEN** merchant modifies member remark, adjusts membership expiry, adds/deducts balance, freezes/unfreezes account
- **THEN** system updates the member record
- **AND** action is logged

### Requirement: Merchant can manage orders and deliveries

The system SHALL allow merchant to view all orders and confirm delivery completion.

#### Scenario: View delivery orders
- **WHEN** merchant opens order management page
- **THEN** system displays all orders with type (consumption/periodic delivery/one-time delivery), status, user info, amount, gift details

#### Scenario: Confirm delivery
- **WHEN** merchant clicks "Confirm Delivery" on an order
- **THEN** order status changes to "completed"
- **AND** 7-day timer starts for rebate credit

### Requirement: Merchant can manage products/gifts

The system SHALL allow merchant to create, edit, delete, and manage product/gift inventory.

#### Scenario: Add new product
- **WHEN** merchant enters product details (name, price, stock, images, description)
- **AND** merchant sets whether it is the default gift
- **AND** merchant sets whether it is on shelf
- **THEN** system creates the product record

#### Scenario: Manage inventory
- **WHEN** merchant views product inventory
- **THEN** system displays current stock levels
- **AND** system alerts when stock is below threshold

#### Scenario: Handle low inventory
- **WHEN** product stock is insufficient for periodic delivery
- **THEN** system automatically switches to default gift (if alternative exists)
- **OR** system pauses delivery and alerts merchant

### Requirement: Merchant can configure rebate rate

The system SHALL allow merchant to set and modify the rebate percentage for referrals.

#### Scenario: Set rebate rate
- **WHEN** merchant enters rebate percentage
- **THEN** system saves the rate
- **AND** rate takes effect for subsequent consumptions

#### Scenario: Modify rebate rate
- **WHEN** merchant changes rebate rate
- **THEN** new rate applies to all subsequent consumptions
- **AND** existing pending rebates are not affected

### Requirement: Merchant can configure membership settings

The system SHALL allow merchant to set membership price and default validity period.

#### Scenario: Set membership price
- **WHEN** merchant enters membership price
- **THEN** system saves the price
- **AND** price takes effect immediately for new purchases

#### Scenario: Set default membership days
- **WHEN** merchant enters default membership validity days
- **THEN** system saves the configuration
- **AND** new memberships use this duration

### Requirement: Merchant can configure gift delivery settings

The system SHALL allow merchant to set default gift and delivery cycle.

#### Scenario: Set default gift
- **WHEN** merchant selects a product as default gift
- **THEN** system saves the selection
- **AND** periodic deliveries use this gift when user balance is insufficient

#### Scenario: Set delivery cycle
- **WHEN** merchant configures delivery cycle (daily/weekly/monthly/quarterly)
- **AND** merchant sets delivery time
- **THEN** system saves the configuration
- **AND** deliveries are triggered according to schedule

### Requirement: Merchant can configure delivery city

The system SHALL allow merchant to set cities available for delivery.

#### Scenario: Set delivery cities
- **WHEN** merchant selects available cities for delivery
- **THEN** system saves the configuration
- **AND** users outside these cities see self-pickup option

### Requirement: Merchant can process withdrawal requests

The system SHALL allow merchant to view and process user withdrawal requests.

#### Scenario: View withdrawal requests
- **WHEN** merchant opens withdrawal management page
- **THEN** system displays pending withdrawal requests with amount, user info, time

#### Scenario: Process withdrawal
- **WHEN** merchant confirms a withdrawal request
- **AND** merchant completes the payment to user
- **THEN** system updates withdrawal status to "completed"

### Requirement: Merchant can view data reports

The system SHALL generate and display data reports for membership, transactions, and operations.

#### Scenario: View membership report
- **WHEN** merchant opens membership report
- **THEN** system displays: new members, active members, expired members, referral stats

#### Scenario: View transaction report
- **WHEN** merchant opens transaction report
- **THEN** system displays: total recharge amount, total consumption amount, total rebate paid, order count

#### Scenario: View delivery report
- **WHEN** merchant opens delivery report
- **THEN** system displays: total deliveries, price difference collected, default gift substitution count

#### Scenario: Export data
- **WHEN** merchant clicks export
- **THEN** system generates downloadable report file

### Requirement: Merchant can send marketing messages

The system SHALL allow merchant to create and send WeChat template messages to users.

#### Scenario: Send to all members
- **WHEN** merchant composes a message and selects "send to all"
- **THEN** system sends WeChat template message to all opted-in members

#### Scenario: Send to selected group
- **WHEN** merchant selects specific members
- **THEN** system sends message only to selected members

#### Scenario: Send to single member
- **WHEN** merchant selects a specific member
- **THEN** system sends message only to that member

### Requirement: Merchant can manage sub-accounts

The system SHALL allow merchant to create sub-accounts with different permission levels.

#### Scenario: Create sub-account
- **WHEN** merchant creates new sub-account with username and password
- **AND** merchant assigns permissions to the sub-account
- **THEN** sub-account can log in with configured credentials

#### Scenario: Set sub-account permissions
- **WHEN** merchant modifies sub-account permissions
- **THEN** new permissions take effect on next login