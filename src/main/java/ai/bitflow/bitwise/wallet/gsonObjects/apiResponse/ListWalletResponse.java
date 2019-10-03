package ai.bitflow.bitwise.wallet.gsonObjects.apiResponse;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.ApiCommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ListWalletResponse extends ApiCommonResponse {
    private Result result;
    public void setResult(int count, String[] walletnames) {
        this.result = new Result(count, walletnames);
    }
    @Data
    public class Result {
        int count;
        String[] walletnames;
        public Result(int count, String[] walletnames) {
            this.count = count;
            this.walletnames = walletnames;
        }
    }
}
