package ai.bitflow.bitwise.wallet.services.abstracts;

import ai.bitflow.bitwise.wallet.constants.Bitcoin0170Constant;
import ai.bitflow.bitwise.wallet.daos.BitcoinDao;
import ai.bitflow.bitwise.wallet.domains.TbBlockchainMaster;
import ai.bitflow.bitwise.wallet.domains.TbError;
import ai.bitflow.bitwise.wallet.domains.TbTrans;
import ai.bitflow.bitwise.wallet.domains.TbUserAddress;
import ai.bitflow.bitwise.wallet.domains.primarykeys.PkTxidCategoryToAddr;
import ai.bitflow.bitwise.wallet.gsonObjects.NewAddressResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.RpcError;
import ai.bitflow.bitwise.wallet.gsonObjects.apiParameters.PersonalRequest;
import ai.bitflow.bitwise.wallet.gsonObjects.apiParameters.SendToAddressRequest;
import ai.bitflow.bitwise.wallet.gsonObjects.apiResponse.*;
import ai.bitflow.bitwise.wallet.gsonObjects.bitcoin.*;
import ai.bitflow.bitwise.wallet.gsonObjects.common.AddressBalance;
import ai.bitflow.bitwise.wallet.gsonObjects.common.BitcoinDoubleResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.common.BitcoinStringResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.common.SystemInfo;
import ai.bitflow.bitwise.wallet.services.interfaces.OwnChain;
import ai.bitflow.bitwise.wallet.services.interfaces.SendManyWallet;
import ai.bitflow.bitwise.wallet.utils.JsonRpcUtil;
import com.google.gson.internal.LinkedTreeMap;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.*;


/**
 * Bitcoin 계열 공통 services
 * @author sungjoon.kim
 */
