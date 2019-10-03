package ai.bitflow.bitwise.wallet.gsonObjects.abstracts;

import lombok.Data;

@Data
public abstract class BitcoinAbstractResponse {
    private int id;
    private RpcError error;
}
