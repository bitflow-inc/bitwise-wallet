package ai.bitflow.bitwise.wallet.gsonObjects.objects;

import lombok.Data;

import java.io.Serializable;

@Data
public class Wallet implements Serializable {

    private String name;
    private boolean lock;
    private double balance;

}
