package ai.bitflow.bitwise.wallet.repositories;

import ai.bitflow.bitwise.wallet.domains.TbTrans;
import ai.bitflow.bitwise.wallet.domains.primarykeys.PkTxidCategoryToAddr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends PagingAndSortingRepository
        <TbTrans, PkTxidCategoryToAddr> {

    // 1) 블록에서 발견한 관련 출금 건이 출금 테이블에 있는지 조회
    TbTrans findFirstByTxidAndToAddrAndRegDtGreaterThanOrderByRegDtDesc
    (String txid, String toAddr, Date regDt);
    // 2) 최근 전송 Fee 조회
    TbTrans findFirstByFeeGreaterThanOrderByRegDtDesc (double realFee);
    // 4) 출금 confirm 업데이트 대상 조회
    List<TbTrans> findByTxidIsNotNullAndErrorIsNullAndNotiCntLessThanAndRegDtGreaterThan
        (int notiCnt, Date regDt);
    // 5) 출금 실패 알림 대상 조회
    List<TbTrans> findByErrorIsNotNullAndNotiCntLessThanAndRegDtGreaterThan
        (int notiCnt, Date regDt);
    // 6) 수기 처리한 재 알림 대상 트랜잭션들 조회
    List<TbTrans> findByReNotify(char reNotify);
    Page<TbTrans> findAllByUid(String uid, Pageable pr);

}