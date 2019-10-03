package ai.bitflow.bitwise.wallet.daos.abstracts;

import ai.bitflow.bitwise.wallet.services.abstracts.BitcoinService;

public interface BlockchainSharedIf {
    long getBestBlockCount(BitcoinService ctx) throws Exception;
}
