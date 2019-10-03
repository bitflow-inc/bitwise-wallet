package ai.bitflow.bitwise.wallet.gsonObjects.abstracts;

import lombok.Data;

@Data
public class RpcError {
    private int code;
    private String message;
    public RpcError() { }
    public RpcError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}