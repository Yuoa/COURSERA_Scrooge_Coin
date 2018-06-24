# Scrooge Coin
This is a repository for the first assignment of 2018 Summer Coursera Lectures: Bitcoin and Cryptocurrency Technology. I wrote `TxHandler.java` and `MaxFeeTxHandler.java`. 95-score-achieved codes.  

## TxHandler Test
```
Running 15 tests
Test 1: test isValidTx() with valid transactions
==> passed

Test 2: test isValidTx() with transactions containing signatures of incorrect data
==> passed

Test 3: test isValidTx() with transactions containing signatures using incorrect private keys
==> passed

Test 4: test isValidTx() with transactions whose total output value exceeds total input value
==> passed

Test 5: test isValidTx() with transactions that claim outputs not in the current utxoPool
==> passed

Test 6: test isValidTx() with transactions that claim the same UTXO multiple times
==> passed

Test 7: test isValidTx() with transactions that contain a negative output value
==> passed


Test 8: test handleTransactions() with simple and valid transactions
Total Transactions = 2
Number of transactions returned valid by student = 2
Total Transactions = 50
Number of transactions returned valid by student = 50
Total Transactions = 100
Number of transactions returned valid by student = 100
==> passed

Test 9: test handleTransactions() with simple but some invalid transactions because of invalid signatures
Total Transactions = 2
Number of transactions returned valid by student = 0
Total Transactions = 50
Number of transactions returned valid by student = 2
Total Transactions = 100
Number of transactions returned valid by student = 1
==> passed

Test 10: test handleTransactions() with simple but some invalid transactions because of inputSum < outputSum
Total Transactions = 2
Number of transactions returned valid by student = 2
Total Transactions = 50
Number of transactions returned valid by student = 25
Total Transactions = 100
Number of transactions returned valid by student = 42
==> passed

Test 11: test handleTransactions() with simple and valid transactions with some double spends
Total Transactions = 2
Number of transactions returned valid by student = 1
Total Transactions = 50
Number of transactions returned valid by student = 21
Total Transactions = 100
Number of transactions returned valid by student = 43
==> passed

Test 12: test handleTransactions() with valid but some transactions are simple, some depend on other transactions
Total Transactions = 2
Number of transactions returned valid by student = 2
Total Transactions = 50
Number of transactions returned valid by student = 29
Total Transactions = 100
Number of transactions returned valid by student = 85
==> passed

Test 13: test handleTransactions() with valid and simple but some transactions take inputs from non-exisiting utxo's
Total Transactions = 2
Number of transactions returned valid by student = 1
Total Transactions = 50
Number of transactions returned valid by student = 12
Total Transactions = 100
Number of transactions returned valid by student = 57
==> passed

Test 14: test handleTransactions() with complex Transactions
Total Transactions = 2
Number of transactions returned valid by student = 2
Total Transactions = 50
Number of transactions returned valid by student = 10
Total Transactions = 100
Number of transactions returned valid by student = 30
==> passed

Test 15: test handleTransactions() with simple, valid transactions being called again to check for changes made in the pool
Total Transactions = 2
Number of transactions returned valid by student = 2
Total Transactions = 50
Number of transactions returned valid by student = 48
Total Transactions = 100
Number of transactions returned valid by student = 53
==> passed


Total:15/15 tests passed!
```

## Max Fee TxHandler Test

```
Running 1 tests

Test 1: test handleTransactions() with simple and valid transactions
Total Transactions = 2
Number of transactions returned valid by student = 1
Correct, Returned fees 0.0017396507581575746 = maximum fees
Total Transactions = 10
Number of transactions returned valid by student = 3
Correct, Returned fees 0.011991189987225814 = maximum fees
Total Transactions = 30
Number of transactions returned valid by student = 9
Returned set is not a maximum fees set of transactions
Returned fees 0.024996000645054695, maximum fees 0.03345190404025702
==> FAILED


Total:0/1 tests passed!
```
