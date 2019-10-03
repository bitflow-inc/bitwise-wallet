package ai.bitflow.bitwise.wallet.gsonObjects.bitcoin;

import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.BitcoinAbstractResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class RawTx extends BitcoinAbstractResponse {
  
    private Result result;
    
	@Data
    public class Result {

	    private String txid;
	    private int version;
	    private long locktime;
        private Vin[] vin;
        private Vout[] vout;
        
        public Result() { }
        
		@Data
        public class Vin {
  		    private int vout;
  		    private String txid;
  		    // scriptSig (asm, hex)
  		    private long sequence;
		    public Vin() { }
		}
		
		@Data
        public class Vout {
            private double value;
            private int n;
            private String txid;
            private ScriptPubKey scriptPubKey; // (asm, hex, addresses)
            private long sequence;
            public Vout() { }
            
            @Data public class ScriptPubKey {
                private String[] addresses;
            }
        }
		
	}
	
//	{
//	        "vin": [
//	                    "asm": "3045022100b34c4b50694e1ae04a176ddbbbfd6c1939593b437d7eaa3598fa980549d16edc022024161d0084d1bdd0d23e96ed5a4679f1e4cd8bf522f85e11c61f33dc0ac86b3101 02008f1259bf3e251f3a472de2b84b51dc03dd73971f2a6593ab92b8759251fff5",
//	                    "hex": "483045022100b34c4b50694e1ae04a176ddbbbfd6c1939593b437d7eaa3598fa980549d16edc022024161d0084d1bdd0d23e96ed5a4679f1e4cd8bf522f85e11c61f33dc0ac86b31012102008f1259bf3e251f3a472de2b84b51dc03dd73971f2a6593ab92b8759251fff5"
//	        "vout": [
//	            {
//	                "value": 486.9919,
//	                "n": 0,
//	                "scriptPubKey": {
//	                    "asm": "OP_DUP OP_HASH160 0a1f494da52b94546313bc8e93f0171018fbe9d5 OP_EQUALVERIFY OP_CHECKSIG",
//	                    "hex": "76a9140a1f494da52b94546313bc8e93f0171018fbe9d588ac",
//	                    "reqSigs": 1,
//	                    "type": "pubkeyhash",
//	                    "addresses": [
//	                        "D64cmzN2rVVeh5jE9HnqdKSR8Fz5xZCq4u"
//	                    ]
//	            },
//	    },
//	    "error": null,
//	    "id": 1
//	}

}
