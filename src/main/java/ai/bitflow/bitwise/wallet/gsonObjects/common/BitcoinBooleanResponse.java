package ai.bitflow.bitwise.wallet.gsonObjects.common;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.BitcoinAbstractResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BitcoinBooleanResponse extends BitcoinAbstractResponse {
    private boolean result;
}
