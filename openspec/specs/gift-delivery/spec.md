## Purpose

TBD - Document the gift delivery system purpose and scope.

## Requirements

### Requirement: System supports two gift delivery modes

The system SHALL support two independent gift delivery modes: periodic automatic delivery (member benefit) and one-time order delivery.

#### Scenario: Periodic automatic delivery (差价模式)
- **WHEN** merchant has configured default gift and delivery cycle
- **AND** user is an active member
- **AND** delivery time arrives based on configured cycle
- **THEN** system calculates price_diff = user's selected gift price - default gift price
- **AND** if price_diff <= 0, user pays nothing and default gift is delivered
- **AND** if price_diff > 0 AND user has sufficient balance, user pays price_diff and selected gift is delivered
- **AND** if price_diff > 0 AND user has insufficient balance, default gift is delivered and user is notified

#### Scenario: One-time order delivery (全额模式)
- **WHEN** user selects a product for one-time delivery
- **AND** user pays the full product price
- **THEN** system creates a one-time delivery order
- **AND** order is independent from periodic delivery

### Requirement: Delivery is only available within the same city

The system SHALL restrict delivery to users within the merchant's configured delivery city.

#### Scenario: Check delivery availability
- **WHEN** user enters shipping address
- **THEN** system checks if the city is within merchant's delivery configuration
- **AND** if not deliverable, system displays message about self-pickup option

#### Scenario: Continue order for non-deliverable address
- **WHEN** user is in a non-deliverable area
- **THEN** user can choose to pick up in person
- **AND** order can proceed with self-pickup delivery type

### Requirement: Delivery is free within city

The system SHALL not charge additional delivery fees for orders within the delivery city.

### Requirement: Merchant confirms delivery completion

The system SHALL require merchant to click "Confirm Delivery" to complete the delivery fulfillment.

#### Scenario: Merchant confirms delivery
- **WHEN** merchant delivers the gift to the user address
- **AND** merchant clicks "Confirm Delivery"
- **THEN** order status changes to "completed"
- **AND** 7-day timer starts for rebate credit

### Requirement: Gift price change handling for pending orders

The system SHALL lock the price for already-generated periodic delivery orders.

#### Scenario: Price change for pending order
- **WHEN** merchant changes gift price after an order is generated
- **THEN** the pending order maintains original price
- **AND** next cycle uses new price

### Requirement: User can change periodic gift selection

The system SHALL allow users to change their selected periodic gift, with effective timing based on delivery status.

#### Scenario: Change gift before delivery
- **WHEN** user changes selected gift
- **AND** current cycle has not yet been delivered
- **THEN** new selection takes effect immediately for current cycle

#### Scenario: Change gift after delivery
- **WHEN** user changes selected gift
- **AND** current cycle has already been delivered
- **THEN** new selection takes effect from next cycle

### Requirement: Periodic delivery order cannot be cancelled

The system SHALL not allow cancellation of periodic delivery orders once generated.

#### Scenario: Cannot cancel periodic delivery
- **WHEN** user attempts to cancel a periodic delivery order
- **THEN** system rejects the cancellation request
- **AND** no refund is processed

### Requirement: One-time delivery can be cancelled before shipping

The system SHALL allow cancellation of one-time delivery orders before merchant confirms shipping.

#### Scenario: Cancel before shipping
- **WHEN** user cancels one-time delivery order
- **AND** merchant has not yet confirmed delivery
- **THEN** order is cancelled
- **AND** refund is processed

#### Scenario: Cannot cancel after shipping
- **WHEN** user attempts to cancel one-time delivery order
- **AND** merchant has already confirmed delivery
- **THEN** cancellation is rejected