package DSCoinPackage;

public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions;

  public void AddTransactions (Transaction transaction) {
    this.numTransactions += 1;

    if (this.firstTransaction == null) {
      this.firstTransaction = transaction;
      this.lastTransaction = transaction;
    } else {
      this.lastTransaction.next = transaction;
      this.lastTransaction = transaction;
    }
  }
  
  public Transaction RemoveTransaction () throws EmptyQueueException {
    if (this.size() == 0) {
      throw new EmptyQueueException();
    }

    this.numTransactions -= 1;

    Transaction a = this.firstTransaction;
    this.firstTransaction = this.firstTransaction.next;
    return a;
  }

  public int size() {
    return this.numTransactions;
  }
}
