package ai.bitflow.bitwise.wallet.gsonObjects.bitcoin;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.BitcoinAbstractResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateWallet extends BitcoinAbstractResponse {

    private Result result;

    @Data
    public class Result {
        private String name;
        private String warning;
    }
}
