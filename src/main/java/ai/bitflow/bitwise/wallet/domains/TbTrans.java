package ai.bitflow.bitwise.wallet.domains;

import ai.bitflow.bitwise.wallet.constants.abstracts.BlockchainConstant;
import ai.bitflow.bitwise.wallet.domains.primarykeys.PkTxidCategoryToAddr;
import ai.bitflow.bitwise.wallet.gsonObjects.apiParameters.SendToAddressRequest;
import lombok.Data;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@IdClass(PkTxidCategoryToAddr.class)
public class TbTrans implements Serializable {

	@Temporal(TemporalType.TIMESTAMP)
	@Generated(GenerationTime.INSERT)
	@Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable=false, updatable=false)
	private Date regDt;
	@Id
    private String txid;
	@Id
	private String category;
	@Id
	private String toAddr;
	@Column(nullable=false)
	private String uid;
	private long confirm;
	private String fromAddr;
	private String myAddr;
	private double amount;
	private char reNotify = 'N';
	private char notifiable = 'Y';
	private int notiCnt;
	private long txTime;
	private String txTimeStr;
	private String fromTag;
	private String toTag;
	private String blockId;
	private double fee;
	private String error;
	private String txIdx;
	private String orderId;

	public TbTrans() { }

	public TbTrans(SendToAddressRequest req) {
		this.uid       	= req.getUid();
		this.category   = BlockchainConstant.CATEGORY_SEND;
		this.toAddr    	= req.getToAddress();
		this.amount    	= req.getAmount();
	}

	public TbTrans(String category, String txid, String uid,
				   String toAddr, double amount) {
		this.category   = category;
		this.txid		= txid;
		this.uid		= uid;
		this.toAddr		= toAddr;
		this.amount		= amount;
	}

	// case of send
	public TbTrans(String txid, String uid, String toAddr, double amount) {
		this.category   = BlockchainConstant.CATEGORY_SEND;
		this.txid		= txid;
		this.uid		= uid;
		this.toAddr		= toAddr;
		this.amount		= amount;
	}

	public TbTrans(String uid, String toAddr, double amount) {
		this.uid       = uid;
		this.toAddr    = toAddr;
		this.amount    = amount;
	}

}
