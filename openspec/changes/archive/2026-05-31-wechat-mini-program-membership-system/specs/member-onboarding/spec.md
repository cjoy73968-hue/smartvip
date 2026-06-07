## ADDED Requirements

### Requirement: User can register via mini-program QR code

The system SHALL allow users to scan a merchant's mini-program QR code and complete registration as a member.

#### Scenario: Successful registration via QR code
- **WHEN** user scans the mini-program QR code
- **THEN** system redirects to the registration flow
- **AND** system obtains user's openid, nickname, and avatar via WeChat authorization
- **AND** system prompts user to bind phone number
- **AND** system prompts user to complete shipping address
- **AND** system creates the user account with referrer info if applicable
- **AND** system generates a unique referral code for the user

#### Scenario: Registration via QR code with referrer
- **WHEN** user scans a QR code containing a referrer's referral code
- **THEN** system binds the user to the referrer
- **AND** the referral relationship becomes permanent and cannot be changed

### Requirement: User can register via official account redirect

The system SHALL allow users who are in or follow the merchant's official account to register via the account's menu or link.

#### Scenario: Successful registration via official account
- **WHEN** user clicks the registration link in the official account menu
- **THEN** system redirects to the mini-program registration flow
- **AND** system obtains user's openid, nickname, and avatar via WeChat authorization

### Requirement: User can bind phone number

The system SHALL require users to bind a phone number during registration for login and notifications.

#### Scenario: Successful phone binding
- **WHEN** user enters a valid phone number
- **THEN** system sends verification code via SMS
- **AND** user enters correct verification code
- **AND** system binds the phone number to the user account

### Requirement: User can manage shipping addresses

The system SHALL allow users to add, edit, delete, and set default shipping addresses.

#### Scenario: Add new address
- **WHEN** user enters address details (name, phone, province, city, district, detail)
- **THEN** system validates the address format
- **AND** system saves the address for the user

#### Scenario: Set default address
- **WHEN** user selects an address and clicks "Set as Default"
- **THEN** system marks that address as the default address
- **AND** other addresses are unmarked as default

#### Scenario: Delete address
- **WHEN** user clicks delete on an address
- **THEN** system removes the address from the user's address list

#### Scenario: Edit address
- **WHEN** user modifies an existing address
- **THEN** system updates the address details

### Requirement: User can view membership price and benefits

The system SHALL display the membership price, benefits description, and gift information clearly before purchase.

#### Scenario: View membership details
- **WHEN** user logs in to the mini-program
- **THEN** system displays the current membership price
- **AND** system displays the benefits including periodic gift delivery and referral rebate
- **AND** system displays the default gift information

### Requirement: Referral relationship is permanent

The system SHALL ensure that once a referral relationship is established, it cannot be changed or unlinked.

#### Scenario: Referral relationship binding
- **WHEN** user completes registration with a referrer's code
- **THEN** the relationship is permanently recorded
- **AND** subsequent changes to registration do not affect the relationship
- **AND** the referrer can view referral statistics but not referree personal information