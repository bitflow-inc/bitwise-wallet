package ai.bitflow.bitwise.wallet.constants;

import ai.bitflow.bitwise.wallet.utils.JsonPropertySourceFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;

//@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
public class BlockchainProperties {

    private HashMap<String, Blockchain> blockchain;

    public HashMap<String, Blockchain> getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(HashMap<String, Blockchain> blockchain) {
        this.blockchain = blockchain;
    }

    public class Blockchain {

        private String homepage;
        private BlochainNetwork mainnet;
        private BlochainNetwork testnet;

        public String getHomepage() {
            return homepage;
        }

        public void setHomepage(String homepage) {
            this.homepage = homepage;
        }

        public BlochainNetwork getMainnet() {
            return mainnet;
        }

        public void setMainnet(BlochainNetwork mainnet) {
            this.mainnet = mainnet;
        }

        public BlochainNetwork getTestnet() {
            return testnet;
        }

        public void setTestnet(BlochainNetwork testnet) {
            this.testnet = testnet;
        }
    }

    public class BlochainNetwork {

        private int port;
        private String explorer_main;
        private String explorer_tx;
        private String faucet;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getExplorer_main() {
            return explorer_main;
        }

        public void setExplorer_main(String explorer_main) {
            this.explorer_main = explorer_main;
        }

        public String getExplorer_tx() {
            return explorer_tx;
        }

        public void setExplorer_tx(String explorer_tx) {
            this.explorer_tx = explorer_tx;
        }

        public String getFaucet() {
            return faucet;
        }

        public void setFaucet(String faucet) {
            this.faucet = faucet;
        }
    }

}
