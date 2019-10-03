package ai.bitflow.bitwise.wallet.constants;

public interface EthereumConstant {

    String METHOD_SENDFROMADDR     	= "eth_sendTransaction";
    String METHOD_GAS_PRICE        	= "eth_gasPrice";
    String METHOD_ESTIMATEGAS      	= "eth_estimateGas";
    String METHOD_GETBALANCE       	= "eth_getBalance";
    String METHOD_SYNCING          	= "eth_syncing";
    String METHOD_ACCOUNTS         	= "eth_accounts";           // ETC?
    String METHOD_LISTACCOUNTS     	= "personal_listAccounts";  // ETH?
    String METHOD_WALLETPP         	= "personal_unlockAccount";
    String METHOD_WALLETLOCK       	= "personal_lockAccount";
    String METHOD_NEWADDR          	= "personal_newAccount";
    String METHOD_GETTX            	= "eth_getTransactionByHash";
    String METHOD_TXRECEIPT        	= "eth_getTransactionReceipt";
    String METHOD_BLOCKBYNUMBER    	= "eth_getBlockByNumber";
    String METHOD_BLOCKBYHASH      	= "eth_getBlockByHash";
    String METHOD_GETBLOCKCOUNT    	= "eth_blockNumber";
	
    String ERC_TRANSFER_CODE      	= "0xa9059cbb";
    String ERC_TRANSFERFROM_CODE  	= "0x23b872dd";
    String ERC_APPROVE_CODE       	= "0x095ea7b3";
    String ERC_EVENTTRANSFER_CODE 	= "0xddf252ad";
    String ERC_TRANSFEROWNER_CODE  	= "0xf2fde38b";
    String[] ENABLED_ERC_METHODS    = {ERC_TRANSFER_CODE, ERC_TRANSFERFROM_CODE, ERC_APPROVE_CODE};
    
    String ERC_TRANSFER_NAME      	= "TRANSFER";
    String ERC_APPROVE_NAME       	= "APPROVE";
    String ERC_TRANSFERFROM_NAME  	= "TRANSFER_FROM";
    String ERC_EVENTTRANSFER_NAME 	= "EVENT_TRANSFER";
    
	String METHOD_ERC_BALANCEOF 	= "balanceOf";
	String METHOD_ERC_UNKNOWN       = "unknown";
    
    String ERR_ETH_TX_FAIL        	= "0x0";
    String ERR_ETH_TX_SUCCESS     	= "0x1";
    
    
}
