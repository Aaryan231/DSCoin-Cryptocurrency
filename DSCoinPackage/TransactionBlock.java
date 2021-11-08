package DSCoinPackage;

import HelperClasses.MerkleTree;
import HelperClasses.CRF;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;

  TransactionBlock(Transaction[] t) {
    Transaction[] a = new Transaction[t.length];
    for (int i = 0; i < t.length; i++) {
 	a[i] = t[i];
    }
    this.trarray = a;
    this.previous = null;
    MerkleTree tree = new MerkleTree();
    String s = tree.Build(t);
    this.Tree = tree;
    this.trsummary = s;
    this.dgst = null;
  }

  public boolean checkTransaction (Transaction t) {
    TransactionBlock a = t.coinsrc_block;
    TransactionBlock curr = this;
    while (curr != a) {
      boolean temp = false;
      for (int i = 0; i < curr.trarray.length; i++) {
        if (curr.trarray[i].coinID.equals(t.coinID)) {
          if (temp == false) {
		      temp = true;
	      } else {
		      return false;
	      }
        }
      }
      curr = curr.previous;
    }
    if (a == null) {
      return true;
    }
    for (int j = 0; j < a.trarray.length; j++) {
      if (a.trarray[j].coinID.equals(t.coinID) && a.trarray[j].Destination == t.Source) {
        return true;
      }
    }
    return false;
  }
}
