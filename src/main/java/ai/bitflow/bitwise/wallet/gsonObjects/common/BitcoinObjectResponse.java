package ai.bitflow.bitwise.wallet.gsonObjects.common;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.BitcoinAbstractResponse;
import com.google.gson.internal.LinkedTreeMap;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BitcoinObjectResponse extends BitcoinAbstractResponse {
    private LinkedTreeMap result;
}
