package ai.bitflow.bitwise.wallet.gsonObjects;

import ai.bitflow.bitwise.wallet.gsonObjects.apiParameters.PersonalRequest;
import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.ApiCommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NewAddressResponseOld extends ApiCommonResponse {

	private Result result;
	public NewAddressResponseOld() {}
	public void setResult(PersonalRequest req) {
    	this.result = new Result(req);
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class Result extends PersonalRequest {
        public Result(PersonalRequest req) {
        	super(req);
        }
    }
    
}
