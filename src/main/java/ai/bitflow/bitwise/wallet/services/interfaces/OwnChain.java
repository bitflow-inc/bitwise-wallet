package ai.bitflow.bitwise.wallet.services.interfaces;

public interface OwnChain {

    boolean openBlocksGetTxsThenSave();
    long getBlockStartFrom();
    long getMinConfirm();

}