package ai.bitflow.bitwise.wallet.gsonObjects.objects;

import ai.bitflow.bitwise.wallet.converters.DoubleSerializer;
import ai.bitflow.bitwise.wallet.domains.TbTrans;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
public class Transaction implements Serializable {

	private Date regDt;
    private String txid;
	private String category;
	private String toAddr;
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

	public Transaction() { }

	public Transaction(TbTrans tx) {
		this.regDt = tx.getRegDt();
		this.txid = tx.getTxid();
		this.category = tx.getCategory();
		this.toAddr = tx.getToAddr();
		this.uid = tx.getUid();
		this.confirm = tx.getConfirm();
		this.fromAddr = tx.getFromAddr();
		this.myAddr = tx.getMyAddr();
		this.amount = tx.getAmount();
		this.txTime = tx.getTxTime();
		this.fee = tx.getFee();
		this.orderId = tx.getOrderId();
		this.error = tx.getError();
	}

	public Date getRegDt() {
		return regDt;
	}

	public String getTxid() {
		return txid;
	}

	public String getCategory() {
		return category;
	}

	public String getToAddr() {
		return toAddr;
	}

	public String getUid() {
		return uid;
	}

	public long getConfirm() {
		return confirm;
	}

	public String getFromAddr() {
		return fromAddr;
	}

	public String getMyAddr() {
		return myAddr;
	}

	@JsonSerialize(using = DoubleSerializer.class)
	public double getAmount() {
		return amount;
	}

	public char getReNotify() {
		return reNotify;
	}

	public char getNotifiable() {
		return notifiable;
	}

	public int getNotiCnt() {
		return notiCnt;
	}

	public long getTxTime() {
		return txTime;
	}

	public String getTxTimeStr() {
		return txTimeStr;
	}

	public String getFromTag() {
		return fromTag;
	}

	public String getToTag() {
		return toTag;
	}

	public String getBlockId() {
		return blockId;
	}

	@JsonSerialize(using = DoubleSerializer.class)
	public double getFee() {
		return fee;
	}

	public String getError() {
		return error;
	}

	public String getTxIdx() {
		return txIdx;
	}

	public String getOrderId() {
		return orderId;
	}
}
