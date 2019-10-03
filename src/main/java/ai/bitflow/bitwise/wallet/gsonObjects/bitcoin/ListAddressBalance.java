package ai.bitflow.bitwise.wallet.gsonObjects.bitcoin;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.BitcoinAbstractResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ListAddressBalance extends BitcoinAbstractResponse {
  
    private AddressBalance[] result;
    
    @Data
	public class AddressBalance {
    	private String address;
    	private String account;
    	private double amount;
    	private String label;
    	private String[] txtds;
    }

}
