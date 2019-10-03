package ai.bitflow.bitwise.wallet.domains.primarykeys;

import lombok.Data;

import java.io.Serializable;

@Data
public class PkUidAddress implements Serializable {

	private static final long serialVersionUID = 4322946219884324792L;
    private String uid;
    private String address;

    public PkUidAddress() {}
    public PkUidAddress(String uid, String address) {
        super();
        this.uid = uid;
        this.address = address;
    }
  
}