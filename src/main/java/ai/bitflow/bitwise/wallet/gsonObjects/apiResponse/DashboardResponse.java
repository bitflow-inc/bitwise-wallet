package ai.bitflow.bitwise.wallet.gsonObjects.apiResponse;

import ai.bitflow.bitwise.wallet.gsonObjects.common.SystemInfo;
import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.ApiCommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DashboardResponse extends ApiCommonResponse {

	private Result result;
	public DashboardResponse() {
	    this.result = new Result();
    }

	public void setResult(long startCount, long syncCount, long bestCount, double balance, SystemInfo systemInfo) {
    	this.result.setStartCount(startCount);
        this.result.setSyncCount(syncCount);
        this.result.setBestCount(bestCount);
        this.result.setBalance(String.format("%.8f", balance));
        this.result.setSystemInfo(systemInfo);
    }

    @EqualsAndHashCode(callSuper = false) @Data
    public class Result {
        long startCount;
        long syncCount;
        long bestCount;
        String balance;
        int walletCount;
        SystemInfo systemInfo;
    }

}
