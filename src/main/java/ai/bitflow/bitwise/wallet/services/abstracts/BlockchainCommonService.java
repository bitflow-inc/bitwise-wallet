package ai.bitflow.bitwise.wallet.services.abstracts;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import ai.bitflow.bitwise.wallet.constants.abstracts.BlockchainConstant;
import ai.bitflow.bitwise.wallet.daos.BlockchainDao;
import ai.bitflow.bitwise.wallet.domains.TbBlockchainMaster;
import ai.bitflow.bitwise.wallet.domains.TbError;
import ai.bitflow.bitwise.wallet.domains.TbTrans;
import ai.bitflow.bitwise.wallet.gsonObjects.NewAddressResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.apiParameters.PersonalRequest;
import ai.bitflow.bitwise.wallet.gsonObjects.apiParameters.SendToAddressRequest;
import ai.bitflow.bitwise.wallet.gsonObjects.apiResponse.DashboardResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.apiResponse.GetBalanceResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.apiResponse.GetErrorsResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.apiResponse.SendFromResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.apiResponse.ValidateAddressResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.common.SystemInfo;
import ai.bitflow.bitwise.wallet.repositories.BlockchainMasterRepository;
import ai.bitflow.bitwise.wallet.repositories.ErrorRepository;
import ai.bitflow.bitwise.wallet.repositories.TransactionRepository;
import ai.bitflow.bitwise.wallet.repositories.UserAddressRepository;
import ai.bitflow.bitwise.wallet.services.interfaces.LockableAddress;
import ai.bitflow.bitwise.wallet.services.interfaces.LockableWallet;
import ai.bitflow.bitwise.wallet.services.interfaces.OwnChain;
import ai.bitflow.bitwise.wallet.utils.ConvertUtil;
import ai.bitflow.bitwise.wallet.utils.Logger;

/**
 * 
 * @author sungjoon.kim
 */
@Service
public abstract class BlockchainCommonService implements BlockchainConstant {

    // wallet properties
    public abstract boolean isOwnerAddressExists();
    public abstract Object getTransaction(String uid, String txid);
    public abstract GetBalanceResponse getBalance(String address) throws Exception;
    
    // open API functions for exchange
    public abstract NewAddressResponse newAddress(PersonalRequest req);
    public abstract ValidateAddressResponse validateAddress(PersonalRequest param);

    // sync tx
    public abstract boolean openBlocksGetTxsThenSave();
    public abstract void beforeBatchSend();
    public abstract boolean sendOneTransaction(TbTrans datum);
    public abstract DashboardResponse getDashboardData();
    public abstract boolean updateTxConfirmCount();
    public abstract long getBestBlockCount() throws Exception;
    
    // 입출금 테이블
    @Autowired
    protected UserAddressRepository userAddresses;
    @Autowired
    protected TransactionRepository transactions;
    @Autowired
    protected BlockchainMasterRepository blockchainMaster;
    @Autowired
    protected ErrorRepository error;
    @Autowired
    protected EntityManagerFactory emf;
    @Autowired
    protected BlockchainDao blockchainDao;
    @Autowired
    protected Logger log;
    protected Gson gson = new Gson();


    public SystemInfo getSystemInfo() {

        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        String osname = os.getName();
        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
        double cpuavr = os.getSystemLoadAverage();
//        int proc = os.getAvailableProcessors();
        double memmax = mem.getNonHeapMemoryUsage().getMax()
                + mem.getHeapMemoryUsage().getMax();
        double memuse = mem.getNonHeapMemoryUsage().getUsed()
                + mem.getHeapMemoryUsage().getUsed();
        // 논힙(윈도우즈) => -1
//        log.debug("usage", "" + mem.getNonHeapMemoryUsage().getMax());
//        log.debug("usage", "" + mem.getHeapMemoryUsage().getMax());
        File file = new File("/");
        double diskmax  = file.getTotalSpace();
        double diskfree = file.getFreeSpace();
        SystemInfo ret = new SystemInfo(osname, cpuavr, memuse/memmax,
                (diskmax - diskfree)/diskmax, (long)memmax, (long)memuse,
                (long)diskmax, (long)(diskmax - diskfree));
        return ret;
    }

