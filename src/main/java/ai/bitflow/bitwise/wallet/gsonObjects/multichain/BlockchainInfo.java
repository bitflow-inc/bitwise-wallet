package ai.bitflow.bitwise.wallet.gsonObjects.multichain;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.RpcError;
import lombok.Data;

@Data public class BlockchainInfo {
  
    private int id;
    private Result result;
    private RpcError error;

    @Data public class Result {
        private long blocks;
        private long headers;
        private String bestblockhash;
    }
    
}
