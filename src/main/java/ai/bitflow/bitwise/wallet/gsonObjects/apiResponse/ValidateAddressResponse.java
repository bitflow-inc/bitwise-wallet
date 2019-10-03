package ai.bitflow.bitwise.wallet.gsonObjects.apiResponse;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.ApiCommonResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.apiParameters.PersonalRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data public class ValidateAddressResponse extends ApiCommonResponse {
  
    private Result result;
    public ValidateAddressResponse() {}
    public ValidateAddressResponse(PersonalRequest req) {
    	this.result = new Result(req);
    }
    
    @EqualsAndHashCode(callSuper = false) @Data 
    public class Result extends PersonalRequest {
    	boolean valid;
    	public Result(PersonalRequest req) {
    		super(req);
    	}
    }
    
}
