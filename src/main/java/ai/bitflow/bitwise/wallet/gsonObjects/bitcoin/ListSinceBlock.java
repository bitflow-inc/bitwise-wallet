package ai.bitflow.bitwise.wallet.gsonObjects.bitcoin;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.BitcoinAbstractResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ListSinceBlock extends BitcoinAbstractResponse {
  
    private Result result;

    @Data public class Result {
      
        private Transaction[] transactions;
        private String lastblock;
        
        @Data
        public class Transaction {
            String label;
            String address;
            String category; // send, receive
            double amount;
            int vout;
            double fee;
            int confirmations;
            String blockhash;
            int blockindex;
            long blocktime;
            String txid;
            String[] walletconflicts;
            long time;
            long timereceived;
            boolean abandoned;

            @Override
            public String toString() {
                return "[" + category + "] address " + address + " amount " + amount
                        + " fee " + fee + " txid " + txid;
            }
        }
    }
    
}
