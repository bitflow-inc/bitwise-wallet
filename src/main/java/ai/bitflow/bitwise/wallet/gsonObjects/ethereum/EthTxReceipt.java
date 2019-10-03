package ai.bitflow.bitwise.wallet.gsonObjects.ethereum;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.RpcError;
import lombok.Data;

@Data public class EthTxReceipt {
  
    private int id;
    private TxReceipt result;
    private RpcError error;
    
    public EthTxReceipt(RpcError error) { this.error = error; }
  
    @Data public static class TxReceipt {
        private String blockHash;
        private String blockNumber;
        private String contractAddress;
        private String cumulativeGasUsed;
        private String from;
        private String gasUsed;
        private String status; // 0x1 = success, 이 값은 무시하여야 한다고... https://medium.com/hexlant/해피데스데이-토큰은-그-곳에-있지-않았다-ba280c52d2b9
        private String to;
        private String transactionHash;
        private String transactionIndex;
    }
	
}
