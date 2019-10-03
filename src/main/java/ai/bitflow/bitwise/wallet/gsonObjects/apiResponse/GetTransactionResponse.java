package ai.bitflow.bitwise.wallet.gsonObjects.apiResponse;

import ai.bitflow.bitwise.wallet.gsonObjects.bitcoin.GetTransaction;
import ai.bitflow.bitwise.wallet.gsonObjects.abstracts.ApiCommonResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GetTransactionResponse extends ApiCommonResponse {

	private Result result;

	public GetTransactionResponse() {
		this.result = new Result();
    }

    public GetTransactionResponse(GetTransaction tx) {
		this.result = new Result();
		this.result.txid = tx.getResult().getTxid();
		this.result.confirmations = tx.getResult().getConfirmations();
		this.result.amount = Math.abs(tx.getResult().getAmount());
		this.result.fee = Math.abs(tx.getResult().getFee());
		this.result.blockhash = tx.getResult().getBlockhash();
		this.result.time = tx.getResult().getTime();
		this.result.txid = tx.getResult().getTxid();
		this.result.status = tx.getResult().getStatus();
	}

	@Data
    public class Result {
		private String txid;
		private int confirmations;
		private double amount;
		private double fee;
		private String blockhash;
		private long time;
		private boolean abandoned;
		private char status;
	}

}
