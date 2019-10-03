package ai.bitflow.bitwise.wallet.gsonObjects.multichain;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.RpcError;
import lombok.Data;

@Data public class WalletTransaction {
  
    private int id;
    private Transaction result;
    private RpcError error;

    public WalletTransaction() {}
    public WalletTransaction(RpcError error) { this.error = error; }
    
    @Data public class Transaction {
        private Balance balance;
        private String[] myaddresses;
        private String[] addresses;
        private long confirmations;
        private boolean generated;
        private String blockhash;
        private long blocktime;
        private String txid;
        private long time;
        private long timereceived;
        private int txcount;
        private boolean valid;
    }
    
    @Data public class Balance {
        private double amount;
        private String[] assets;
    }
    
}
