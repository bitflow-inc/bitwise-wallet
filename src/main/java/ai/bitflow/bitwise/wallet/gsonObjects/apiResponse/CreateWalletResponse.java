package ai.bitflow.bitwise.wallet.gsonObjects.apiResponse;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.ApiCommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateWalletResponse extends ApiCommonResponse {
    private Result result;
    public void setResult(String address, String privateKey, String mnemonic) {
        this.result = new Result(address, privateKey, mnemonic);
    }

    @Data
    public class Result {
        String address;
        String privateKey;
        String mnemonic;
        public Result(String address, String privateKey, String mnemonic) {
            this.address = address;
            this.privateKey = privateKey;
            this.mnemonic = mnemonic;
        }
    }
}
