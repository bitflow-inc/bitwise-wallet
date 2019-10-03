package ai.bitflow.bitwise.wallet.gsonObjects.ethereum;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.RpcError;
import lombok.Data;

@Data public class EthTransactionResponse {
  
    private int id;
    private EthTransaction result;
    private RpcError error;
    
    public EthTransactionResponse(RpcError error) { this.error = error; }
  
}
