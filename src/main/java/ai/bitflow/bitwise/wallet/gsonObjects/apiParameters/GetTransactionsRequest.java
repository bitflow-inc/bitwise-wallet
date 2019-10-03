package ai.bitflow.bitwise.wallet.gsonObjects.apiParameters;

import lombok.Data;

@Data
public class GetTransactionsRequest {

    private String uid;
    private int pageno;
    private int size;

    public GetTransactionsRequest() {}

    public GetTransactionsRequest(String uid, int pageno, int size) {
        this.uid = uid;
        this.pageno = pageno;
        this.size = size;
    }

}
