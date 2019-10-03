package ai.bitflow.bitwise.wallet.gsonObjects;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.ApiCommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NewAddressResponse extends ApiCommonResponse {
  
	private String result;
	public NewAddressResponse() {}
	public void setResult(String address) {
    	this.result = address;
    }

}
