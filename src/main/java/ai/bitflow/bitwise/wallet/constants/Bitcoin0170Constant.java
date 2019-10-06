package ai.bitflow.bitwise.wallet.constants;

public interface Bitcoin0170Constant {

    String METHOD_CREATEWALLET          = "createwallet";

    String METHOD_ENCRYPTWALLET         = "encryptwallet";
    String METHOD_ESTIMATESMARTFEE      = "estimatesmartfee";

    String METHOD_GETADDRBYLABEL        = "getaddressesbylabel";
    String METHOD_GETBALANCE            = "getbalance";
    String METHOD_GETBLOCK              = "getblock";
    String METHOD_GETBLOCKCOUNT         = "getblockcount";
    String METHOD_GETBLOCKHASH          = "getblockhash";
    String METHOD_GETTRANSACTION        = "gettransaction";
    String METHOD_GETWALLETINFO         = "getwalletinfo";

    String METHOD_LISTADDRESSGROUPINGS  = "listaddressgroupings";
    String METHOD_LISTSINCEBLOCK        = "listsinceblock";
    String METHOD_LISTWALLETS           = "listwallets";
    String METHOD_NEWADDR               = "getnewaddress";

    String METHOD_SENDMANY              = "sendmany";
    String METHOD_SENDTOADDRESS         = "sendtoaddress";
    String METHOD_SETHDSEED             = "sethdseed";
    String METHOD_SETTXFEE              = "settxfee";

    String METHOD_VALIDATEADDRESS       = "validateaddress";
    String METHOD_WALLETLOCK            = "walletlock";
    String METHOD_WALLETPP              = "walletpassphrase";

    int ERR_CD_PP_WRONG                 = -14;  // Error: The wallet passphrase entered was incorrect.
    int ERR_CD_WALLET_UNENC             = -15;  // Error: running with an unencrypted wallet, but walletpassphrase was called.


}
