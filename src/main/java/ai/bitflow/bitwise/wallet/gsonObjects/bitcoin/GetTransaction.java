package ai.bitflow.bitwise.wallet.gsonObjects.bitcoin;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.BitcoinAbstractResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.RpcError;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GetTransaction extends BitcoinAbstractResponse {
  
    private Result result;

    public GetTransaction() {}
    public GetTransaction(RpcError error) { setError(error); }
    
	@Data public class Result {

        private String txid;
        private int confirmations;
        private double amount;
        private double fee;
        private boolean abandoned;
        private String blockhash;
        private long time;

        private int blockindex;
        private String comment;
        private String hex;
        private Detail[] details;
        private long timereceived;
        private long blocktime;

        private char status;
        
        public Result() { }
        
	}
	
	@Data public static class Detail {
        private int vout;
        private double amount;
        private String account;
        private String address;
        private String category;
        private String label;
        private double fee;
        private boolean abandoned;
        public Detail() { }
    }

}
