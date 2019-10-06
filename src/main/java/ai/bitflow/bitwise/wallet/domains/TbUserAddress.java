package ai.bitflow.bitwise.wallet.domains;

import ai.bitflow.bitwise.wallet.domains.primarykeys.PkUidAddress;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 사용자별 암호화폐 월렛/(+어카운트)/주소 정보 저장 테이블
 */
@Data
@Entity
@IdClass(PkUidAddress.class)
public class TbUserAddress implements Serializable {

	private static final long serialVersionUID = 5127319640626022175L;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable=false, updatable=false)
	private Date regDt;
	@Id
	private String uid;
	@Id
	private String address;
	private String mnemonicEnc;
	private char defaultYn;
	private double balance;
	private double unconfirmedBalance;
	private String account;
	private String tag;


	public TbUserAddress() {}

	public TbUserAddress(String uid, String address) {
		this.uid = uid;
		this.address = address;
	}

	@Override public String toString() {
		return "[" + uid + "] " + address
				+ " balance is " + balance;
	}

}

