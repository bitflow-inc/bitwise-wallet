package ai.bitflow.bitwise.wallet.domains;


import ai.bitflow.bitwise.wallet.domains.primarykeys.PkSymbolTestnet;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;


@Entity
@Data
@IdClass(PkSymbolTestnet.class)
public class TbSetting implements Serializable {

	private static final long serialVersionUID = 3428912633510946300L;

    @Id
    private String symbol;
	@Id
	private char testnet;
	private String rpcIp;
	private int rpcPort;
	private String rpcUser;
	private String rpcPasswd;
	private String databaseIp;
	private String databaseUser;
	private String databasePasswd;

}
