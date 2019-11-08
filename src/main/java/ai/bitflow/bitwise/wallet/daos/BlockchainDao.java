package ai.bitflow.bitwise.wallet.daos;

import ai.bitflow.bitwise.wallet.constants.abstracts.BlockchainConstant;
import ai.bitflow.bitwise.wallet.domains.TbBlockchainMaster;
import ai.bitflow.bitwise.wallet.domains.TbTrans;
import ai.bitflow.bitwise.wallet.domains.primarykeys.PkSymbolTestnet;
import ai.bitflow.bitwise.wallet.repositories.BlockchainMasterRepository;
import ai.bitflow.bitwise.wallet.repositories.TransactionRepository;
import ai.bitflow.bitwise.wallet.services.abstracts.BitcoinService;
import ai.bitflow.bitwise.wallet.services.abstracts.BlockchainCommonService;
import ai.bitflow.bitwise.wallet.services.interfaces.OwnChain;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * 1) 블록체인의 종류와 관계없이 공통적으로 사용하는 함수들을 추상 메서드로 정의
 * 2) 블록체인의 종류와 관계없어 공통적으로 처리하는(메타 정보 저장 등) 함수들 구현
 */
@Slf4j
@Repository
public abstract class BlockchainDao implements BlockchainConstant {

	@Getter @Value("${app.setting.symbol}") private String symbol;
    @Value("${app.setting.rpcUrl}") private String rpcUrl;
    @Getter @Value("${app.setting.testnet}") private boolean testnet;
    @Getter @Value("${app.setting.rpcId}") private String rpcId;
    @Getter @Value("${app.setting.rpcPw}") private String rpcPw;
    @Getter @Value("${app.setting.decimals}") private int decimals;
    @Getter @Value("${app.setting.minConfirm}") private long minConfirm;

    @Getter @Value("${app.setting.ownerAddress}") private String ownerAddress;

    @Getter @Value("${app.setting.minGasAmt}") private double minGasAmt;      // not using
    @Getter @Value("${app.setting.minAmtGather}") private double minAmtGather; // not using
    @Getter @Value("${app.setting.blockStartFrom}") private long blockStartFrom;

    @Getter @Value("${app.setting.ownerAccount}") private String ownerAccount;
    @Getter @Value("${app.setting.userAccount}") private String userAccount;

    @Autowired
    private BlockchainMasterRepository blockchainMaster;
    @Autowired
    private TransactionRepository transaction;
    
    abstract long getBestBlockCount() throws Exception;
    abstract List<String> getAllAddressListFromNode(String uid);
    abstract Set<String> getAllAddressSetFromNode();

    public String getRpcUrl() {
    	if (multiWalletSupport()) {
    		return rpcUrl + "wallet/";
    	} else {
    		return rpcUrl;
    	}
    }
    
    public boolean multiWalletSupport() {
    	if (symbol.toUpperCase().equals(SYMBOL_BTC) || symbol.toUpperCase().equals(SYMBOL_LTC)) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public void setRpcError() {
    	TbBlockchainMaster item = getBlockchainMaster();
        item.setRpcOk('N');
        blockchainMaster.save(item);
    }
    /**
     * DB에서 마스터 테이블 조회하는 경우
     * @return
     */
    public TbBlockchainMaster getBlockchainMaster() {
        PkSymbolTestnet pk = new PkSymbolTestnet(getSymbol(), isTestnet()?'Y':'N');
        Optional<TbBlockchainMaster> obj = blockchainMaster.findById(pk);
        TbBlockchainMaster master = null;
        if (obj.isPresent()) {
            // 기존 데이터가 있으면
            master = obj.get();
        } else {
            if (this instanceof OwnChain) {
                // 1) Case of Coin
                master = new TbBlockchainMaster(getSymbol(),
                        isTestnet(),
                        getOwnerAddress(),
                        ((OwnChain)this).getBlockStartFrom(),
                        ((OwnChain)this).getBlockStartFrom());
            } else {
                // 2) Case of Token
                master = new TbBlockchainMaster(getSymbol(), isTestnet(), getOwnerAddress());
            }
            blockchainMaster.save(master);
        }
        return master;
    }

    /**
     *
     * @param pageno
     * @param size
     * @return
     */
    public Page<TbTrans> getTransactions(int pageno, int size) {
        PageRequest pr = PageRequest.of(pageno, size, Sort.Direction.DESC,
                "txTime");
        Page<TbTrans> ret = transaction.findAll(pr);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                "M.d HH:mm");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
        List<TbTrans> list = ret.getContent();
        for (TbTrans item : list) {
            if (item.getTxTime()==0) {
                item.setTxTimeStr(sdf.format(
                        Calendar.getInstance().getTimeInMillis()));
            } else {
                item.setTxTimeStr(sdf.format(item.getTxTime() * 1000L));
            }

        }
        return ret;
    }

    public Page<TbTrans> getTransactions(String uid, int pageno, int size) {
        PageRequest pr = PageRequest.of(pageno, size, Sort.Direction.DESC,
                "txTime");
        return transaction.findAllByUid(uid, pr);
    }

    /**
     * 입출금 처리상태 변동 시
     * 1) 거래소의 경우 KAFKA로 알림
     * 2) 모바일 월렛의 경우 FCM 알림
     * @param trans
     * @return
     */
    public boolean notifyFrontEnd(TbTrans trans) {
        if (trans==null) { return false; }
        if (trans.getNotifiable()=='N') {
            // 알리지 말아야할 TX에 대한 방어코드
            return true;
        } else {
            // 1. 거래소의 경우 KAFKA로 알림
//            TransactionResponse item = WalletUtil.convertTbSendToResponse(datum);
//            sender.send(TopicId.walletTransmit(datum.getBrokerId()), item);
            // 2. 모바일 월렛의 경우 FCM 알림
//        	sendFcm(send.getIntent(), ldatum.getStatus(), ldatum.getPayAmount()
//	        					, ldatum.getTokenAmount(), send.getFromAddr(), SYMBOL_CPD);
        }
        return true;
    }

    public void saveBlockchainMaster(TbBlockchainMaster item) {
        blockchainMaster.save(item);
    }

}
