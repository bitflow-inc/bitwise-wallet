package ai.bitflow.bitwise.wallet.services.interfaces;

public interface LockableAddress {

    boolean walletpassphraseWithAddress(String address);
    boolean walletlock(String address);
    String getPp();
    
}