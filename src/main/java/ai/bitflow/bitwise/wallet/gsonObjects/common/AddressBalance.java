package ai.bitflow.bitwise.wallet.gsonObjects.common;

import lombok.Data;

@Data
public class AddressBalance {
    private String address;
    private double balance;
    private String label;
}
