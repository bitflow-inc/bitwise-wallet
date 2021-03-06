package ai.bitflow.bitwise.wallet.gsonObjects.common;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.BitcoinAbstractResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BitcoinDoubleResponse extends BitcoinAbstractResponse {
    private double result;
}
