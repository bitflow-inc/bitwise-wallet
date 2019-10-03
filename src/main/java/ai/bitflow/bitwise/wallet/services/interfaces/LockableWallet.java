package ai.bitflow.bitwise.wallet.services.interfaces;

import ai.bitflow.bitwise.wallet.services.abstracts.BitcoinService;

public interface LockableWallet {

    boolean walletpassphrase(BitcoinService ctx);
    boolean walletpassphraseshort(BitcoinService ctx);
    boolean walletpassphrase(BitcoinService ctx, String interval);
    boolean walletlock(BitcoinService ctx, String uid);
    String getPassphrase();
    
}