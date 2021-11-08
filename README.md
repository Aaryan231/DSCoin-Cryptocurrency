# DSCoin-Cryptocurrency
* Implementation Blockchains &amp; Merkle Trees for tamper-proof and secure storage of data using DSA in Java.
* Has code for 2 different cases in one of which the miner of the cryptocurrency is honest and other in which the miner is not honest.


## DSCoinPackage
* Consist of 11 java files as follows:
* _**Transaction**_ - A class which creates a transaction object and stores its attributes.
* _**Transaction Queue**_ - Maintains a queue of all the pending transactions to be completed by the miner and includes methods to add and remove transactions from the queue.
* _**Transaction Block**_ - Used to a create a block and Merkle Trees out of an array of transactions.
* _**BlockChain_Honest**_ - Used to maintain a block chain out of the Transaction Blocks and contains method to add blocks for the honest case.
* _**BlockChain_Malicious**_ - Used to maintain a block chain out of the Transaction Blocks and contains method to add blocks for the malicious case.
* _**DSCoin_Honest**_ - The main class which maintains and stores the overall structure of the entire cryptocurrency for the honest case.
* _**DSCoin_Malicious**_ - The main class which maintains and stores the overall structure of the entire cryptocurrency for the malicious case.
* _**Members**_ - The class used to define a member who uses this cryptocurrency and it contains methods that send, mine and recieve a coin from some other user/member.
* _**Moderator**_ - This class creates a moderator object which initially distributes some fixed amount of coins amongst all members of the "DS Coin".
* _**EmptyQueueException**_ - Exception class which handles the exception in case the Transaction Queue doesn't contain any transactions.
* _**MissingTransactionException**_ - Exception class which handles the exception in case a particular transaction is not found in the entire blockchain.

## HelperClasses
* Consist of 6 java files _**Conversion, CRF, MerkleTree, Pair, sha256**_ and _**TreeNode**_.
* These files help in computing the CRF of a string, building a tree for storage of data in a Transaction Block etc.

### HOW TO RUN THE ENTIRE PROGRAM:
Create a java file which contains the main method and initialize either the DSCoin_Honest object or the DSCoin_Malicious object, Moderator and Members to distribute a fixed amount of coins amongst the members. After that do the required transactions using all the given classes to get the final result.
