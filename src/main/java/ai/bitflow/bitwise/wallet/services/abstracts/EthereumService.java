package ai.bitflow.bitwise.wallet.services.abstracts;

import ai.bitflow.bitwise.wallet.constants.EthereumConstant;
import ai.bitflow.bitwise.wallet.domains.TbBlockchainMaster;
import ai.bitflow.bitwise.wallet.domains.TbTrans;
import ai.bitflow.bitwise.wallet.domains.TbUserAddress;
import ai.bitflow.bitwise.wallet.gsonObjects.NewAddressResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.RpcError;
import ai.bitflow.bitwise.wallet.gsonObjects.apiParameters.PersonalRequest;
import ai.bitflow.bitwise.wallet.gsonObjects.apiResponse.GetBalanceResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.apiResponse.ValidateAddressResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.common.BitcoinBooleanResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.common.BitcoinObjectResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.common.BitcoinStringResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.ethereum.EthBlock;
import ai.bitflow.bitwise.wallet.gsonObjects.ethereum.EthListAccount;
import ai.bitflow.bitwise.wallet.gsonObjects.ethereum.EthTransaction;
import ai.bitflow.bitwise.wallet.gsonObjects.ethereum.EthTxReceipt;
import ai.bitflow.bitwise.wallet.utils.ConvertUtil;
import ai.bitflow.bitwise.wallet.utils.JsonRpcUtil;
import com.google.gson.JsonSyntaxException;
import ai.bitflow.bitwise.wallet.services.interfaces.LockableAddress;
import ai.bitflow.bitwise.wallet.services.interfaces.OwnChain;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;
import org.web3j.utils.Numeric;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;


