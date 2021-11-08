package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;

  public void InsertBlock_Honest (TransactionBlock newBlock) {
    CRF a = new CRF(64);
    int i = 1000000001;
    newBlock.nonce = "1000000001";
    if (this.lastBlock == null) {
      newBlock.dgst = a.Fn(start_string + "#" + newBlock.trsummary + "#" + newBlock.nonce);
      while (!newBlock.dgst.substring(0, 4).equals("0000")) {
        i += 1;
        newBlock.nonce = String.valueOf(i);
        newBlock.dgst = a.Fn(start_string + "#" + newBlock.trsummary + "#" + newBlock.nonce);
      }
    } else {
      newBlock.dgst = a.Fn(this.lastBlock.dgst + "#" + newBlock.trsummary + "#" + newBlock.nonce);
      while (!newBlock.dgst.substring(0, 4).equals("0000")) {
        i += 1;
        newBlock.nonce = String.valueOf(i);
        newBlock.dgst = a.Fn(this.lastBlock.dgst + "#" + newBlock.trsummary + "#" + newBlock.nonce);
      }
    }
    newBlock.previous = this.lastBlock;
    this.lastBlock = newBlock;
  }
}
