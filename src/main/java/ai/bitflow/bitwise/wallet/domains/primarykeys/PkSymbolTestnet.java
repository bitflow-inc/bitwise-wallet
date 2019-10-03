package ai.bitflow.bitwise.wallet.domains.primarykeys;

import lombok.Data;

import java.io.Serializable;

@Data
public class PkSymbolTestnet implements Serializable {

    private static final long serialVersionUID = 8735488833586516770L;
    private String symbol;
    private char testnet;

    public PkSymbolTestnet() {}
    public PkSymbolTestnet(String symbol, char testnet) {
        super();
        this.symbol = symbol;
        this.testnet = testnet;
    }
  
}