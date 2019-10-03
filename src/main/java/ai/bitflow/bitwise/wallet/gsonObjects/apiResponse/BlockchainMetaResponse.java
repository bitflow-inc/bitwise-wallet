package ai.bitflow.bitwise.wallet.gsonObjects.apiResponse;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.ApiCommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BlockchainMetaResponse extends ApiCommonResponse {

	private Result result;
	public BlockchainMetaResponse() {
	    this.result = new Result();
    }

	public void setResult(String chainName, boolean testnet) {
        this.result.setChainName(chainName);
        this.result.setTestnet(testnet);
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class Result {
        String chainName;
        boolean testnet;
    }

}
