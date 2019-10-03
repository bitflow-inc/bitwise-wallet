package ai.bitflow.bitwise.wallet.gsonObjects.ethereum;

import lombok.Data;

@Data public class EthTransaction {
  
    // 1) for eth_getTransactionByHash
    String blockHash;
    String blockNumber;
    String from;
    String to;
    String hash;
    String input;
    String value; // BigNumber
    String gas;
    String gasPrice;
	
}
