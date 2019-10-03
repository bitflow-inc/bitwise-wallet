package ai.bitflow.bitwise.wallet.gsonObjects.bitcoin;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.BitcoinAbstractResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class GetBlock extends BitcoinAbstractResponse {
  
    private Block result;

    @Data public class Block {
        // height, txcount, confirmations, time
        private String hash;
        private String miner;
        private long confirmations;
        private long height;
        private long time;
        private String[] tx;
    }
    
}
