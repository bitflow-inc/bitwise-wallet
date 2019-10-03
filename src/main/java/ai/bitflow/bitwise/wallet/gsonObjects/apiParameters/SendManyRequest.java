package ai.bitflow.bitwise.wallet.gsonObjects.apiParameters;

import lombok.Data;

import java.util.Map;

@Data
public class SendManyRequest {

    private String uid;
    private String pp;
    private Map<String, Double> to;

    public SendManyRequest() {}

    @Override
    public String toString() {
        return uid + " " + to.toString();
    }

}
