package ai.bitflow.bitwise.wallet;

import ai.bitflow.bitwise.wallet.constants.abstracts.BlockchainConstant;
import ai.bitflow.bitwise.wallet.interceptors.LogExecutionTime;
import ai.bitflow.bitwise.wallet.services.abstracts.BlockchainCommonService;
import ai.bitflow.bitwise.wallet.services.interfaces.OwnChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true", matchIfMissing = true)
public class BlockchainSyncScheduler implements BlockchainConstant {

    private final String TAG 	= "[SCHED]";
	private final int INTERVAL 	= 5000;         // 5 seconds
	private final int SEC_5_CNT = 4;            // 5000 * 4
	private final int MIN_1    	= 12;           // 5000 * 12
	private final int HOUR_3   	= 12 * 60 * 3;  // 5000 * 12 * 60 * 3
	private int count;
	
	@Autowired
    private BlockchainCommonService service;

	/**
	 * 동기화 배치 스케줄러
	 */
	@LogExecutionTime
    @Scheduled(fixedDelay=INTERVAL)
    public void syncBlockTransactions() {
      
        boolean success = false;

        // 1) 15초마다 => 블록체인 기본 정보 업데이트
        if (count%SEC_5_CNT==0) {
            try {
                success = service.syncBlockchainMaster();
                if (!success) {
                    log.error(TAG + "[syncBlockchainMaster] unsuccessful");
                }
            } catch (Exception e) {
              log.error(TAG + "[syncBlockchainMaster] " + e.getMessage());
                e.printStackTrace();
            }
        }

        success = false;
        
        // 2) 15초 마다 => 블록체인 트랜잭션 검색
        // 블록을 열거나 내 트랜잭션 조회 API를 통해 거래소 주소 관련 TX들을 찾아내서 처리
        if (count%SEC_5_CNT==1) {
            if (service instanceof OwnChain) {
            	try {
                    success = ((OwnChain)service).openBlocksGetTxsThenSave();
                    if (!success) {
                        log.error(TAG + "[openBlocksGetTxsThenSave] unsuccessful");
                    }
                } catch (Exception e) {
                    log.error(TAG + "[openBlocksGetTxsThenSave] " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        success = false;

        // 3) 15초 마다 => 입금/출금 컨펌 수 업데이트
        if (count%SEC_5_CNT==2) {
            try {
                success = service.updateTxConfirmCount();
                if (!success) {
                log.error(TAG + "[updateTxConfirmCount] unsuccessful");
                }
            } catch (Exception e) {
                log.error(TAG + "[updateTxConfirmCount] " + e.getMessage());
                e.printStackTrace();
            }
        }

        success = false;

        // 4) 15초 마다 => 실패한 트랜잭션 알림
//        if (count%SEC_5_CNT==3) {
//            try {
//                success = service.notifyTXsFailedNotNotified();
//                if (!success) {
//                    log.error(TAG + "[notifyfailed] failed");
//                }
//            } catch (Exception e) { e.printStackTrace(); }
//        }

        count++;
        if (count>=100) { count = 0; }
    }
    
}

