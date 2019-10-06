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

     app.setting.symbol=btc
     app.setting.testnet=true
     app.setting.rpcUrl=http://bitflow.ai:50000/wallet/
     app.setting.rpcId=btc-user
     app.setting.rpcPw=btc-passwd
     app.setting.decimals=8
     app.setting.blockStartFrom=1569000
     app.setting.minConfirm=3
     app.setting.userAccount=btc-users
     app.setting.pp=00000000
     app.setting.testAddress=333qWatuDDtHRfJjgVcsVNAVvAwCQ114iR
     app.setting.coldAddress=
     app.setting.minAmtGather=0.1
     app.setting.minGasAmt=0
     */

    public void debug() {
        System.out.println("1111 " + blockchainProperties.toString());
        System.out.println("2222 " + blockchainProperties.getBlockchain());
    }
}
