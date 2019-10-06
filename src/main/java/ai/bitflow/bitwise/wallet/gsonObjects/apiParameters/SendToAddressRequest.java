package ai.bitflow.bitwise.wallet.gsonObjects.apiParameters;

import lombok.Data;

@Data
public class SendToAddressRequest {
  
    private String uid;
    private String toAddress;
    private double amount;

    private String orderId;
    private String fromAccount;
    private String fromAddress;
    private String fromTag;
    private String toTag;
    private char notifiable = 'Y';
    private String brokerId; 
    private String txid;      // 전송 요청결과 TXID
    private boolean finished; // 전송결과 최종 CONFIRM/FAIL 통지여부
    private double customFee; // 사용자 설정 수수료
    private double expectFee; // 거래소 수수료
    private double realFee;   // 실 전송 수수료
    private int dupeCount;    // 중복 수신자 개수
    private String error;     // 에러 메시지
    private String pp;
    
    public SendToAddressRequest() {}

    public SendToAddressRequest(String uid, String toAddress, double amount) {
        this.uid = uid;
        this.toAddress = toAddress;
        this.amount = amount;
    }
    
    public SendToAddressRequest(SendToAddressRequest input) {
        this.uid        = input.uid;
        this.toAddress  = input.toAddress;
        this.amount     = input.amount;
    }
    
}
