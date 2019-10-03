package ai.bitflow.bitwise.wallet.services.interfaces;

import ai.bitflow.bitwise.wallet.gsonObjects.apiResponse.SendToAddressResponse;

import java.util.Map;

public interface SendManyWallet {
	SendToAddressResponse sendMany(String uid, String pp, Map to) throws Exception;
}