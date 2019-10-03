package ai.bitflow.bitwise.wallet.gsonObjects.apiResponse;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.ApiCommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data public class GetBlockCountResponse extends ApiCommonResponse {
    private long result;
}
