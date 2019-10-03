package ai.bitflow.bitwise.wallet.gsonObjects.apiResponse;

import ai.bitflow.bitwise.wallet.gsonObjects.common.AddressBalance;
import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.ApiCommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class GetBalanceResponse extends ApiCommonResponse {

	private Result result;
	public GetBalanceResponse() {
		this.result = new Result();
	}

	@Data
	public class Result {
		private double balance;
		private double unconfirmedBalance;
		private List<AddressBalance> addressBalances;
	}
}