public abstract class BitcoinService extends BlockchainCommonService
        implements Bitcoin0170Constant, OwnChain, SendManyWallet {
  
    public abstract String getRpcId();
    public abstract String getRpcPw();
    public abstract String getUserAccount();

    @Autowired
    private BitcoinDao bitcoinDao;

    @Override public void beforeBatchSend() {}

    @Override
    public long getBestBlockCount() throws Exception {
        return bitcoinDao.getBestBlockCount(this);
    }

    public void getBlockchainData(Model model) {
        model.addAttribute("symbol", getSymbol().toUpperCase());
        model.addAttribute("networkType", isTestnet()?"Testnet":"Mainnet");
        model.addAttribute("isTestnet", isTestnet());
        boolean isDBCon = false;
        model.addAttribute("isDBCon", isDBCon);
    }

    public void getSettingData(Model model) {
//        model.addAttribute("symbol", getSymbol().toUpperCase());
//        model.addAttribute("networkType", isTestnet()?"Testnet":"Mainnet");
//        model.addAttribute("isTestnet", isTestnet());
    }

    /**
     * Cacheable
     * @return
     */
    public DashboardResponse getDashboardData() {
        DashboardResponse ret = new DashboardResponse();
        long startCount = getBlockStartFrom();
        long bestCount = -1;
        String[] wallets = null;
        try {
            bestCount = bitcoinDao.getBestBlockCount(this);
            wallets = bitcoinDao.listwallets(this);
            ret.getResult().setWalletCount(wallets.length);
        } catch (Exception e) {
            TbError item = new TbError("getDashboardData", e);
            error.save(item);
        }
        double totalBalance = 0;
        if (wallets!=null) {
            try {
                for (String item : wallets) {
                    GetWalletInfo walletInfo =
                            bitcoinDao.getWalletInfo(this, item);
                    totalBalance += walletInfo.getResult().getBalance();
                }
            } catch (IOException e) {
                log.error(METHOD_GETWALLETINFO, e.getMessage());
            }
        }
        long syncCount = -1;
        TbBlockchainMaster item = bitcoinDao.getBlockchainMaster(this);
        if (item!=null) {
            syncCount = item.getSyncHeight();
        }
        SystemInfo info = null;

        try {
            info = getSystemInfo();
        } catch (Exception e) {
            log.error("GET_SYSTEM_INFO", info.toString());
        }
        ret.setResult(startCount, syncCount, bestCount,
                totalBalance, info);
        return ret;
    }

    /**
     *
     * @return
     */
    public WalletAjaxResponse getWalletsInfo() {
        WalletAjaxResponse ret = new WalletAjaxResponse();
        try {
            String[] walletnames = bitcoinDao.listwallets(this);
            List<ai.bitflow.bitwise.wallet.gsonObjects.objects.Wallet> wallets
                    = new ArrayList<>();
            for (String walletname : walletnames) {
                // 잔고조회 + walletlock 체크
                // listaddressgroupings
                ai.bitflow.bitwise.wallet.gsonObjects.objects.Wallet wallet
                        = new ai.bitflow.bitwise.wallet.gsonObjects.objects.Wallet();
                wallet.setName(walletname);
                double balance = 0;
                boolean lock = false;
                wallet.setBalance(bitcoinDao.getWalletInfo(this, walletname)
                        .getResult().getBalance());
                wallet.setLock(bitcoinDao.walletlock(this, walletname));
                wallets.add(wallet);
            }
            ret.setResult(wallets);
        } catch (Exception e) {
            TbError item = new TbError("getDashboardData", e);
            error.save(item);
        }
        return ret;
    }

    public GetWalletInfo getWalletInfo(String uid) {
        GetWalletInfo ret = null;
        try {
            ret = bitcoinDao.getWalletInfo(this, uid);
            List<String> addresses = bitcoinDao.getAllAddressListFromNode(this, uid);
            ret.getResult().setAddresses(addresses);
            ret.getResult().setAddressCount(addresses.size());
        } catch (Exception e) {
            TbError item = new TbError("getDashboardData", e);
            error.save(item);
        }
        return ret;
    }

    public Page<TbTrans> getLastTransactions() {
        return bitcoinDao.getTransactions(0, 20);
    }

    public Page<TbTrans> getTransactions(String uid, int pageno, int size) {
        return bitcoinDao.getTransactions(uid, pageno-1, size);
    }

    /**
     *
     * @return
     */
    public GetBlockCountResponse getBlockCount() {
        GetBlockCountResponse ret = new GetBlockCountResponse();
        try {
            ret.setResult(bitcoinDao.getBestBlockCount(this));
        } catch (Exception e) {
            ret.setError("" + e.getMessage());
            TbError item = new TbError(METHOD_GETBLOCKCOUNT, e);
            error.save(item);
        }
        return ret;
    }

    public BitcoinDoubleResponse estimateFee() {
        BitcoinDoubleResponse ret = new BitcoinDoubleResponse();
        try {
            ret.setResult(bitcoinDao.estimateFee(this));
        } catch (Exception e) {
            ret.getError().setMessage("" + e.getMessage());
            TbError item = new TbError(METHOD_ESTIMATESMARTFEE, e);
            error.save(item);
        }
        return ret;
    }

    /**
     * 지갑 개수와 지갑 이름들 반환
     * @return
     */
    public ListWalletResponse listwallets() {
        ListWalletResponse ret = new ListWalletResponse();
        try {
            String[] wallets = bitcoinDao.listwallets(this);
            ret.setResult(wallets.length, wallets);
        } catch (Exception e) {
            ret.setError("" + e.getMessage());
            TbError item = new TbError(METHOD_LISTWALLETS, e);
            error.save(item);
        }
        return ret;
    }

//    public void encryptwallet() {
//        bitcoinDao.encryptwallet(this);
//    }

    public CreateWalletResponse createWallet(String uid, String passphrase) {
        return null;
    }


    /**
     * 1) 지갑 생성
     * 2) 니모닉 생성
     * 3) 프라이빗 키 추출
     * 4) setHDseed (시드)
     * 5) 지갑 암호화 (lockWallet)
     * 6) 새 주소 생성?
     * @param uid
     * @return mnemonic, address, privatekey
     */
    public CreateWalletResponse createDeterministicWallet(String uid, String passphrase) {
        CreateWalletResponse ret = new CreateWalletResponse();
        NetworkParameters params = null;
        if (isTestnet()) {
            params = TestNet3Params.get();
        } else {
            params = MainNetParams.get();
        }
        // 1) 니모닉 생성
        Wallet wallet = Wallet.createDeterministic(params,
                Script.ScriptType.P2PKH);
        DeterministicSeed seed = wallet.getKeyChainSeed();
        // 2) 주소 생성
        String address = wallet.currentReceiveAddress().toString();
        // 3) 프라이빗 키 생성
        DeterministicKey key = wallet.currentReceiveKey();
        String privateKey = key.getPrivateKeyAsWiF(params);
        String mnemonic = Utils.SPACE_JOINER.join(seed.getMnemonicCode());
        log.debug(METHOD_CREATEWALLET, "mnemonicCode: " + mnemonic);
        log.debug(METHOD_CREATEWALLET, "seed: " + seed.toString());
        log.debug(METHOD_CREATEWALLET, "address: " + address);
        log.debug(METHOD_CREATEWALLET, "privatekey: " + privateKey);
        String shaHash = Sha256Hash.of(mnemonic.getBytes(Charset.forName("UTF-8")))
                .toString();
        try {
            // 4) 지갑생성
            bitcoinDao.createWallet(this, uid);
            // 5) HD 시드 변경
            bitcoinDao.setHdSeed(this, uid, privateKey);
            ret.setResult(address, privateKey, mnemonic);
            bitcoinDao.encryptwallet(this, uid, passphrase);
            // 6) DB에 저장
            TbUserAddress datum = new TbUserAddress(uid, address);
            datum.setMnemonicEnc(shaHash);
            userAddresses.save(datum);
            ret.setCode(CODE_SUCCESS);
            ret.getResult().setAddress(address);
            return ret;
        } catch (Exception e) {
            log.error(METHOD_CREATEWALLET, e.getMessage());
            ret.setCode(-1);
            ret.setError(e.getMessage());
            return ret;
        }
    }

	/**
	 * 새 주소 발급
	 * @param
	 */
    @Transactional
    @Override
    public NewAddressResponse newAddress(PersonalRequest req) {
	  
        NewAddressResponse ret = new NewAddressResponse();

        // CASE2) 새 요청인 경우 생성 => param[0]: 어카운트ID
	    String resStr = null;
	    BitcoinStringResponse res = null;

//        1. label           (string, optional, default="")
//          The label name for the address to be linked to. It can also be set to the
//          empty string "" to represent the default label. The label does not need to exist,
//          it will be created if there is no label by the given name.
//        2. address_type    (string, optional, default=set by -addresstype)
//          The address type to use. Options are "legacy", "p2sh-segwit", and "bech32".
        String[] params = new String[1];
        params[0] = "\"" + req.getUid() + "\"";

        try {
	    	bitcoinDao.walletpassphraseshort(this);
    	    resStr = JsonRpcUtil.sendJsonRpcBasicAuth(getRpcUrl()
                    + req.getUid(), METHOD_NEWADDR, params,
                    getRpcId(), getRpcPw());
    	    log.info(METHOD_NEWADDR, resStr);
    	    res = gson.fromJson(resStr, BitcoinStringResponse.class);
    	    if (res.getError()!=null) {
                log.error(METHOD_NEWADDR, res.getError());
                ret.setCode(CODE_FAIL_LOGICAL);
                // e.g.) [-18]Requested wallet does not exist or is not loaded
                ret.setError("[" + res.getError().getCode() + "] "
                        + res.getError().getMessage());
                return ret;
            }
    	    TbUserAddress datum = new TbUserAddress(
                    req.getUid(), res.getResult());
    	    userAddresses.save(datum);
    	    ret.setResult(res.getResult());
    	    
	    } catch (Exception e) {
            TbError item = new TbError(METHOD_NEWADDR, e);
            error.save(item);
            ret.setError(e.getMessage());
            return ret;
	    }
	    return ret;
	}
    
    @Override
    public boolean isOwnerAddressExists() {
        return false;
    }
    
	/**
	 * 주소 유효성 검증
	 */
    @Override public ValidateAddressResponse validateAddress(
            PersonalRequest param) {
      
    	String resStr = null;
    	ValidateAddressResponse ret = new ValidateAddressResponse(param);
    	String[] params = new String[1];
    	params[0] = "\"" + ret.getResult().getAddress() + "\"";
    	ValidateAddress res = null;
    	try {
    	    resStr = JsonRpcUtil.sendJsonRpcBasicAuth(getRpcUrl(),
                    METHOD_VALIDATEADDRESS, params,
    	    		getRpcId(), getRpcPw());
    	    res = gson.fromJson(resStr, ValidateAddress.class);
    	    log.info(METHOD_VALIDATEADDRESS, resStr);
    	    if (res.getError()!=null) {
    	        // 정상: 응답갑 재확인 필요
                log.error(METHOD_VALIDATEADDRESS, res.getError());
                ret.setCode(CODE_FAIL_LOGICAL);
                ret.setError(res.getError().getMessage());
                return ret;
            }
        } catch (Exception e) {
            TbError item = new TbError(METHOD_VALIDATEADDRESS, e);
            error.save(item);
            return ret;
        }
    	ret.getResult().setValid(res.getResult().isIsvalid());
        return ret;
    }

    /**
     * 입출금 트랜잭션 조회
     */
    @Override
    public Object getTransaction(String uid, String txid) {
    	String[] params = new String[1];
    	params[0] = "\"" + txid + "\"";
        GetTransaction res = null;
    	try {
            String resStr = JsonRpcUtil.sendJsonRpcBasicAuth(getRpcUrl() + uid,
                    METHOD_GETTRANSACTION, params, getRpcId(),
                    getRpcPw());
            res = gson.fromJson(resStr, GetTransaction.class);
            if (res.getError()!=null) {
                TbError item = new TbError(METHOD_GETTRANSACTION,
                        res.getError().getCode(), res.getError().getMessage());
                error.save(item);
            } else if (res.getResult().getConfirmations()< getMinConfirm()) {
                res.getResult().setStatus('P');
            } else {
                res.getResult().setStatus('Y');
            }
            return res;
    	} catch (Exception e) {
            TbError item = new TbError(METHOD_GETTRANSACTION, e);
            error.save(item);
    	    res = new GetTransaction(new RpcError(-1, e.getMessage()));
    	    return res;   
    	}
    }

    /**
     * 1) setTxFee
     * 2) sendToAddress
     * @param param
     * @return
     * @throws Exception
     */
    public SendToAddressResponse sendToAddress(SendToAddressRequest param)
            throws Exception {

        SendToAddressResponse ret = new SendToAddressResponse();
        boolean success = false;
        if (param.getCustomFee()>0) {
            success = bitcoinDao.setTxFee(this, param);
            if (!success) {
                ret.setCode(500);
                ret.setError(METHOD_SETTXFEE);
                return ret;
            }
        }
        success = bitcoinDao.walletpassphraseshort(this, param.getPp());
        if (!success) {
            ret.setCode(500);
            ret.setError(WALLET_UNLOCK_SHORT);
            return ret;
        }
        BitcoinStringResponse res = bitcoinDao.sendToAddress
                (this, param);
        if (res.getError()==null) {
            ret.setResult(res.getResult());
        } else {
            ret.setCode(res.getError().getCode());
            ret.setError(res.getError().getMessage());
            TbError item = new TbError(METHOD_SENDTOADDRESS,
                    res.getError().getMessage());
            error.save(item);
        }
        return ret;
    }

    /**
     * BTC sendmany 멀티 전송
     * (!) 수신자 주소가 같은 것이 있으면(중복) 에러가 발생하므로 중복 수신주소는 1개만 전송해야 함
     * 20개 정도 보내는 것이 안전할 듯
     * SendMany => params: fromAccount, to{address(string-base58):amount(double),,,,} => result(txid:string)
     *        {"1yeTWjh876opYp6R5VRj8rzkLFPE4dP3Uw":10,"1yeTWjh876opYp6R5VRj8rzkLFPE4dP3Uw":15}
     * [-8] Invalid parameter, duplicated address
     */
    @Override
    public SendToAddressResponse sendMany(String uid, String passphrase, Map to)
            throws Exception {
        SendToAddressResponse ret = new SendToAddressResponse();
        boolean success = bitcoinDao.walletpassphraseshort(this, passphrase);
        if (!success) {
            ret.setCode(500);
            ret.setError(WALLET_UNLOCK_SHORT);
            return ret;
        }
        BitcoinStringResponse res = bitcoinDao.sendMany(this, uid, to);
        if (res.getError()==null) {
            ret.setResult(res.getResult());
        } else {
            ret.setCode(res.getError().getCode());
            ret.setError(res.getError().getMessage());
        }
        return ret;
    }

    /**
     * listsinceblock으로 단순하게 대체 가능
     * 비트 0.15.0 이후 각 월렛마다 반복
     */
    @Override
    public boolean openBlocksGetTxsThenSave() {
      
        TbBlockchainMaster master = bitcoinDao.getBlockchainMaster(this);
        long startBlockNo = master.getSyncHeight();
        long bestBlockNo = master.getBestHeight();

        if (startBlockNo>bestBlockNo) {
            // 동기화 할 블럭이 없으면 리턴
            log.debug("SYNC", "start: " + startBlockNo + " > end: "
                    + bestBlockNo + " skip");
            return true;
        } else {
            log.info("SYNC", startBlockNo + " -> " + bestBlockNo);
            String startBlockHash = null;
            try {
                startBlockHash = bitcoinDao.getBlockHash(this, startBlockNo);
                if (startBlockHash==null) { return false; }
            } catch (Exception e) {
                TbError item = new TbError(METHOD_GETBLOCKHASH, e);
                error.save(item);
                return false;
            }

            // 모든 월렛명 가져오기
            String[] uidStrArr = null;
            try {
                uidStrArr = bitcoinDao.listwallets(this);
//                TbUserAddress address = new TbUserAddress();
            } catch (Exception e) {
                TbError item = new TbError(METHOD_LISTWALLETS, e);
                error.save(item);
                return false;
            }

            List<TbTrans> trans = new ArrayList<>();
            // 월렛마다 반복
            for (String uidStr : uidStrArr) {
                // 최근 동기화 블럭 인덱스 부터 최근까지 트랜잭션 반환
                ListSinceBlock.Result.Transaction[] txs = null;
                try {
                    ListSinceBlock res1 = bitcoinDao.listSinceBlock(
                            this, uidStr, startBlockHash);
                    txs = res1.getResult().getTransactions();
                } catch (Exception e) {
                    TbError item = new TbError(METHOD_LISTSINCEBLOCK, e);
                    error.save(item);
                    return false;
                }

                if (txs== null || txs.length < 1) { continue; }

                // 거래건이 있으면 WALLET트랜잭션 반복해서 SEND/RECEIVE인지 판단한 뒤 저장
                try {
                    for (ListSinceBlock.Result.Transaction tx : txs) {
                        // 마지막 동기화 블럭 이후의 모든 (나의) 트랜잭션 반복
                        double amount = Math.abs(tx.getAmount());
                        long time = tx.getTime();
                        long confirm = tx.getConfirmations();
                        String txid = tx.getTxid();
                        String address = tx.getAddress();
                        String category = tx.getCategory();
//                            String account   = tx.getLabel();
//                            Long blockid     = getBlockHeight(tx.getBlockhash());
                        log.debug("tx toString()", tx.toString());
                        // 입출금 공통 처리
                        PkTxidCategoryToAddr id = new PkTxidCategoryToAddr(txid, category);
                        Optional<TbTrans> rawtrans = transactions.findById(id);
                        TbTrans item = null;
                        if (rawtrans.isPresent()) {
                            item = rawtrans.get();
                        } else {
                            item = new TbTrans(category, txid,
                                    uidStr, address, amount);
                        }
                        if (time>0) {
                            item.setTxTime(time);
                        }
                        item.setConfirm(confirm);
                        if (CATEGORY_SEND.equals(category)) {
                            // 1) 출금처리 START >> send의 경우만 fee와 abandoned가 존재
                            item.setFee(Math.abs(tx.getFee()));
                            if (tx.isAbandoned()) { item.setError(MSG_SEND_ABANDONED); }
                            // 출금처리 END
                        } else if (CATEGORY_RECEIVE.equals(tx.getCategory())) {
                            // 2) 입금처리 START >> coinbase 마이닝 되는 경우 간헐적 존재
                            // 입금처리 END
                        }
                        item.setUid(uidStr);
                        trans.add(item);
                    }
                    // tx array loop end
                } catch (Exception e) {
                    TbError item = new TbError("ExtractingTXs", e);
                    error.save(item);
                    return false;
                }
            }
            // 월렛 반복 END
            EntityManager em = emf.createEntityManager();
            EntityTransaction etx = em.getTransaction();
            try {
                etx.begin();
                transactions.saveAll(trans);
                master.setSyncHeight(bestBlockNo);
                blockchainMaster.save(master);
                etx.commit();
                log.info("SYNC", "finished at " + bestBlockNo);
            } catch (Exception e) {
                etx.rollback();
            } finally {
                em.close();
            }
            return true;
        }
    }

    /**
     * 
     * @param datum
     * @return
     */
    @Override
    public boolean sendOneTransaction(TbTrans datum) {
        Object[] params  = new Object[3];
        DecimalFormat df = new DecimalFormat("#.########");
        params[0] = "\"" + datum.getUid() + "\"";
        params[1] = "\"" + datum.getToAddr() + "\"";
        params[2] = df.format(datum.getAmount()); 
        // 노드에 전송 요청
        BitcoinStringResponse res = null;
        try {
            String resStr = JsonRpcUtil.sendJsonRpcBasicAuth(getRpcUrl(),
                    METHOD_SENDTOADDRESS, params, getRpcId(), getRpcPw());
            res = gson.fromJson(resStr, BitcoinStringResponse.class);
            if (res.getError()!=null) {
                // Ex) "error": {"code":-4, "message":"Transaction amount too small"},
                //     {"code":-6, "message":"Account has insufficient funds"},
                log.error(METHOD_SENDTOADDRESS, res.getError());
                datum.setError("[" + res.getError().getCode() + "] " + res.getError().getMessage());
                datum.setTxid(res.getResult());;
                return false;
            } else {
                // ON SUCCESS
                datum.setTxid(res.getResult());
                return true;
            }
        } catch (Exception e) {
            // FAIL시 NOTIFY
            log.error(METHOD_SENDTOADDRESS, e);
            datum.setError("[-1]" + e.getMessage());
            return false;
        }
    }

    @Override
    public GetBalanceResponse getBalance(String uid) throws Exception {
        GetBalanceResponse res = new GetBalanceResponse();
        GetWalletInfo wallet = bitcoinDao.getWalletInfo(this, uid);
        log.debug("getWalletInfo", wallet.toString());
        ListAddressGroupings balances = bitcoinDao.listAddressGroupings
                (this, uid);
        log.debug("listAddressGroupings", balances.toString());
        if (res.getError()!=null) {
            // {"result":null,"error":{"code":-18,"message":"Requested wallet does not exist or is not loaded"},"id":1}
        }

        List<AddressBalance> list = new ArrayList<>();
        if (wallet.getResult()!=null) {
            res.getResult().setBalance(wallet.getResult().getBalance());
            res.getResult().setUnconfirmedBalance(
                    wallet.getResult().getUnconfirmed_balance());
            if (balances.getResult()!=null && balances.getResult().length>0) {
                for (int j = 0; j < balances.getResult().length; j++) {
	            	for (int i = 0; i < balances.getResult()[j].length; i++) {
	                    Object[] raw = balances.getResult()[j][i];
	                    AddressBalance item = new AddressBalance();
	                    item.setAddress((String) raw[0]);
	                    item.setBalance((double) raw[1]);
	                    log.debug("arr", item.toString());
	                    list.add(item);
	                }
                }
            }
        }
        res.getResult().setAddressBalances(list);
        return res;
    }
    
    /**
     * 출금 CONFIRM수 변경 알림
     */
    @Transactional
    @Override public boolean updateTxConfirmCount() {
        boolean success = true;
        // 1) 출금 진행중인 건 조회: TXID가 있고 거래소에 알리지 않은 건
        List<TbTrans> data = getSendTXToUpdate();
        if (data!=null && data.size()>0) {
            for (TbTrans datum : data) {
                boolean notify = false;
                GetTransaction res = (GetTransaction)getTransaction(datum.getUid(),
                        datum.getTxid());
                if (res.getError()!=null) {
                    // 에러 코드가 -1이면 노드에러가 아니고 IO익셉션인 경우이므로 다음번에 재발송 될 가능성 있음
                    TbError item = new TbError(METHOD_GETTRANSACTION,
                            res.getError());
                    error.save(item);
                    success = false;
                } else {
                    if (res.getResult().getConfirmations()>= getMinConfirm()) {
                        // 완료처리해야 할 것 있는지 조회
                        datum.setNotiCnt(NOTI_CNT_FINISHED);
                        notify = true;
                    } else {
                        // 미완료 건 중 알림회수 0인 경우
                        if (datum.getNotiCnt()<NOTI_CNT_PROGRESS) {
                            datum.setNotiCnt(NOTI_CNT_PROGRESS);
                            notify = true;
                        }
                    }
                    datum.setFee(Math.abs(res.getResult().getFee()));
                    datum.setTxTime(res.getResult().getTime());
                    datum.setConfirm(res.getResult().getConfirmations());
                }
                if (notify) {
                    bitcoinDao.notifyFrontEnd(datum);
                }
                transactions.save(datum);
            }
        }
        return success;
    }

}
