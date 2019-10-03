package ai.bitflow.bitwise.wallet.gsonObjects.apiResponse;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.ApiCommonResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.objects.Wallet;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class WalletAjaxResponse extends ApiCommonResponse {

	private Result result;
	public WalletAjaxResponse() { }

	public void setResult(List<Wallet> wallets) {
	    this.result = new Result(wallets);
	    this.result.setWalletCount(wallets.size());
    }

    @EqualsAndHashCode(callSuper = false) @Data
    public class Result {
        private int walletCount;
        private List<Wallet> wallets;

        public Result(List<Wallet> wallets) {
            this.wallets = wallets;
        }

    }

}
