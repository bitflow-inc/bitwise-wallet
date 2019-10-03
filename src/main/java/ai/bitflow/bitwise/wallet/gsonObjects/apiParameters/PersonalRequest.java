package ai.bitflow.bitwise.wallet.gsonObjects.apiParameters;

import lombok.Data;

@Data
public class PersonalRequest {
  
    private String symbol;
    private String uid;
    private String orderId;
    private String address;
    private String addressTag;
    private String passphrase;

    public PersonalRequest() {}

    public PersonalRequest(String uid) {
        this.uid = uid;
    }

    public PersonalRequest(String symbol, String uid) {
        this.symbol = symbol;
        this.uid = uid;
    }
    
    public PersonalRequest(PersonalRequest req) {
        this.symbol = req.getSymbol();
        this.uid = req.getUid();
        this.address = req.getAddress();
        this.addressTag = req.getAddressTag();
        this.passphrase = req.getPassphrase();
    }
    
}
