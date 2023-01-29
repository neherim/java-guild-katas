# Money transfer kata

## Business requirements

- Money can only be transferred between two registered accounts.
- The Complience department wants to do manual checks on every money transfer. So money must be transferred in two steps:
  1. Reserve the required amount of money on the first account.
  2. Wait until the transfer request passes all manual checks.
  3. Remove reservation from the step 1 and transfer money between accounts.
- Money can be reserved on the account for different transfer requests at the same time.

## Implementation requirements
You need to implement the `MoneyTransferService` class with two methods:
- `reserveMoney(Long operationId, Long accountId, Integer amount)` - to reserve money in the source account before the actual transfer
- `transferMoney(Long operationId, Long fromAccountId, Long toAccountId)` - to remove a reservation and make a transfer between two accounts

## Solutions
- [money-transfer-anemic](money-transfer-anemic/README.md) layered architecture with anemic domain model pattern
- [money-transfer-rich](money-transfer-rich/README.md) onion architecture with rich domain model
