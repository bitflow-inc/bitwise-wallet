package ai.bitflow.bitwise.wallet.gsonObjects.ethereum;

import lombok.Data;

@Data public class Balance implements Cloneable {
  
    private Double amount;
    private String error;
    
}
