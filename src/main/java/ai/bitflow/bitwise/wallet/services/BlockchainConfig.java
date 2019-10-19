package ai.bitflow.bitwise.wallet.services;

import lombok.Data;

import java.io.Serializable;

//@Component
//@ConfigurationProperties(prefix = "blockchain")
@Data
public class BlockchainConfig implements Serializable {

    private Cryptocurrency btc;
    private Cryptocurrency ltc;
    private Cryptocurrency bch;
    private Cryptocurrency bsv;
    private Cryptocurrency doge;
    private Cryptocurrency btg;
    private Cryptocurrency bcd;

    @Override
    public String toString() {
        return "{ \"btc\": " + btc + ", \"ltc\": " + ltc + ", \"bch\": " + bch + ", \"bsv\": " + bsv + ", \"doge\": " + doge
                + ", \"btg\": " + btg + ", \"bcd\": " + bcd + "}";
    }

    @Data
    public class Cryptocurrency implements Serializable {

        private String homepage;
        private int minConfirm;
        private Network mainnet;
        private Network testnet;

        @Override
        public String toString() {
            return "{ \"homepage\": \"" + homepage + "\", \"minConfirm\": " + minConfirm + ", \"mainnet\": " + mainnet
                    + ", \"testnet\": " + testnet + "}";
        }

        @Data
        public class Network implements Serializable {

            private int port;
            private String explorerMain;
            private String explorerTx;
            private String faucet;

            @Override
            public String toString() {
                return "{ \"port\": " + port + ", \"explorerMain\": \"" + explorerMain + "\", \"explorerTx\": \"" + explorerTx
                        + "\", \"faucet\": \"" + faucet + "\" }";
            }

        }

    }

}