    public GetErrorsResponse getErrors() {
        PageRequest pr = PageRequest.of(0, 10, Sort.Direction.DESC,
                "regDt");
        List<TbError> result = error.findAll(pr).getContent();
        GetErrorsResponse ret = new GetErrorsResponse();
        ret.setResult(result);
        return ret;
    }

    @Transactional
    public SendFromResponse requestSendTransaction(SendToAddressRequest req) {
      
        if (this instanceof BitcoinService && req.getFromAccount()==null) {
            // 비트계열 출금어카운트를 유저어카운트로 변경
            req.setFromAccount("");
        } else if (req.getFromAddress()==null) {
            req.setFromAddress(blockchainDao.getOwnerAddress());
        }
        if (SYMBOL_BCD.equals(blockchainDao.getSymbol())) {
            // BCD는 소수점 5자리 이하 송금 시 에러 발생하여 조치
            req.setAmount((double)Math.floor(req.getAmount()*10000d)/10000d);
        }
        SendFromResponse ret = new SendFromResponse();
        ret.setResult(req);
        
        try {
            TbTrans datum = new TbTrans(req);
            transactions.save(datum);
        } catch (Exception e) {
            e.printStackTrace();
            ret.setCode(CODE_FAIL_LOGICAL);
            ret.setError(e.getMessage());
        }
        return ret;
    }
    
    /**
     * 2) 출금 진행중인 건 중에 완료처리해야 할 것 있는지 조회
     * @return
     */
    public List<TbTrans> getSendTXToUpdate() {
        return getSendTXToUpdate(blockchainDao.getSymbol());
    }
    
    /**
     * 최종 confirm 완료되지 않은 트랜잭션 목록
     * @param symbol
     * @return
     */
    public List<TbTrans> getSendTXToUpdate(String symbol) {
        return transactions.findByTxidIsNotNullAndErrorIsNullAndNotiCntLessThanAndRegDtGreaterThan
                    (NOTI_CNT_FINISHED, ConvertUtil.getLimitDate());
    }
    
    /**
     * 입출금 실패 건 중 알리지 않은 건이 있는지 조회
     * @return
     */
    @Transactional
    public boolean notifyTXsFailedNotNotified() {
      
        boolean success1 = true, success2 = true;
        // 1) 출금 실패 건 알림
        List<TbTrans> data1 = transactions.findByErrorIsNotNullAndNotiCntLessThanAndRegDtGreaterThan
                    (NOTI_CNT_FINISHED, ConvertUtil.getLimitDate());
        if (data1!=null && data1.size()>0) {
            try {
                for (TbTrans datum : data1) {
                    if (datum.getNotifiable()=='N') {
                        // 출금 실패 건 중: 브로커ID가 없거나 알리지 않아야할 수신건, 시스템에서 처리하는 출금건인 경우
                        datum.setNotifiable('N');
                    } else {
                        // 출금 실패 건: 일반
//                        TransactionResponse item = WalletUtil.convertTbSendToResponse(datum);
//                        sender.send(TopicId.walletTransmit(datum.getBrokerId()), item);                           
                    }
                    datum.setNotiCnt(NOTI_CNT_FINISHED);
                }
                transactions.saveAll(data1);
            } catch (Exception e) {
                success1 = false;
                e.printStackTrace();
            }
        }
        
        return (success1 && success2);
    }
    
