## ADDED Requirements

### Requirement: User account has dual balance structure

The system SHALL maintain two independent balances for each user: recharge_balance and rebate_balance.

#### Scenario: View dual balances
- **WHEN** user checks account balance
- **THEN** system displays recharge_balance separately from rebate_balance
- **AND** recharge_balance shows“可提现/可退款” label
- **AND** rebate_balance shows“仅限消费/不可提现” label

### Requirement: Recharge balance can be withdrawn and refunded

The system SHALL allow users to withdraw recharge balance to WeChat or refund to account balance.

#### Scenario: Withdraw recharge balance
- **WHEN** user requests withdrawal
- **THEN** system creates withdrawal application
- **AND** system notifies merchant for confirmation
- **AND** upon merchant confirmation, system processes payment to user's WeChat

#### Scenario: Refund recharge balance
- **WHEN** user requests refund to balance
- **THEN** system adds the amount to user's recharge_balance
- **AND** transaction is recorded

### Requirement: Rebate balance can only be used for consumption

The system SHALL restrict rebate balance to only be used for purchasing goods and membership.

#### Scenario: Consume using rebate balance
- **WHEN** user has insufficient recharge balance
- **THEN** system deducts remaining amount from rebate balance
- **AND** transaction is recorded

#### Scenario: Rebate balance withdrawal is prohibited
- **WHEN** user attempts to withdraw rebate balance
- **THEN** system rejects the request
- **AND** system displays message “返利余额不可提现”

### Requirement: Consumption priority rule

The system SHALL always deduct from recharge_balance first, and only deduct from rebate_balance when recharge_balance is insufficient.

#### Scenario: Consume with sufficient recharge balance
- **WHEN** user has recharge_balance >= consumption amount
- **THEN** only recharge_balance is deducted

#### Scenario: Consume with insufficient recharge balance
- **WHEN** user has recharge_balance < consumption amount
- **THEN** recharge_balance is deducted to zero
- **AND** remaining amount is deducted from rebate_balance

### Requirement: Balance operations are transactional

The system SHALL ensure all balance modifications are atomic and consistent using database transactions.

#### Scenario: Transaction rollback on failure
- **WHEN** balance modification operation fails midway
- **THEN** all changes are rolled back
- **AND** no partial deduction occurs

### Requirement: Rebate balance debt tracking

The system SHALL track debt when rebate balance goes negative due to refundclawback.

#### Scenario: Track negative rebate balance
- **WHEN** refundclawback occurs and user has insufficient rebate_balance
- **THEN** rebate_balance becomes negative (debt)
- **AND** subsequent consumption deducts from rebate_balance first to recover debt