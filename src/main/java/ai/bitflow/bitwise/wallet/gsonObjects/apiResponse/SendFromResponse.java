package ai.bitflow.bitwise.wallet.gsonObjects.apiResponse;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.ApiCommonResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.apiParameters.SendToAddressRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SendFromResponse extends ApiCommonResponse {
  
    private Result result;
    public void setResult(SendToAddressRequest req) {
    	this.result = new Result(req);
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class Result extends SendToAddressRequest {
    	private char status;
    	public Result(SendToAddressRequest req) {
    		super(req);
    	}
    }
    
}
