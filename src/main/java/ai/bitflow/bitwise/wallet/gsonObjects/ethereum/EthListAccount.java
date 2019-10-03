package ai.bitflow.bitwise.wallet.gsonObjects.ethereum;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.RpcError;
import lombok.Data;

@Data public class EthListAccount implements Cloneable {
  
    private int id;
    private RpcError error;
    private String[] result;
    
}