public abstract class EthereumService extends BlockchainCommonService implements
        EthereumConstant, OwnChain, LockableAddress {
  
    private BigInteger gasPrice;
    private Web3j web3j;
    public Web3j getWeb3j() {
        if (web3j==null) { web3j = Web3j.build(new HttpService(getRpcUrl())); }
        return web3j;
    }
    @Autowired
    private EntityManagerFactory emf;
    
    /**
     * 주소를 발급하고 DB에 저장 후 반환
     * 0x3cc4f01623de56d8fb204c4fd8a81ecab3724410
     * @param
     */
    @Transactional
    @Override public NewAddressResponse newAddress(PersonalRequest req) {
      
        NewAddressResponse ret = getSavedAddress(req);
//        if (ret.getResult().getAddress()!=null) { return ret; }
//        ret.setResult(req);
//
//        // CASE2) 새 요청인 경우 생성
//        BitcoinStringResponse res = null;
//        String[] params = new String[1];
//        params[0] = "\"" + getPp() + "\"";
//        try {
//            String resStr = JsonRpcUtil.sendJsonRpc2(getRpcUrl(), METHOD_NEWADDR,
//                    params);
//            res = gson.fromJson(resStr, BitcoinStringResponse.class);
//            if (res.getError()!=null) {
//                log.error(METHOD_NEWADDR, res.getError());
//                ret.setCode(CODE_FAIL_LOGICAL);
//                ret.setError(res.getError().getMessage());
//                return ret;
//            }
//        } catch (Exception e) {
//            log.error(METHOD_NEWADDR, e);
//            ret.setCode(CODE_FAIL_LOGICAL);
//            ret.setError(e.getMessage());
//            return ret;
//        }
//        TbUserAddress datum = new TbUserAddress(
//                ret.getResult().getUid(), res.getResult());
//        userAddresses.save(datum);
//        ret.setCode(CODE_SUCCESS);
//        ret.getResult().setAddress(res.getResult());
        return ret;      
    }

    private String[] getAllAddressArray() {
        String method = SYMBOL_ETC.equals(getSymbol())?METHOD_ACCOUNTS:METHOD_LISTACCOUNTS;
        EthListAccount res = null;
        try {
            String resStr = JsonRpcUtil.sendJsonRpc2(getRpcUrl(), method, null); //
            res = gson.fromJson(resStr, EthListAccount.class);
            if (res.getError()!=null) {
                return null;
            } else {
                return res.getResult();
            }
        } catch (IOException | IllegalStateException | JsonSyntaxException e) {
              log.error(METHOD_LISTACCOUNTS, e);
              return null;
        }
    }
    
    @Override public List<String> getAllAddressListFromNode() {
        String[] ret = getAllAddressArray();
        if (ret==null) { return null; }
        return (List<String>) Arrays.asList(ret);
    }
    
    @Override public Set<String> getAllAddressSetFromNode() {
        // gets all addresses from ETH node
        String[] userarr = getAllAddressArray();
        if (userarr==null) { return null; }
        Set<String> users = new HashSet<>();
        for (String user : userarr) {
            users.add(user);
        }
        return users;
    }
    
    /**
     * Any 42 character string starting with 0x, and following with 0-9, A-F, a-f (valid hex characters)
     * represent a valid Ethereum address. Pattern is ^(0x)?[a-zA-Z0-9]*$
     */
    @Override public ValidateAddressResponse validateAddress(
    		PersonalRequest param) {
        ValidateAddressResponse ret = new ValidateAddressResponse(param);
        if (WalletUtils.isValidAddress(ret.getResult().getAddress())) {
            ret.getResult().setValid(true);
        } else {
            ret.getResult().setValid(false);
        }
        return ret;
    }
    
    @Override public boolean isOwnerAddressExists() {
    	List<String> alladdrs = getAllAddressListFromNode();
    	if (alladdrs!=null && alladdrs.contains(getOwnerAddress())) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * raw call examples METHOD_GETBALANCE (eth_getBalance)
     * {"jsonrpc":"2.0","method":"eth_call","params":[
     * {"to": "0xee74110fb5a1007b06282e0de5d73a61bf41d9cd", 
     * "data":"0x70a08231000000000000000000000000c9fbb1691def4d2a54a3eb22e0164359628aa98b"}
     * , "latest"],"id":67}
     */
    @Override
    public GetBalanceResponse getBalance(String addr) {
        return null;
//        if (addr==null) { return 0; }
//        BigInteger res = null;
//        try {
//            res = getWeb3j().ethGetBalance(addr,
//            		DefaultBlockParameterName.LATEST).send().getBalance();
//            return Convert.fromWei(new BigDecimal(res), Unit.ETHER).doubleValue();
//        } catch (Exception e) {
//            log.error("balanceOf", e);
//            return 0;
//        }
    }
    
    /**
     * Long lastGasPrice, Long gasLimit
     * @param datum
     * @return
     */
    @Override
    public boolean sendOneTransaction(TbTrans datum) {
      
        BitcoinStringResponse res = null;
        JSONObject params = new JSONObject();
        
        String from = (datum.getFromAddr()==null||"".equals(datum.getFromAddr()))? getOwnerAddress()
        		:datum.getFromAddr();
        double amountEth = datum.getAmount();
        params.put("from",     from);
        params.put("to",       datum.getToAddr());
        params.put("value",    ConvertUtil.ethToWeiHex(amountEth)); 		      // 단위 WEI
        params.put("gas",      Numeric.toHexStringWithPrefix(getGasLimit())); // 단위 WEI
        params.put("lastGasPrice", Numeric.toHexStringWithPrefix(ConvertUtil.bigIntegerAddFirst(gasPrice))); // 단위 WEI
        
        JSONArray paramArr = new JSONArray();
        paramArr.put(params);
        // 노드에 전송 요청
        try {
            String resStr = JsonRpcUtil.sendJsonRpcJson(getRpcUrl(),
                    METHOD_SENDFROMADDR, paramArr);
            res = gson.fromJson(resStr, BitcoinStringResponse.class);
            if (res.getError()!=null) {
                log.error(METHOD_SENDFROMADDR, res.getError());
                datum.setError("[" + res.getError().getCode() + "] "
                			+ res.getError().getMessage());
                datum.setTxid(res.getResult());
                return false;
            } else {
                datum.setTxid(res.getResult());
                return true;
            }
        } catch (Exception e) {
            log.error(METHOD_SENDFROMADDR, e);
            datum.setError("[-1] " + e.getMessage());
            return false;
        }
    }

    /**
     * 출금 CONFIRM수 변경 알림
     */
    @Transactional
    @Override public boolean updateTxConfirmCount() {
        return updateSendConfirm(getSymbol());
    }
    
    public boolean updateSendConfirm(String symbol) {
        
        // 1) 출금 진행중인 건 조회: TXID가 있고 거래소에 알리지 않은 건
        boolean success = true;
        TbBlockchainMaster master = commonDao.getBlockchainMaster(this);
        long lastestHeight = master.getBestHeight();
        List<TbTrans> data = getSendTXToUpdate(symbol);
        
        if (data!=null && data.size()>0) {
            for (TbTrans datum : data) {
                boolean notify = false;
                long currHeight = 0, confirm = 0;
                // 트랜잭션 confirm 조회
                EthTxReceipt res = getTransaction("1", datum.getTxid());
                if (res.getError()!=null) {
                	// 트랜잭션 에러
                    String errMsg = "[" + res.getError().getCode() + "] " 
                    				+ res.getError().getMessage();
                    log.error(METHOD_GETTX, errMsg);
                    if (res.getError().getCode()==-1) {
                        // 에러 코드가 -1이면 노드에러가 아니고 IO익셉션인 경우이므로 다음번에 재발송 될 가능성 있음
                        continue;
                    } else {
                    	// 복구할 수 없는 에러인 경우
                        datum.setError(errMsg);
                        datum.setNotiCnt(NOTI_CNT_FINISHED);
                        notify = true;
                    }
                    success = false;
                } else if (res.getResult()!=null && ERR_ETH_TX_FAIL.equals(
                		res.getResult().getStatus())) {
                    // 실패한 트랜잭션
                    datum.setNotiCnt(NOTI_CNT_FINISHED);
                    datum.setError(MSG_BLOCK_TX_FAIL);
                    success = false;
                    notify = true;
                } else {
                    // 성공한 트랜잭션
                    try {
                        currHeight = ConvertUtil.hexToLong(
                        		res.getResult().getBlockNumber());
                    } catch (NullPointerException e) {
                        log.error("blocknumber", "is null");
                        // 주로 펜딩 프랜잭션
                        // Todo: 어떻게 처리할지 고민
                        continue;
                    }
                    confirm = lastestHeight - currHeight;
                    datum.setConfirm(confirm);
                    if (res.getResult()!=null) {
                        datum.setFee(Convert.fromWei(
                        		new BigDecimal(Numeric.toBigInt(res.getResult().getGasUsed()))
                        		, Unit.ETHER).doubleValue());
                    }
                    // 미완료 건 중 알림회수 0인 경우
                    if (datum.getNotiCnt()<NOTI_CNT_PROGRESS) {
                    	// 미완료 건 중 알림회수 0인 경우
                        datum.setNotiCnt(NOTI_CNT_PROGRESS);
                        notify = true;
                    } else if (confirm>= getMinConfirm() && datum.getNotiCnt()<NOTI_CNT_FINISHED) {
                        // confirm 완료된 건 중 완료알림 처리해야 할 것 있는지 조회
                        datum.setNotiCnt(NOTI_CNT_FINISHED);
                        notify = true;
                    }
                }
                if (notify) {
                    commonDao.notifyFrontEnd(datum);
                }
                transactions.save(datum);
            }
        }
        return success;
    }
    
    @Override
    public EthTxReceipt getTransaction(String uid, String txid) {
        String[] params = new String[1];
        params[0] = "\"" + txid + "\"";
        try {
            String resStr = JsonRpcUtil.sendJsonRpc2(getRpcUrl(), METHOD_TXRECEIPT,
                    params);
            return gson.fromJson(resStr, EthTxReceipt.class);
        } catch (Exception e) {
            return new EthTxReceipt(new RpcError(-1, e.getMessage()));
        }
    }
    
    public long getBestBlockCount() {
        BitcoinStringResponse res = null;
        try {
            String resStr = JsonRpcUtil.sendJsonRpc2(getRpcUrl(),
            		METHOD_GETBLOCKCOUNT, null);
            res = gson.fromJson(resStr, BitcoinStringResponse.class);
            if (res.getError()!=null) {
                log.error(METHOD_GETBLOCKCOUNT, res.getError());
                return 0;
            } else {
                return Long.decode(res.getResult());
            }
        } catch (Exception e) {
            // 에러 HTML 502 Bad Gateway 등 =>
            // com.google.gson.JsonSyntaxException: java.lang.IllegalStateException
            log.error(METHOD_GETBLOCKCOUNT, e);
            return 0;
        }
    }
    
    public EthBlock getBlockByHash(String hash) {
        EthBlock res = null;
        String[] params = new String[2];
        params[0] = "\"" + hash + "\"";
        params[1] = "true";
        try {
            String resStr = JsonRpcUtil.sendJsonRpc2(getRpcUrl(), METHOD_BLOCKBYHASH, params);
            res = gson.fromJson(resStr, EthBlock.class);
            if (res.getError()!=null) {
                log.error(METHOD_BLOCKBYHASH, res.getError());
                return null;
            } else {
                return res;
            }
        } catch (Exception e) {
            // 에러 HTML 502 Bad Gateway 등 => com.google.gson.JsonSyntaxException: java.lang.IllegalStateException
            log.error(METHOD_BLOCKBYHASH, e);
            return null;
        }
    }
    
    @Transactional
    @Override public void beforeBatchSend() {
        gasPrice = getGasPrice();
        if (gasPrice.compareTo(BigInteger.ZERO)==1) {
        	// 0보다 크면
            TbBlockchainMaster master = commonDao.getBlockchainMaster(this);
            master.setLastGasPrice(Convert.fromWei(new BigDecimal(gasPrice),
            		Unit.GWEI).doubleValue());
//            blockchainMaster.save(master);
        }
    }

    /**
     * BigInteger.valueOf(90000).longValue();
     * @return
     */
    public BigInteger getGasLimit() {
        return BigInteger.valueOf(200000); 
    }
    
    /**
     * @return
     */
    public synchronized BigInteger getGasPrice() {
        try {
            String resStr = JsonRpcUtil.sendJsonRpc2(getRpcUrl(), METHOD_GAS_PRICE,
                    null);
            BitcoinStringResponse res = gson.fromJson(resStr, BitcoinStringResponse.class);
            if (res.getResult()!=null) {
                return Numeric.decodeQuantity(res.getResult());
            } else {
                log.error(METHOD_GAS_PRICE, res.getError());
                return BigInteger.ZERO;
            }
        } catch(Exception e) {
            log.error(METHOD_GAS_PRICE, e);
            return BigInteger.ZERO;
        }
    }
    
    @Override public boolean walletpassphraseWithAddress(String address) {
    	BitcoinBooleanResponse res = null;
        String[] params = new String[3];
        params[0] = "\"" + address + "\"";
        params[1] = "\"" + getPp() + "\"";
        params[2] = "null";
//        params[2] = WALLET_UNLOCK_SHORT; // 패리티에서 에러 발생
//        params[2] = "\"" + Numeric.toHexStringWithPrefix(new BigInteger(WALLET_UNLOCK_SHORT)) + "\"";
        
        try {
            String resStr = JsonRpcUtil.sendJsonRpc2(getRpcUrl(), METHOD_WALLETPP,
                    params);
            res = gson.fromJson(resStr, BitcoinBooleanResponse.class);
            if (res.getError()!=null) {
            	log.error("walletpassphraseWithAddress", res.getError());
                log.error(METHOD_WALLETPP, res.getError());
                return false;
            } else {
                boolean success = res.isResult();
                if (success) {
                    log.success(METHOD_WALLETPP, "success");
                    return true;
                } else {
                    return false;
                }
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
    @Override public boolean walletlock(String address) {
        BitcoinObjectResponse res = null;
        String[] params = new String[1];
        params[0] = "\"" + address + "\"";
        try {      
            String resStr = JsonRpcUtil.sendJsonRpc2(getRpcUrl(), METHOD_WALLETLOCK,
                    params);
            res = gson.fromJson(resStr, BitcoinObjectResponse.class);
            if (res.getError()==null) {
                log.success(METHOD_WALLETLOCK, "success");
                return true;
            } else {
                log.error(METHOD_WALLETLOCK, res.getError());
                return false; 
            }
        } catch (Exception e) {
            log.error(METHOD_WALLETLOCK, e);
            return false;
        }
    }
    
    /**
     * CLO인 경우나 TOKEN없이 ETH 싱글모드로만 할 때 이용
     * @return
     */
    public boolean openBlocksGetTxsThenSaveSingle() {
        
        TbBlockchainMaster master = commonDao.getBlockchainMaster(this);
        long currentHeight = master.getSyncHeight();
        long lastestHeight = master.getBestHeight();
        
        if (currentHeight<lastestHeight) {

            // 동기화 할 블럭이 있으면          
            int blockcount = 0; 
            log.info("SYNC", currentHeight + "=>" + lastestHeight);
            String masteraddr    = getOwnerAddress();
            Set<String> alladdrs = getAllAddressSetFromNode();

            EntityManager em = emf.createEntityManager();
            EntityTransaction etx = em.getTransaction();
            etx.begin();
            
            try {
              
                for (long i=currentHeight; i<=lastestHeight; i++) {
                  
                    // 블럭 다 열어봐
                    String[] params2 = new String[2];
                    params2[0] = "\"" + ConvertUtil.longToHex(i) + "\"";
                    params2[1] = "true";
                    EthBlock res2 = null;
                    try {
                        String resStr = JsonRpcUtil.sendJsonRpc2(getRpcUrl(),
                        		METHOD_BLOCKBYNUMBER, params2);
                        // 블럭해시 가져오기
                        res2 = gson.fromJson(resStr, EthBlock.class);
                        if (res2.getError()!=null) {
                            log.error(METHOD_BLOCKBYNUMBER, res2.getError());
                            if (etx.isActive()) {
                                etx.rollback();
                            }
                            em.close();
                            return false;
                        }
                    } catch (Exception e) {
                        log.error(METHOD_BLOCKBYNUMBER, e);
                        if (etx.isActive()) {
                            etx.rollback();
                        }
                        em.close();
                        return false;
                    }
                    
                    // 블록 높이 차로 confirmation 수 구함
                    long confirm   = lastestHeight - i;
                    long time      = ConvertUtil.hexToLong(
                            res2.getResult().getTimestamp().toString());
                    String blockid = "" + i;
                    
                    for (EthTransaction tx : res2.getResult().getTransactions()) {
                        // 반환된 모든 트랜잭션 반복해서 SEND/RECEIVE인지 판단 한 뒤 각 테이블에 CONFIRMATION 수 업데이트
                        // 송신주소나 수신주소가 ETH거나 ERC20이 아니면
                        if (!(alladdrs.contains(tx.getFrom()) || alladdrs.contains(tx.getTo()))) {
                            // 우리 주소가 아니면
                            continue;
                        }
                        boolean failed = false;
                        EthTxReceipt res3 = getTransaction("1", tx.getHash());
                        if (res3.getError()!=null || ERR_ETH_TX_FAIL.equals(res3.getResult().getStatus())) {
                            // 실패한 트랜잭션
                            failed = true;
                        } else if (res3.getResult()==null) { 
                            continue;
                        }
                        
                        double amount       = ConvertUtil.hexToEth(tx.getValue());
                        boolean issend      = alladdrs.contains(tx.getFrom());
                        boolean isreceive   = alladdrs.contains(tx.getTo());
                        boolean isgenerated = false;
                        boolean istomaster  = tx.getTo()!=null && tx.getTo().equals(masteraddr);
                        String toAddress    = tx.getTo();
                        
                        if (issend) {
                            // 출금) 마스터 어카운트에서 어드레스로
                            double actualFee = ConvertUtil.hexToWei(res3.getResult().getGasUsed())
                                    / 1 * ConvertUtil.hexToWei(tx.getGasPrice());
                            TbTrans datum = transactions.findFirstByTxidAndToAddrAndRegDtGreaterThanOrderByRegDtDesc
                                      (tx.getHash(), toAddress, ConvertUtil.getLimitDate());
                            if (datum==null) {
//                                TbSendRequest reqdatum = sendReqRepo.findFirstBySymbolAndToAddrAndRegDtGreaterThanOrderByRegDtDesc
//                                    (getSymbol(), toaddr, getLimitDate());
//                                if (reqdatum!=null) {
//                                    datum = new TbSend(reqdatum.getOrderId(), reqdatum.getSymbol(),
//                                        reqdatum.getUid(), reqdatum.getToAddr(), null,
//                                        reqdatum.getAmount(), reqdatum.getBrokerId());
//                                } else {
//                                    // important) 출금/출금요청 테이블에 존재하지 않는 출금 건: 출금건의 경우 오히려 브로커 매핑이 간단
//                                    datum = new TbSend(WalletUtil.getRandomSystemOrderId(), getSymbol(),
//                                        UID_SYSTEM, toaddr, null, amount, BROKER_ID_SYSTEM);
//                                    datum.setNotifiable('N');
//                                }
                            }
                            datum.setFromAddr(tx.getFrom());
                            datum.setConfirm(confirm);
                            datum.setBlockId(blockid);
                            datum.setFee(actualFee);
                            datum.setTxTime(time);
                            datum.setTxid(tx.getHash());
                            if (failed) {
                                if (res3.getError()!=null && res3.getError().getMessage()!=null) {
                                    datum.setError("[" + res3.getError().getCode() + "] "
                                            + res3.getError().getMessage());
                                } else {
                                    datum.setError(MSG_BLOCK_TX_FAIL);
                                }
                            }
                            em.merge(datum);
                        }
    
                        if (isreceive || isgenerated) {
                            // 입금건) 입금자 유저  어드레스인 거래건: 
                            TbTrans transaction = transactions.
                                    findFirstByTxidAndToAddrAndRegDtGreaterThanOrderByRegDtDesc
                                            (tx.getHash(), tx.getTo(), ConvertUtil.getLimitDate());
                            if (transaction==null) {
                                // find broker_id, uid, From address
                            	Optional<TbUserAddress> rawuser = userAddresses
                                        .findFirstByAddress(toAddress);
                            	if (!rawuser.isPresent()) {
                            	    continue;
                                }
                                TbUserAddress user = rawuser.get();
                                transaction = new TbTrans(user.getUid(),
                                        toAddress, amount);
                            }
                            transaction.setFromAddr(tx.getFrom());
                            transaction.setTxid(tx.getHash());
                            transaction.setConfirm(confirm);
                            transaction.setBlockId(blockid);
                            transaction.setTxTime(time);
                            
                            if (istomaster || isgenerated) {
                                // 마스터주소로 입금건은 알리지 않음
                                transaction.setNotifiable('N');
                            }
                            if (failed) {
                            	if (res3.getError()!=null
                                        && res3.getError().getMessage()!=null) {
                                    transaction.setError("[" + res3.getError().getCode()
                                            + "] " + res3.getError().getMessage());
                                } else {
                                    transaction.setError(MSG_BLOCK_TX_FAIL);
                                }
                            }
                            em.merge(transaction);
                        }
                    }
                    // TXs end
                    blockcount++;
                    if (blockcount>99) {
                        log.info("SYNC", blockid);
                        master.setSyncHeight(i);
                        em.merge(master);
                        etx.commit();
                        etx.begin();
                        blockcount = 0;
                    }
                }
                master.setSyncHeight(lastestHeight);
                em.merge(master);
                if (etx.isActive()) {
                    etx.commit();
                }
                em.close();
            } catch (Exception e) {
                if (etx.isActive()) {
                    etx.rollback();
                }
                em.close();
            }
            return true;
        } else {
            log.debug("SYNC", currentHeight + "=" + lastestHeight + " skip");
            return true;
        }
    }
    
}
