package DSCoinPackage;

import HelperClasses.*;

public class Moderator
 {

  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
   String coin = "100000";
   int k = 0;
   Members mod = new Members();
   mod.UID = "Moderator";
   while (coinCount != 0) {
    Transaction q = new Transaction();
    q.coinID = coin;

    q.Source = mod;
    q.Destination = DSObj.memberlist[k % (DSObj.memberlist.length)];

    q.coinsrc_block = null;

    DSObj.pendingTransactions.AddTransactions(q);

    coinCount -= 1;
    if (coinCount == 0) {
     break;
    }
    int j = Integer.parseInt(coin);
    j += 1;
    coin = String.valueOf(j);
    k += 1;
   }
   DSObj.latestCoinID = coin;
   int i = 0;
   Transaction[] t = new Transaction[DSObj.bChain.tr_count];
   TransactionBlock tB;
   while (DSObj.pendingTransactions.size() != 0) {
    if (i != DSObj.bChain.tr_count) {
     try {
      t[i] = DSObj.pendingTransactions.RemoveTransaction();
      i += 1;
     } catch (EmptyQueueException e) {}
    } else {
     tB = new TransactionBlock(t);
     DSObj.bChain.InsertBlock_Honest(tB);
     i = 0;
     while (i != DSObj.bChain.tr_count) {
      Pair<String, TransactionBlock> p = new Pair<String, TransactionBlock>(t[i].coinID, tB);
      t[i].Destination.mycoins.add(p);
      i += 1;
     }
     t = new Transaction[DSObj.bChain.tr_count];
     i = 0;
    }
   }
   tB = new TransactionBlock(t);
   DSObj.bChain.InsertBlock_Honest(tB);
   i = 0;
   while (i != DSObj.bChain.tr_count) {
    Pair<String, TransactionBlock> p = new Pair<String, TransactionBlock>(t[i].coinID, tB);
    t[i].Destination.mycoins.add(p);
    i += 1;
   }
  }
    
  public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
   String coin = "100000";
   int k = 0;
   Members mod = new Members();
   mod.UID = "Moderator";
   while (coinCount != 0) {
    Transaction q = new Transaction();
    q.coinID = coin;

    q.Source = mod;
    q.Destination = DSObj.memberlist[k % (DSObj.memberlist.length)];

    q.coinsrc_block = null;

    DSObj.pendingTransactions.AddTransactions(q);

    coinCount -= 1;
    if (coinCount == 0) {
     break;
    }
    int j = Integer.parseInt(coin);
    j += 1;
    coin = String.valueOf(j);
    k += 1;
   }
   DSObj.latestCoinID = coin;
   int i = 0;
   Transaction[] t = new Transaction[DSObj.bChain.tr_count];
   TransactionBlock tB;
   while (DSObj.pendingTransactions.size() != 0) {
    if (i != DSObj.bChain.tr_count) {
     try {
      t[i] = DSObj.pendingTransactions.RemoveTransaction();
      i += 1;
     } catch (EmptyQueueException e) {}
    } else {
     tB = new TransactionBlock(t);
     DSObj.bChain.InsertBlock_Malicious(tB);
     i = 0;
     while (i != DSObj.bChain.tr_count) {
      Pair<String, TransactionBlock> p = new Pair<String, TransactionBlock>(t[i].coinID, tB);
      t[i].Destination.mycoins.add(p);
      i += 1;
     }
     t = new Transaction[DSObj.bChain.tr_count];
     i = 0;
    }
   }
   tB = new TransactionBlock(t);
   DSObj.bChain.InsertBlock_Malicious(tB);
   i = 0;
   while (i != DSObj.bChain.tr_count) {
    Pair<String, TransactionBlock> p = new Pair<String, TransactionBlock>(t[i].coinID, tB);
    t[i].Destination.mycoins.add(p);
    i += 1;
   }
  }
}
