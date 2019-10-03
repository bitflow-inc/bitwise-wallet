package ai.bitflow.bitwise.wallet.domains.primarykeys;

import lombok.Data;

import java.io.Serializable;

@Data
public class PkTxidCategoryToAddr implements Serializable {

    private static final long serialVersionUID = 8735401150586516770L;
    private String txid;
    private String category;
    private String toAddr;

    public PkTxidCategoryToAddr() {}
    public PkTxidCategoryToAddr(String txid, String category) {
        this.txid = txid;
        this.category = category;
    }
    public PkTxidCategoryToAddr(String txid, String category, String toAddr) {
        this.txid = txid;
        this.category = category;
        this.toAddr = toAddr;
    }
  
}