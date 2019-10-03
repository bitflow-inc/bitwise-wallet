package ai.bitflow.bitwise.wallet.constants;

public interface Bitcoin0170Constant {

    String METHOD_GETBLOCKCOUNT     = "getblockcount";
    String METHOD_ESTIMATESMARTFEE  = "estimatesmartfee";
    String METHOD_SETHDSEED         = "sethdseed";
    String METHOD_LISTADDRESSGROUPINGS = "listaddressgroupings";
    // listaddressgroupings
    String METHOD_CREATEWALLET      = "createwallet";
    String METHOD_NEWADDR           = "getnewaddress";
    String METHOD_DUMPPRIVKEY       = "dumpprivkey";
    String METHOD_ENCRYPTWALLET     = "encryptwallet";

    String METHOD_LISTWALLETS       = "listwallets";
    String METHOD_VALIDATEADDRESS   = "validateaddress";
    String METHOD_GETWALLETINFO     = "getwalletinfo";
//    String METHOD_GETBALANCE       = "getbalance";

    String METHOD_WALLETPP          = "walletpassphrase";
    String METHOD_SENDTOADDRESS     = "sendtoaddress";
    String METHOD_SETTXFEE          = "settxfee";

    String METHOD_SENDMANY          = "sendmany";
    String METHOD_WALLETLOCK        = "walletlock";

    String METHOD_LISTSINCEBLOCK    = "listsinceblock";
    String METHOD_GETTRANSACTION    = "gettransaction";

    String METHOD_GETBLOCKHASH      = "getblockhash";
    String METHOD_GETBLOCK          = "getblock";

}
