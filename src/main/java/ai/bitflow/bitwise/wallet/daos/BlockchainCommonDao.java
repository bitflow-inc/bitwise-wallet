package ai.bitflow.bitwise.wallet.daos;

import ai.bitflow.bitwise.wallet.domains.TbBlockchainMaster;
import ai.bitflow.bitwise.wallet.domains.TbTrans;
import ai.bitflow.bitwise.wallet.repositories.BlockchainMasterRepository;
import ai.bitflow.bitwise.wallet.repositories.TransactionRepository;
import ai.bitflow.bitwise.wallet.services.abstracts.BlockchainCommonService;
import ai.bitflow.bitwise.wallet.services.interfaces.OwnChain;
import lombok.Getter;
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


@Repository
public class BlockchainCommonDao {

    @Getter @Value("${app.crypto.symbol}")
    private String symbol;
    @Getter @Value("${app.crypto.testnet}")
    private boolean testnet;
    @Autowired
    protected BlockchainMasterRepository blockchainMaster;
    @Autowired
    protected TransactionRepository transaction;

    /**
     * DB에서 마스터 테이블 조회하는 경우
     * @return
     */
    public TbBlockchainMaster getBlockchainMaster(BlockchainCommonService ctx) {
        Optional<TbBlockchainMaster> obj = blockchainMaster.findById(ctx.getSymbol());
        TbBlockchainMaster master = null;
        if (obj.isPresent()) {
            // 기존 데이터가 있으면
            master = obj.get();
        } else {
            if (this instanceof OwnChain) {
                // 메인 체인의 코인이면 (not token)
                master = new TbBlockchainMaster(ctx.getSymbol(), ctx.getOwnerAddress(),
                        ((OwnChain)this).getBlockStartFrom(),
                        ((OwnChain)this).getBlockStartFrom());
            } else {
                // 토큰이면
                master = new TbBlockchainMaster(ctx.getSymbol(), ctx.getOwnerAddress());
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
     * 출금내역)
     * 1) 거래소의 경우  KAFKA 객체로 변환하여 전송
     * 2) 프라이빗 세일의 경우 구매상태 변경
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
