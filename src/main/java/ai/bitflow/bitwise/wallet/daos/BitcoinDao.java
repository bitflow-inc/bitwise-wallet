package ai.bitflow.bitwise.wallet.daos;

import ai.bitflow.bitwise.wallet.utils.Logger;
import ai.bitflow.bitwise.wallet.constants.Bitcoin0170Constant;
import ai.bitflow.bitwise.wallet.constants.abstracts.BlockchainConstant;
import ai.bitflow.bitwise.wallet.domains.TbError;
import ai.bitflow.bitwise.wallet.domains.TbTrans;
import ai.bitflow.bitwise.wallet.gsonObjects.apiParameters.SendToAddressRequest;
import ai.bitflow.bitwise.wallet.gsonObjects.bitcoin.*;
import ai.bitflow.bitwise.wallet.gsonObjects.common.*;
import ai.bitflow.bitwise.wallet.repositories.ErrorRepository;
import ai.bitflow.bitwise.wallet.repositories.TransactionRepository;
import ai.bitflow.bitwise.wallet.services.abstracts.BitcoinService;
import ai.bitflow.bitwise.wallet.services.interfaces.LockableWallet;
import ai.bitflow.bitwise.wallet.utils.JsonRpcUtil;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import lombok.Getter;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;


/**
 * 비트코인 계열 블록체인의 JSON-RPC를 호출하여
 * 객체를 리턴하는 함수 집합
 */
