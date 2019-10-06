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

	@Getter @Value("${app.setting.symbol}") private String symbol;
    @Getter @Value("${app.setting.rpcUrl}") private String rpcUrl;
    @Getter @Value("${app.setting.testnet}") private boolean testnet;
    @Getter @Value("${app.setting.rpcId}") private String rpcId;
    @Getter @Value("${app.setting.rpcPw}") private String rpcPw;
    @Getter @Value("${app.setting.decimals}") private int decimals;
    @Getter @Value("${app.setting.minConfirm}") private long minConfirm;

    @Getter @Value("${app.setting.ownerAddress}") private String ownerAddress;

    @Getter @Value("${app.setting.minGasAmt}") private double minGasAmt;      // not using
    @Getter @Value("${app.setting.minAmtGather}") private double minAmtGather; // not using
    @Getter @Value("${app.setting.blockStartFrom}") private long blockStartFrom;

    @Getter @Value("${app.setting.ownerAccount}") private String ownerAccount;
    @Getter @Value("${app.setting.userAccount}") private String userAccount;
}