    /**
     * insert or update
     * @return
     */
    @Transactional
    public boolean syncBlockchainMaster() {
    	
        TbBlockchainMaster master = blockchainDao.getBlockchainMaster();
        long startBlock = 0, bestBlock = 0;
        try {
        	master.setDecimals(blockchainDao.getDecimals());
        	if (this instanceof OwnChain) {
	            startBlock = master.getSyncHeight();
	            if (startBlock<((OwnChain)this).getBlockStartFrom()) {
	                startBlock = ((OwnChain)this).getBlockStartFrom();
	            }
	            if (startBlock<1) {
	            	log.error("getBlockStartFrom",
                            "couldn't find blockStartFrom property");
	            	return false;
	        	}
	            // get latest blocknum from chain
	            bestBlock = getBestBlockCount();
	            if (bestBlock>0) {
	                if (startBlock>bestBlock) {
	                    log.error("getBestBlockCount", "curr sync height is bigger than bestchain");
	                } else {
	                	master.setBestHeight(bestBlock);
	                }
	            } else {
	                master.setBestHeight(startBlock);
	            }
        	}
            blockchainDao.saveBlockchainMaster(master);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *
     * @param req
     * @return
     */
    public NewAddressResponse getSavedAddress(PersonalRequest req) {
    	NewAddressResponse ret = new NewAddressResponse();
//    	ret.setResult(req);
//        List<TbUserAddress> result = userAddresses.findByUid(req.getUid());
//        // CASE1) TABLE에 동일한 요청이 있으면 기존 생성값 리턴
//        if (!result.isEmpty()) {
//            if (result.size()==1) {
//                log.debug("getSavedAddress", "address exists " + result);
//                ret.getResult().setAddress(result.get(0).getAddress());
//            } else if (result.size()>1) {
//                log.error("getSavedAddress", "multiple address exists " + result);
//                ret.getResult().setAddress(result.get(0).getAddress());
//            }
//        }
        return ret;
    }
    
    /**
     *
     * query: update TB_SEND set re_notify = 'Y'
     * @return
     */
    @Transactional
    public boolean renotify() {

    	int renotiCnt = 0, successcnt = 0;

    	try {

	        List<TbTrans> data1 = transactions.findByReNotify('Y');
	        if (data1!=null && data1.size()>0) {
	            renotiCnt += data1.size();
	            for (TbTrans datum : data1) {
	                datum.setReNotify('N');
	                blockchainDao.notifyFrontEnd(datum);
	                successcnt++;
	            }
	            transactions.saveAll(data1);
	        }
	        if (renotiCnt>0) {
	            log.info("reNotify", "success/count " + successcnt + "/" + renotiCnt);
	        }
    	} catch (Exception e) { e.printStackTrace(); }
        return (successcnt==renotiCnt);
    }

    /**
     * 1) 출금 처리해야할 건 조회
     * @return
     */
    public List<TbTrans> getToSendList() {
//        return transactions.findByTxidIsNullAndErrMsgIsNullAndRegDtGreaterThan
//                (getLimitDate());
        return null;
    }

    /**
     * 공통 배치 출금
     * @return
     */
    public boolean batchSendTransaction() {

        // 전처리 필요 시
        beforeBatchSend();
        List<TbTrans> data = getToSendList();

        if (data==null || data.size()<1) {
            return true;
        } else {
            int successcount = 0;
            int datasize = data.size();
            boolean success = false;
            // 1) UNLOCK NODE
//            if (this instanceof LockableWallet && this instanceof OwnChain) {
//                success = ((LockableWallet)this).walletpassphrase();
//                if (!success) { return false; }
//            }

            EntityManager em  = emf.createEntityManager();
            EntityTransaction etx = em.getTransaction();

            // Todo: WHAT IF SENDMANY WALLET?
            for (TbTrans datum : data) {
                etx.begin();
                // 2) SEND
                if (this instanceof LockableAddress) {
                    if (datum.getFromAddr()!=null && datum.getFromAddr().length()>0) {
                        success = ((LockableAddress)this)
                                .walletpassphraseWithAddress(datum.getFromAddr());
                        if (!success) {
                            datum.setError(MSG_UNLOCK_FAIL);
                            em.merge(datum);
                            etx.commit();
                            continue;
                        }
                    }
                }
                try {
                    success = sendOneTransaction(datum);
                    if (datum.getError()!=null && datum.getError().length()>0) {
                        // FAIL
                        datum.setError(MSG_SEND_FAIL);
                    }
                    if (success) { successcount++; }
                    em.merge(datum);
                    etx.commit();

                    // 토큰 구매용 출금 요청인 경우
                    etx.begin();

                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
            em.close();
            data.clear();
            data = null;
            if (this instanceof LockableWallet && this instanceof OwnChain) {
                // 4) LOCK NODE
//                success = ((LockableWallet)this).walletlock();
            }
            return (successcount==datasize);
        }
    }

}