@Repository
public class BitcoinDao extends BlockchainDao implements Bitcoin0170Constant,
        BlockchainConstant, LockableWallet {

    @Getter
    @Value("${app.setting.pp}") private String passphrase;
    @Autowired
    protected Logger log;
    @Autowired
    protected ErrorRepository error;
    @Autowired
    protected TransactionRepository transactions;
    @Autowired
    protected EntityManagerFactory emf;
    private Gson gson = new Gson();

    /**
     *
     * @param ctx
     * @return
     * @throws Exception
     */
    public long getBestBlockCount(BitcoinService ctx) throws Exception {
        BitcoinLongResponse res = gson.fromJson(
                JsonRpcUtil.sendJsonRpcBasicAuth(ctx.getRpcUrl(),
                        METHOD_GETBLOCKCOUNT, null, ctx.getRpcId(),
                        ctx.getRpcPw()), BitcoinLongResponse.class);
        if (res.getError()==null) {
            return res.getResult();
        } else {
            throw new Exception(METHOD_GETBLOCKCOUNT
                    + ": " + res.getError());
        }
    }

    public double estimateFee(BitcoinService ctx) throws Exception {
        String[] params = new String[1];
        params[0] = "6";
        EstimateSmartFee res = gson.fromJson(
                JsonRpcUtil.sendJsonRpcBasicAuth(ctx.getRpcUrl(),
                        METHOD_ESTIMATESMARTFEE, params, ctx.getRpcId(),
                        ctx.getRpcPw()), EstimateSmartFee.class);
        if (res.getError()!=null) {
            throw new Exception(METHOD_ESTIMATESMARTFEE
                    + ": " + res.getError());
        } else {
            return res.getResult().getFeerate();
        }
    }

    // 수신주소가 중복이 있으면 SENDMANY 실패
    public BitcoinStringResponse sendMany(BitcoinService ctx,
                                          String uid, Map<String, Double> to) throws Exception {
        Object[] params = new Object[2];
        params[0] = "\"\"";
        DecimalFormat df = new DecimalFormat("#.########");
        // Iterate over send request count
        Iterator keys = to.keySet().iterator();
        StringBuilder param1 = new StringBuilder("{");
        List<String> toAddrs = new ArrayList<>();
        List<Double> toAmounts = new ArrayList<>();
        while(keys.hasNext()) {
            String key = (String) keys.next();
            param1.append("\"");
            param1.append(key);
            param1.append("\":");
            param1.append(df.format(to.get(key)));
            toAddrs.add(key);
            toAmounts.add(to.get(key));
            if (keys.hasNext()) {
                param1.append(",");
            }
        }
        param1.append("}");
        params[1] = param1.toString();
        // 전송 요청
        String resStr = JsonRpcUtil.sendJsonRpcBasicAuth(
                ctx.getRpcUrl() + uid, METHOD_SENDMANY, params,
                ctx.getRpcId(), ctx.getRpcPw());
        BitcoinStringResponse res = gson.fromJson(resStr, BitcoinStringResponse.class);
        if (res.getError()==null) {
            // ON SUCCESS
            // (String txid, long uid, String toAddr, double amount)
            for (int i=0; i<toAddrs.size(); i++) {
                TbTrans tx = new TbTrans(res.getResult(), uid,
                        toAddrs.get(i), toAmounts.get(i));
                tx.setTxid(res.getResult());
                transactions.save(tx);
            }
        } else {
            // Ex) "error":
            // {"code":-4, "message":"Transaction amount too small"},
            // {"code":-6, "message":"Account has insufficient funds"},
            TbError item = new TbError(METHOD_SENDMANY, res.getError());
            error.save(item);
        }
        return res;
    }

    public String getBlockHash(BitcoinService ctx, long startHeight)
            throws Exception {
        String[] params = new String[1];
        params[0] = "" + startHeight;
        // 시작블럭 해시 가져오기
        BitcoinStringResponse res = gson.fromJson(
                JsonRpcUtil.sendJsonRpcBasicAuth(ctx.getRpcUrl(),
                        METHOD_GETBLOCKHASH, params, ctx.getRpcId(),
                        ctx.getRpcPw()), BitcoinStringResponse.class);
        if (res==null) {
            log.error(METHOD_GETBLOCKHASH, "null");
            throw new Exception(METHOD_GETBLOCKHASH + ": Null");
        } else if (res.getError()!=null) {
            throw new Exception(METHOD_GETBLOCKHASH + ": " + res.getError());
        } else {
            return res.getResult();
        }
    }

    public LinkedTreeMap getAddressesByLabel(BitcoinService ctx, String walletId)
            throws Exception {
        String[] params = new String[1];
        params[0] = "\"\"";
        // 시작블럭 해시 가져오기
        BitcoinObjectResponse res = gson.fromJson(
                JsonRpcUtil.sendJsonRpcBasicAuth(ctx.getRpcUrl() + walletId,
                        METHOD_GETADDRBYLABEL, params, ctx.getRpcId(),
                        ctx.getRpcPw()), BitcoinObjectResponse.class);
        if (res==null) {
            log.error(METHOD_GETADDRBYLABEL, "null");
            throw new Exception(METHOD_GETADDRBYLABEL + ": Null");
        } else if (res.getError()!=null) {
            throw new Exception(METHOD_GETADDRBYLABEL + ": " + res.getError());
        } else {
            return res.getResult();
        }
    }
    /**
     * 해당 월렛의 모든 주소 반환
     * @param uid
     * @return
     * @throws Exception
     */
    @Override
    public List<String> getAllAddressListFromNode(BitcoinService ctx, String uid) {

        List<String> ret = new ArrayList<>();
        try {
            LinkedTreeMap addressMap = getAddressesByLabel(ctx, uid);
            Iterator<String> it = addressMap.keySet().iterator();
            while(it.hasNext()) {
                ret.add(it.next());
            }
        } catch (Exception e) {
            TbError item = new TbError("getDashboardData", e);
            error.save(item);
        }
        return ret;
    }

    @Override
    public Set<String> getAllAddressSetFromNode() {
        return null;
    }

    /**
     *
     * @param blockhash
     * @return
     */
    private Long getBlockHeight(BitcoinService ctx, String blockhash) {
        String[] params2 = new String[1];
        params2[0] = "\"" + blockhash + "\"";
        try {
            GetBlock res = gson.fromJson(
                    JsonRpcUtil.sendJsonRpcBasicAuth(ctx.getRpcUrl(), METHOD_GETBLOCK,
                            params2, ctx.getRpcId(), ctx.getRpcPw()), GetBlock.class);
            if (res.getError()!=null) {
                log.error(METHOD_GETBLOCK, res.getError());
                return null;
            } else {
                return res.getResult().getHeight();
            }
        } catch (Exception e) {
            log.error(METHOD_GETBLOCK, e);
            return null;
        }
    }

    public String[] listwallets (BitcoinService ctx)
            throws IOException {
        String resStr = JsonRpcUtil.sendJsonRpcBasicAuth(ctx.getRpcUrl(),
                METHOD_LISTWALLETS, null, ctx.getRpcId(), ctx.getRpcPw());
        return gson.fromJson(resStr, BitcoinStringArrayResponse.class).getResult();
    }

    public ListSinceBlock listSinceBlock(BitcoinService ctx,
                                         String walletId, String startBlockHash) throws IOException {
        String[] params = new String[1];
        params[0] = "\"" + startBlockHash + "\"";
        // 내 트랜잭션만 반환
        String resStr = JsonRpcUtil.sendJsonRpcBasicAuth(ctx.getRpcUrl()
                + walletId, METHOD_LISTSINCEBLOCK, params,
                ctx.getRpcId(), ctx.getRpcPw());
        return gson.fromJson(resStr, ListSinceBlock.class);
    }

    public GetWalletInfo getWalletInfo(BitcoinService ctx, String uid)
            throws IOException {

        String[] params1 = new String[2];
        params1[0] = "\"00000000\""; // Dummy PW to check
        params1[1] = "1";
        String resStr1 = JsonRpcUtil.sendJsonRpcBasicAuth(ctx.getRpcUrl()
                        + uid, METHOD_GETWALLETINFO, null,
                ctx.getRpcId(), ctx.getRpcPw());
        GetWalletInfo ret1 = gson.fromJson(resStr1, GetWalletInfo.class);

        String resStr2 = JsonRpcUtil.sendJsonRpcBasicAuth(ctx.getRpcUrl()
                        + uid, METHOD_WALLETPP, params1,
                ctx.getRpcId(), ctx.getRpcPw());
        BitcoinStringResponse ret2 = gson.fromJson(resStr2, BitcoinStringResponse.class);
        if (ret2.getError()!=null && ret2.getError().getCode()==ERR_CD_WALLET_UNENC) {
            ret1.getResult().setWalletlock(false);
        } else if (ret2.getError()==null) {
            ret1.getResult().setWalletlock(true);
        }
        return ret1;
    }

    public ListAddressGroupings listAddressGroupings(BitcoinService ctx,
                                                     String uid) throws IOException {
        String resStr = JsonRpcUtil.sendJsonRpcBasicAuth(ctx.getRpcUrl()
                        + uid, METHOD_LISTADDRESSGROUPINGS, null,
                ctx.getRpcId(), ctx.getRpcPw());
        return gson.fromJson(resStr, ListAddressGroupings.class);
    }

    /**
     * Set 송금수수료/Kb
     * @param ctx
     * @param param
     * @return
     * @throws Exception
     */
    public boolean setTxFee(BitcoinService ctx,
               SendToAddressRequest param) throws Exception {
        Object[] params  = new Object[1];
        DecimalFormat df = new DecimalFormat("#.########");
        params[0] = df.format(param.getCustomFee());
        String resStr = JsonRpcUtil.sendJsonRpcBasicAuth(
                ctx.getRpcUrl() + param.getUid(), METHOD_SETTXFEE,
                params, ctx.getRpcId(), ctx.getRpcPw());
        BitcoinBooleanResponse res = gson.fromJson
                (resStr, BitcoinBooleanResponse.class);
        if (res.getError()==null) {
            // ON SUCCESS
            return true;
        } else {
            TbError item = new TbError(METHOD_SETTXFEE,
                    res.getError());
            error.save(item);
            return false;
        }
    }

    public BitcoinStringResponse sendToAddress(BitcoinService ctx,
                      SendToAddressRequest param) throws Exception {

        Object[] params  = new Object[2];
        DecimalFormat df = new DecimalFormat("#.########");
        params[0] = "\"" + param.getToAddress() + "\"";
        params[1] = df.format(param.getAmount());
        // 노드에 전송 요청
        String resStr = JsonRpcUtil.sendJsonRpcBasicAuth(
                ctx.getRpcUrl() + param.getUid(), METHOD_SENDTOADDRESS,
                params, ctx.getRpcId(), ctx.getRpcPw());
        BitcoinStringResponse res = gson.fromJson
                (resStr, BitcoinStringResponse.class);
        if (res.getError()==null) {
            // ON SUCCESS
            TbTrans tx = new TbTrans(param);
            tx.setTxid(res.getResult());
            transactions.save(tx);
        } else {
            // Ex) "error":
            // {"code":-4, "message":"Transaction amount too small"},
            // {"code":-6, "message":"Account has insufficient funds"},
            TbError item = new TbError(METHOD_SENDTOADDRESS,
                    res.getError());
            error.save(item);
        }
        return res;
    }

    public void createWallet(BitcoinService ctx, String uid) throws Exception {
        String[] params = new String[1];
        params[0] = "\"" + uid + "\"";
        // result: { name , warning }
        try {
            String resStr = JsonRpcUtil.sendJsonRpcBasicAuth(ctx.getRpcUrl(),
                    Bitcoin0170Constant.METHOD_CREATEWALLET, params,
                    ctx.getRpcId(), ctx.getRpcPw());
            log.info(METHOD_CREATEWALLET, "res " + resStr);
            CreateWallet res = gson.fromJson(resStr, CreateWallet.class);
            if (res.getError()!=null) {
                throw new Exception(METHOD_CREATEWALLET
                        + " [" + res.getError().getCode() + "] "
                        + res.getError().getMessage());
            }
            String walletname = res.getResult().getName();
        } catch (Exception e) {
            TbError item = new TbError(METHOD_CREATEWALLET, e);
            error.save(item);
            throw e;
        }
    }

    /**
     * set 월렛 시드
     * @param ctx
     * @param uid
     * @return
     * @throws Exception
     */
    public void setHdSeed (BitcoinService ctx, String uid, String wifkey) throws Exception {
        String[] params = new String[2];
        params[0] = "true";
        params[1] = "\"" + wifkey + "\"";
        BitcoinStringResponse res = gson.fromJson(
                JsonRpcUtil.sendJsonRpcBasicAuth(ctx.getRpcUrl() + uid,
                        METHOD_SETHDSEED, params, ctx.getRpcId(),
                        ctx.getRpcPw()), BitcoinStringResponse.class);
        if (res.getError()==null) {
            log.info(METHOD_SETHDSEED, "" + res.getResult());
            return;
        } else {
            throw new Exception(METHOD_SETHDSEED
                    + ": " + res.getError());
        }
    }

    /**
     * 지갑 잠금 해제 for BATCH
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Override
    public boolean walletpassphrase(BitcoinService ctx) {
        return walletpassphrase(ctx, WALLET_UNLOCK_10SECS);
    }

    @Override
    public boolean walletpassphraseshort(BitcoinService ctx) {
        return walletpassphrase(ctx, WALLET_UNLOCK_SHORT);
    }

    public boolean walletpassphraseshort(BitcoinService ctx, String passphrase) {
        return walletpassphrase(ctx, passphrase, WALLET_UNLOCK_SHORT);
    }

    /**
     * 지갑 잠금 해제 for 단건
     * @param interval
     * @return
     */
    public boolean walletpassphrase(BitcoinService ctx, String pp, String interval) {
        // 1) WalletPassphrase => params: passphrase(string), seconds(number) => result: null
        BitcoinStringResponse res = null;
        String[] params = new String[2];
        params[0] = "\"" + pp + "\"";
        params[1] = interval;
        try {
            String resStr = JsonRpcUtil.sendJsonRpcBasicAuth(ctx.getRpcUrl(),
                    METHOD_WALLETPP, params, ctx.getRpcId(), ctx.getRpcPw());
            log.debug(METHOD_WALLETPP, ctx.getRpcId() + " "
                    + ctx.getRpcPw() + " " + resStr);
            res = gson.fromJson(resStr, BitcoinStringResponse.class);
            if (res.getError()!=null) {
                log.error(METHOD_WALLETPP, res.getError());
                return false;
            } else {
                log.success(METHOD_WALLETPP, "success");
                return true;
            }
        } catch (Exception e) {
            log.error(METHOD_WALLETPP, e);
            return false;
        }
    }

    /**
     * 지갑 잠금 해제 for 단건
     * @param interval
     * @return
     */
    @Override
    public boolean walletpassphrase(BitcoinService ctx, String interval) {
        // 1) WalletPassphrase => params: passphrase(string), seconds(number) => result: null
        BitcoinStringResponse res = null;
        String[] params = new String[2];
        params[0] = "\"" + getPassphrase() + "\"";
        params[1] = interval;
        try {
            String resStr = JsonRpcUtil.sendJsonRpcBasicAuth(ctx.getRpcUrl(),
                    METHOD_WALLETPP, params, ctx.getRpcId(), ctx.getRpcPw());
            log.debug(METHOD_WALLETPP, ctx.getRpcId() + " "
                    + ctx.getRpcPw() + " " + resStr);
            res = gson.fromJson(resStr, BitcoinStringResponse.class);
            if (res.getError()!=null) {
                log.error(METHOD_WALLETPP, res.getError());
                return false;
            } else {
                log.success(METHOD_WALLETPP, "success");
                return true;
            }
        } catch (Exception e) {
            log.error(METHOD_WALLETPP, e);
            return false;
        }
    }

    /**
     * 지갑 잠금
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Override
    public boolean walletlock(BitcoinService ctx, String uid) {
        // 1) WalletLock => params: null => result: null
        BitcoinObjectResponse res = null;
        try {
            String resStr = JsonRpcUtil.sendJsonRpcBasicAuth(ctx.getRpcUrl() + uid,
                    METHOD_WALLETLOCK, null,
                    ctx.getRpcId(), ctx.getRpcPw());
            res = gson.fromJson(resStr, BitcoinObjectResponse.class);
            if (res.getError()!=null) {
                log.error(METHOD_WALLETLOCK, res.getError());
                return false;
            } else {
                log.success(METHOD_WALLETLOCK, "success");
                return true;
            }
        } catch (Exception e) {
            log.error(METHOD_WALLETLOCK, e);
            return false;
        }
    }

    /**
     * 지갑 잠금
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
//    public void encryptwallet(BitcoinService ctx) {
//        // 1) WalletLock => params: null => result: null
//        new Thread(() -> {
//            String[] params = new String[1];
//            params[0] = "\"" + passphrase + "\"";
//            BitcoinObjectResponse res = null;
//            try {
//                String resStr = JsonRpcUtil.sendJsonRpcBasicAuth(ctx.getRpcUrl(),
//                        METHOD_ENCRYPTWALLET, params, ctx.getRpcId(), ctx.getRpcPw());
//                res = gson.fromJson(resStr, BitcoinObjectResponse.class);
//                if (res.getError() != null) {
//                    log.error(METHOD_ENCRYPTWALLET, res.getError());
//                } else {
//                    log.success(METHOD_ENCRYPTWALLET, "success");
//                }
//            } catch (Exception e) {
//                log.error(METHOD_ENCRYPTWALLET, e);
//            }
//        }).start();
//    }

    /**
     * 지갑 잠금
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public void encryptwallet(BitcoinService ctx, String uid, String lpassphrase) {
        // 1) WalletLock => params: null => result: null
        new Thread(() -> {
            String[] params = new String[1];
            params[0] = "\"" + lpassphrase + "\"";
            BitcoinObjectResponse res = null;
            try {
                String resStr = JsonRpcUtil.sendJsonRpcBasicAuth(ctx.getRpcUrl()
                                + uid,
                        METHOD_ENCRYPTWALLET, params, ctx.getRpcId(), ctx.getRpcPw());
                res = gson.fromJson(resStr, BitcoinObjectResponse.class);
                if (res.getError() != null) {
                    log.error(METHOD_ENCRYPTWALLET, res.getError());
                } else {
                    log.success(METHOD_ENCRYPTWALLET, "success");
                }
            } catch (Exception e) {
                log.error(METHOD_ENCRYPTWALLET, e);
            }
        }).start();
    }

}
