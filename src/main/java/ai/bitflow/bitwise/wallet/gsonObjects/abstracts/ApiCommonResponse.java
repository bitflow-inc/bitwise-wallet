package ai.bitflow.bitwise.wallet.gsonObjects.abstracts;

import ai.bitflow.bitwise.wallet.constants.abstracts.BlockchainConstant;
import lombok.Data;

@Data
public class ApiCommonResponse implements BlockchainConstant {
    int code = CODE_SUCCESS;
    String error;
}
