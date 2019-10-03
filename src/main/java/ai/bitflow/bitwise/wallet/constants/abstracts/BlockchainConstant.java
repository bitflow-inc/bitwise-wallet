package ai.bitflow.bitwise.wallet.constants.abstracts;

import java.math.BigDecimal;

public interface BlockchainConstant {

	int UID_SYSTEM = -999;
    int UID_TEST   = -10;
    
    int NOTI_CNT_NONE     = 0;
    int NOTI_CNT_PROGRESS = 1;
    int NOTI_CNT_FINISHED = 2;
    int NOTI_CNT_WITHDRAW = 3;
    int NOTI_CNT_COMPLETE = 4;

    int SENDMANY_TX_ONCE          = 20;
    int SEND_UNSENT_TX_LIMIT_DATE = -5;
    int SEND_GATHER_LIMIT_HOUR    = -6;
    int SEND_TX_SEARCH_LIMIT_DATE = -23;
    
    int CODE_SUCCESS                 = 999;
    int CODE_FAIL_PARAM              = 100;
    int CODE_FAIL_LOGICAL            = 102;
    int CODE_FAIL_NOT_ENOUGH_BALANCE = 103;
    int CODE_FAIL_DUP_ORDER_ID       = 104;
    int CODE_WALLET_NOT_EXISTS       = 105;
    int CODE_FAIL_IO_PROBLEM         = 106;
    int CODE_FAIL_NOT_MY_WALLET      = -710;
    
    BigDecimal ETH_TO_WEI = BigDecimal.valueOf(1000000000000000000l);
    
    double SYSTEM_EXPCT_FEE = 0.0000D;
    double TEST_AMOUNT_BIG 	= 2D;
    double TEST_AMOUNT 		= 0.001D;
    double TEST_EXPCT_FEE   = 0.0001D;
    double ADA_TO_LOVELACE  = 1000000D;
    
    char INTENT_PURETX         = 'S';
	char INTENT_BUY_TOKEN      = 'T';
	char INTENT_BONUS_TOKEN    = 'B';
	char INTENT_MANUAL_PAY     = 'M';
	char INTENT_POINT_EXCHANGE = 'P';
    		
    char STAT_UNKNOWN  	  	   = 'U'; // UNKNOWN
    char STAT_ACCEPTED    	   = 'A'; // ACCEPTED
    char STAT_BEFOREDEPOSIT    = 'B'; // BEFORE DEPOST
    char STAT_DEPOSITING  	   = 'D'; // DEPOSITING
    char STAT_BEFOREWITHDRAWAL = 'P'; // BEFORE WITHDRAWAL    
    char STAT_PROGRESS 	 	   = 'P'; // 범용: 진행 중
    char STAT_WITHDRAWING  	   = 'W'; // WITHDRAWING
    char STAT_COMPLETED	  	   = 'C'; // COMPLETED
    char STAT_FAILED   	  	   = 'F'; // FAILED
    
    char STAT_WAITCONFIRM   = 'T'; // not using now
    
    char TYPE_TX_SEND  	    = 'S';
    char TYPE_TX_RECV  	    = 'R';
    
    String JSON_HEADER   = "application/json;charset=utf-8";
    String TAG_TEST      = "[TEST]";
    String SUCCESS_ADA   = "success";
    String FAIL_ADA		 = "error";
    String TEST_TOPIC_ID = "WLT_TRANSMIT.KR00";
    
    String ORDER_ID_SYSTEM  = "SYSTEM";
    String BROKER_ID_SYSTEM = "SYST";
    String COINBASE_NAME    = "COINBASE";
    
    String CATEGORY_SEND        = "send";
    String CATEGORY_RECEIVE     = "receive";
    String CATEGORY_SEND_ADA    = "outgoing";
    String CATEGORY_RECEIVE_ADA = "incoming";
    String CATEGORY_GENERATE    = "generate";
    String UNIT_KRW       		= "KRW";
    String MAX_QUEUE_POLL_SIZE  = "100";
    String WALLET_UNLOCK_SHORT  = "1";
    String WALLET_UNLOCK_10SECS = "10";
    String WALLET_UNLOCK_BATCH  = "20";
    String ADA_TO_LOVELACE_STR  = "1000000";
    
    String MSG_EMPTY_SYMBOL       = "symbol is empty";
    String MSG_EMPTY_UID          = "uid is empty";
    String MSG_EMPTY_ADDRESS      = "address is empty";
    String MSG_EMPTY_TXID         = "txid is empty";
    String MSG_INVALID_SYMBOL 	  = "invalid symbol name";
    String MSG_INVALID_BUY_TOKEN  = "not allowed token to buy";
    String MSG_SEND_ABANDONED     = "[-1] send transaction abandoned";
    String MSG_DUP_ORDER_ID		  = "[-2] duplicate order id";
    String MSG_WALLET_NOT_EXISTS  = "[-3] wallet not exists";
    String MSG_NOT_ENOUGH_BALANCE = "[-4] not enough balance";
    String MSG_UNLOCK_FAIL  	  = "[-5] wallet unlocking failed";
    String MSG_SEND_FAIL  	  	  = "[-6] sendtransaction failed";
    String MSG_BLOCK_TX_FAIL  	  = "[-7] blockchain TX failed";
    String MSG_UNCATE_CRYPTO  	  = "[-8] uncategorized cryptocurrency";
    String MSG_IO_PROBLEM         = "[-9] network I/O failed";
    
    String SYMBOL_KRW	= "KRW";
    String SYMBOL_BTC	= "BTC";
    String SYMBOL_ETH	= "ETH";
    
    String SYMBOL_BCD	= "BCD";
    String SYMBOL_KAI	= "KAI";
    String SYMBOL_BHPC	= "BHPC";
    String SYMBOL_ZIL	= "ZIL";
    String SYMBOL_ETC	= "ETC";
    String SYMBOL_CLO	= "CLO";
    String SYMBOL_BCH	= "BCH";
    String SYMBOL_BTG	= "BTG";
    String SYMBOL_LTC	= "LTC";
    String SYMBOL_ADA	= "ADA";
    String SYMBOL_DOGE  = "DOGE";
    String SYMBOL_EOS	= "EOS";
    
    String SYMBOL_NEO	= "NEO";
    String SYMBOL_XRP	= "XRP";
    String SYMBOL_XLM	= "XLM";    
    
    String SYMBOL_OMG	= "OMG";
    String SYMBOL_QTUM	= "QTUM";
    String SYMBOL_XMR	= "XMR";
    String SYMBOL_ZEC	= "ZEC";
    
    String TOKEN_ERC20	= "ERC20";
    String TOKEN_QRC20	= "QRC20";
    
}

