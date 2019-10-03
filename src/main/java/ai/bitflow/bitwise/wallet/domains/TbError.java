package ai.bitflow.bitwise.wallet.domains;


import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.RpcError;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Slf4j
@Data
@Entity
public class TbError implements Serializable {

	private static final long serialVersionUID = 3127317641626022176L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable=false, updatable=false)
	private Date regDt;
	@Id
	@GeneratedValue
	private long id;
	private String error;

	public TbError() { }
	public TbError(String method, Exception e) {
		if (log.isDebugEnabled()) {
//			e.printStackTrace();
		} else {
			log.error(method, e);
		}
		this.error = method + ": " + e.getMessage();
	}

	public TbError(String method, int code, String error) {
		log.error(method, error);
		this.error = method + ": [" + code + "] " + error;
	}

	public TbError(String method, String error) {
		log.error(method, error);
		this.error = method + ": " + error;
	}

	public TbError(String method, RpcError e) {
		log.error(method, error);
		this.error = method + ": [" + e.getCode() + "] " + e.getMessage();
	}

}

