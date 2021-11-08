package DSCoinPackage;

import HelperClasses.*;

public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList = new TransactionBlock[100];

  public static boolean checkTransactionBlock (TransactionBlock tB) {
    CRF a = new CRF(64);
    if (tB.previous == null) {
      if (!tB.dgst.substring(0, 4).equals("0000") || !tB.dgst.equals(a.Fn(start_string + "#" + tB.trsummary + "#" + tB.nonce))) {
        return false;
      }
    } else {
      if (!tB.dgst.substring(0, 4).equals("0000") || !tB.dgst.equals(a.Fn(tB.previous.dgst + "#" + tB.trsummary + "#" + tB.nonce))) {
        return false;
      }
    }


    MerkleTree g = new MerkleTree();
    String s = g.Build(tB.trarray);
    if (!s.equals(tB.trsummary)) {
      return false;
    }
    
    if (!tB.Tree.rootnode.val.equals(tB.trsummary)) {
	return false;
    }

    Transaction[] y = tB.trarray;
    int l = y.length;

    for (int i = 0; i < l; i++) {
      if (!tB.checkTransaction(y[i])) {
        return false;
      }
    }
    return true;
  }

  public TransactionBlock FindLongestValidChain () {
    TransactionBlock fin = null;
    int max = -1;
    int l = 0;
    while (this.lastBlocksList[l] != null) {
      l += 1;
    }
    if (l == 0) {
      return null;
    }
    for (int i = 0; i < l; i++) {
      TransactionBlock curr = this.lastBlocksList[i];
      TransactionBlock res = curr;
      int count = 0;
      while (curr.previous != null) {
        if (!checkTransactionBlock(curr)) {
          res = curr.previous;
          curr = curr.previous;
          count = 0;
        } else {
          count += 1;
          curr = curr.previous;
        }
      }
      if (count > max) {
        fin = res;
        max = count;
      }
    }

    return fin;
  }

  public void InsertBlock_Malicious (TransactionBlock newBlock) {
    TransactionBlock lastBlock = this.FindLongestValidChain();
    CRF a = new CRF(64);
    int i = 1000000001;
    newBlock.nonce = "1000000001";
    if (lastBlock == null) {
      newBlock.dgst = a.Fn(start_string + "#" + newBlock.trsummary + "#" + newBlock.nonce);
      while (!newBlock.dgst.substring(0, 4).equals("0000")) {
        i += 1;
        newBlock.nonce = String.valueOf(i);
        newBlock.dgst = a.Fn(start_string + "#" + newBlock.trsummary + "#" + newBlock.nonce);
      }
    } else {
      newBlock.dgst = a.Fn(lastBlock.dgst + "#" + newBlock.trsummary + "#" + newBlock.nonce);
      while (!newBlock.dgst.substring(0, 4).equals("0000")) {
        i += 1;
        newBlock.nonce = String.valueOf(i);
        newBlock.dgst = a.Fn(lastBlock.dgst + "#" + newBlock.trsummary + "#" + newBlock.nonce);
      }
    }
    newBlock.previous = lastBlock;
    int l = 0;
    while (this.lastBlocksList[l] != null) {
      l += 1;
    }
    int ans = 0;
    if (l != 0) {
      for (int j = 0; j < l; j++) {
        if (this.lastBlocksList[j] == lastBlock) {
          this.lastBlocksList[j] = newBlock;
          ans = 1;
          break;
        }
      }
    }
    if (ans == 0) {
      this.lastBlocksList[l] = newBlock;
    }
  }
}
