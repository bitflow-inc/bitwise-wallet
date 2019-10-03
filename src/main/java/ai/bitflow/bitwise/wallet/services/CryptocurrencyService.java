package ai.bitflow.bitwise.wallet.services;

import ai.bitflow.bitwise.wallet.services.abstracts.BitcoinService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * BTC Services
 * @author sungjoon.kim
 */
@Service("cryptoCurrencyService")
class CryptocurrencyService extends BitcoinService {

	@Getter @Value("${app.crypto.symbol}") private String symbol;
    @Getter @Value("${app.crypto.rpcUrl}") private String rpcUrl;
    @Getter @Value("${app.crypto.testnet}") private boolean testnet;
    @Getter @Value("${app.crypto.rpcId}") private String rpcId;
    @Getter @Value("${app.crypto.rpcPw}") private String rpcPw;
    @Getter @Value("${app.crypto.decimals}") private int decimals;
    @Getter @Value("${app.crypto.userAccount}") private String userAccount;
    @Getter @Value("${app.crypto.blockStartFrom}") private long blockStartFrom;
    @Getter @Value("${app.crypto.minConfirm}") private long minConfirm;

    @Getter @Value("${app.crypto.ownerAccount}") private String ownerAccount;
    @Getter @Value("${app.crypto.ownerAddress}") private String ownerAddress;

    @Getter @Value("${app.crypto.minGasAmt}") private double minGasAmt;      // not using
    @Getter @Value("${app.crypto.minAmtGather}") private double minAmtGather; // not using

}
