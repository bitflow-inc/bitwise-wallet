package ai.bitflow.bitwise.wallet.domains;


import ai.bitflow.bitwise.wallet.domains.primarykeys.PkSymbolTestnet;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Data
@IdClass(PkSymbolTestnet.class)
public class TbBlockchainMaster implements Serializable {

	private static final long serialVersionUID = 3428902633510996300L;
	
	@Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable=false, updatable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updDt;
    @Id
    private String symbol;
    @Id
	@Column(columnDefinition = "CHAR(1) not null default 'Y'")
    private char testnet;
    private long bestHeight;
    private long syncHeight;
    private int decimals;
    private double lastTxFee;
	private String ownerWallet;
	private double lastGasPrice;
    private double lastGasUsed;

    public TbBlockchainMaster() {}
    public TbBlockchainMaster(String symbol, boolean testnet, String ownerWallet) {
	    this.symbol = symbol;
	    this.testnet = testnet?'Y':'N';
	    this.ownerWallet = ownerWallet;
	}
	public TbBlockchainMaster(String symbol, boolean testnet, String sendMastAddr, long syncHeight
			, long latestHeight) {
	    this.symbol = symbol;
		this.testnet = testnet?'Y':'N';
	    this.ownerWallet = sendMastAddr;
	    this.syncHeight = syncHeight;
	    this.bestHeight = latestHeight;
	}
	
	@Override public String toString() {
		return symbol + " " + syncHeight + " " + bestHeight + " " + updDt;
	}

}
