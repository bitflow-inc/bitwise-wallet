package ai.bitflow.bitwise.wallet.gsonObjects.bitcoin;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.BitcoinAbstractResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GetWalletInfo extends BitcoinAbstractResponse {

    private Result result;

    @Data
    public class Result {
        private String walletname;
        private int walletversion;
        private double balance;
        private double unconfirmed_balance;
        private double immature_balance;
        private int txcount;
        private double paytxfee;
        private boolean private_keys_enabled;
//    keypoololdest: 1555301795,
//    keypoolsize: 1000,
//    keypoolsize_hd_internal": 999,
//    hdseedid: "86281af34e015dde2ae2faa5779aa50c432a181e",
//    hdmasterkeyid: "86281af34e015dde2ae2faa5779aa50c432a181e",
    }
}
