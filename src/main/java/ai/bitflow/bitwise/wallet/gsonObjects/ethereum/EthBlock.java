package ai.bitflow.bitwise.wallet.gsonObjects.ethereum;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.RpcError;
import lombok.Data;

@Data public class EthBlock {
  
    private int id;
    private RpcError error;
    private Result result;
    
    @Data
    public class Result {
        private String gasLimit;
        private String gasUsed;
        private String hash;
        private String number;
        private String timestamp;
        private EthTransaction[] transactions;
    }
    
}
