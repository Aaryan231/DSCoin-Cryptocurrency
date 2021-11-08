package DSCoinPackage;

import java.util.*;
import HelperClasses.*;

public class Members
 {

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins = new ArrayList<Pair<String, TransactionBlock>>();
  public Transaction[] in_process_trans = new Transaction[100];

  // Added method to check the validity of Transaction in Honest Case
  public static boolean checkValidity(DSCoin_Honest DSObj, Transaction t, Transaction[] arr) {
   TransactionBlock tB = t.coinsrc_block;
   boolean a = false;
   if (tB != null) {
    for (int i =0; i < tB.trarray.length; i++) {
     Transaction tf = tB.trarray[i];
     if (tf.coinID.equals(t.coinID) && tf.Destination == t.Source) {
      a = true;
     }
    }
   }

   boolean b = true;
   for (int j = 0; j < arr.length; j++) {
    if (arr[j] == null) {
     break;
    } else if (arr[j].coinID.equals(t.coinID)) {
     b = false;
     break;
    }
   }

   boolean c = true;
   TransactionBlock curr = DSObj.bChain.lastBlock;
   while (curr != tB) {
    for (int k = 0; k < curr.trarray.length; k++) {
     if (curr.trarray[k].coinID.equals(t.coinID)) {
      c = false;
      break;
     }
    }
    if (c == false) {
     break;
    }
    if (c == true) {
     curr = curr.previous;
    }
   }

   return (a && b && c);
  }

  // Added method to check the validity of Transaction in Malicious Case
  public static boolean checkValidity(DSCoin_Malicious DSObj, Transaction t, Transaction[] arr) {
   TransactionBlock tB = t.coinsrc_block;
   boolean a = false;
   if (tB != null) {
    for (int i =0; i < tB.trarray.length; i++) {
     Transaction tf = tB.trarray[i];
     if (tf.coinID.equals(t.coinID) && tf.Destination == t.Source) {
      a = true;
     }
    }
   }

   boolean b = true;
   for (int j = 0; j < arr.length; j++) {
    if (arr[j] == null) {
     break;
    } else if (arr[j].coinID.equals(t.coinID)) {
     b = false;
     break;
    }
   }

   boolean c = true;
   TransactionBlock curr = DSObj.bChain.FindLongestValidChain();
   while (curr != tB) {
    for (int k = 0; k < curr.trarray.length; k++) {
     if (curr.trarray[k].coinID.equals(t.coinID)) {
      c = false;
      break;
     }
    }
    if (curr == null && tB != null) {
     c = false;
    }
    if (c == false) {
     break;
    }
    if (c == true) {
     curr = curr.previous;
    }
   }

   return (a && b && c);
  }

  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
   Pair<String, TransactionBlock> p = mycoins.remove(0);
   Transaction tobj = new Transaction();
   tobj.coinID = p.get_first();
   tobj.coinsrc_block = p.get_second();
   tobj.Source = this;
   Members[] m = DSobj.memberlist;
   for (int i = 0; i < m.length; i++) {
    if (m[i].UID.equals(destUID)) {
     tobj.Destination = m[i];
     break;
    }
   }
   int i = 0;
   while (this.in_process_trans[i] != null) {
    i += 1;
   }
   this.in_process_trans[i] = tobj;
   DSobj.pendingTransactions.AddTransactions(tobj);
  }

  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
   TransactionBlock curr = DSObj.bChain.lastBlock;
   int k = 0;
   int ans = 0;
   while (curr != null) {
    for (int i = 0; i < curr.trarray.length; i++) {
     if (curr.trarray[i] == tobj) {
      k = i;
      ans = 1;
      break;
     }
    }
    if (ans == 1) {
     break;
    }
    curr = curr.previous;
   }
   if (curr == null) {
    throw new MissingTransactionException();
   }
   MerkleTree tree = curr.Tree;
   TreeNode node = tree.rootnode;
   int l = curr.trarray.length / 2;
   while (node.left != null) {
    if (k < l) {
     node = node.left;
    } else {
     node = node.right;
    }
    k = k % l;
    l = l / 2;
   }

   ArrayList<Pair<String, String>> ls1 = new ArrayList<Pair<String, String>>();
   while (node.parent != null) {
    Pair<String, String> p;
    String a;
    String b;
    if (node == node.parent.left) {
     a = node.val;
     b = node.parent.right.val;
    } else {
     a = node.parent.left.val;
     b = node.val;
    }
    p = new Pair<String, String>(a, b);
    ls1.add(p);
    node = node.parent;
   }
   Pair<String, String> q = new Pair<String, String>(node.val, null);
   ls1.add(q);

   ArrayList<Pair<String, String>> ls2 = new ArrayList<Pair<String, String>>();
   TransactionBlock end = DSObj.bChain.lastBlock;
   String a;
   String b;
   while (end != curr.previous) {
    Pair<String, String> w;
    a = end.dgst;
    b = end.previous.dgst + "#" + end.trsummary + "#" + end.nonce;
    w = new Pair<String, String>(a, b);
    ls2.add(0, w);
    end = end.previous;
   }
   Pair<String, String> x = new Pair<String, String>(end.dgst, null);
   ls2.add(0, x);

   Pair<List<Pair<String, String>>, List<Pair<String, String>>> result = new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(ls1, ls2);

   int m = 0;
   while (this.in_process_trans[m] != tobj) {
    m += 1;
   }
   this.in_process_trans[m] = null;

   TransactionBlock d = DSObj.bChain.lastBlock;
   int temp = 0;
   while (d != null) {
    for (int z = 0; z < d.trarray.length; z++) {
     if (d.trarray[z].coinID.equals(tobj.coinID)) {
      temp = 1;
      break;
     }
    }
    if (temp == 1) {
     break;
    } else {
     d = d.previous;
    }
   }

   Pair<String, TransactionBlock> e = new Pair<String, TransactionBlock>(tobj.coinID, d);
   int v = 0;
   if (tobj.Destination.mycoins.size() != 0) {
    while (tobj.Destination.mycoins.get(v).get_first().compareTo(e.get_first()) < 0) {
     v += 1;
     if (v == tobj.Destination.mycoins.size()) {
      break;
     }
    }
   }
   tobj.Destination.mycoins.add(v, e);

   return result;
  }

  public void MineCoin(DSCoin_Honest DSObj) {
   int l = DSObj.bChain.tr_count - 1;
   Transaction[] t = new Transaction[DSObj.bChain.tr_count];
   int i = 0;
   while (l != 0) {
    try {
     Transaction y = DSObj.pendingTransactions.RemoveTransaction();
     if (checkValidity(DSObj, y, t)) {
      t[i] = y;

      i += 1;
      l -= 1;
     }
    } catch (EmptyQueueException e) {}
   }

   int y = Integer.parseInt(DSObj.latestCoinID);
   y += 1;
   String s = String.valueOf(y);
   DSObj.latestCoinID = s;

   Transaction fin = new Transaction();
   fin.coinID = DSObj.latestCoinID;
   fin.Source = null;
   fin.Destination = this;
   fin.coinsrc_block = null;

   t[DSObj.bChain.tr_count - 1] = fin;
   TransactionBlock tB = new TransactionBlock(t);
   DSObj.bChain.InsertBlock_Honest(tB);

   Pair<String, TransactionBlock> p = new Pair<String, TransactionBlock>(DSObj.latestCoinID, tB);
   this.mycoins.add(p);
  }  

  public void MineCoin(DSCoin_Malicious DSObj) {
   int l = DSObj.bChain.tr_count - 1;
   Transaction[] t = new Transaction[DSObj.bChain.tr_count];
   int i = 0;
   while (l != 0) {
    try {
     Transaction y = DSObj.pendingTransactions.RemoveTransaction();
     if (checkValidity(DSObj, y, t)) {
      t[i] = y;

      i += 1;
      l -= 1;
     }
    } catch (EmptyQueueException e) {}
   }

   int y = Integer.parseInt(DSObj.latestCoinID);
   y += 1;
   String s = String.valueOf(y);
   DSObj.latestCoinID = s;

   Transaction fin = new Transaction();
   fin.coinID = DSObj.latestCoinID;
   fin.Source = null;
   fin.Destination = this;
   fin.coinsrc_block = null;

   t[DSObj.bChain.tr_count - 1] = fin;
   TransactionBlock tB = new TransactionBlock(t);
   DSObj.bChain.InsertBlock_Malicious(tB);

   Pair<String, TransactionBlock> p = new Pair<String, TransactionBlock>(DSObj.latestCoinID, tB);
   this.mycoins.add(p);
  }  
}
