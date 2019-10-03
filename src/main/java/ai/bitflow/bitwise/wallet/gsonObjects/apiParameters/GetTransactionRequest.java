package ai.bitflow.bitwise.wallet.gsonObjects.apiParameters;

import lombok.Data;

@Data
public class GetTransactionRequest {

    private String uid;
    private String txid;

    public GetTransactionRequest() {}

    public GetTransactionRequest(String uid, String txid) {
        this.uid = uid;
        this.txid = txid;
    }

}
