package ai.bitflow.bitwise.wallet.services.interfaces;

import java.util.List;
import java.util.Set;

public interface OwnChain {

    boolean openBlocksGetTxsThenSave();
    long getBlockStartFrom();
    long getMinConfirm();

    List<String> getAllAddressListFromNode();
    Set<String> getAllAddressSetFromNode();
}