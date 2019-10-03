package ai.bitflow.bitwise.wallet.services;

import ai.bitflow.bitwise.wallet.constants.BlockchainProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Setting Services
 * @author sungjoon.kim
 */
@Service("settingService")
public class SettingService {

    @Autowired
    private BlockchainProperties blockchainProperties;

    /**
     app.useKafka=false
     app.useFcm=false

     app.crypto.symbol=btc
     app.crypto.testnet=true
     app.crypto.rpcUrl=http://bitflow.ai:50000/wallet/
     app.crypto.rpcId=btc-user
     app.crypto.rpcPw=btc-passwd
     app.crypto.decimals=8
     app.crypto.blockStartFrom=1569000
     app.crypto.minConfirm=3
     app.crypto.userAccount=btc-users
     app.crypto.pp=00000000
     app.crypto.testAddress=333qWatuDDtHRfJjgVcsVNAVvAwCQ114iR

     app.crypto.ownerAccount=btc-owner
     app.crypto.ownerAddress=3P6yoEDNrcZcUs3uMM3RW7qCGFtz8SZbk6
     app.crypto.coldAddress=

     app.crypto.minAmtGather=0.1
     app.crypto.minGasAmt=0
     */

    public void debug() {
        System.out.println("1111 " + blockchainProperties.toString());
        System.out.println("2222 " + blockchainProperties.getBlockchain());
    }
}
