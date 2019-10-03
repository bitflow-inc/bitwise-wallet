package ai.bitflow.bitwise.wallet.services.abstracts;

import ai.bitflow.bitwise.wallet.constants.EthereumConstant;
import ai.bitflow.bitwise.wallet.domains.TbTrans;
import ai.bitflow.bitwise.wallet.gsonObjects.NewAddressResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.apiParameters.PersonalRequest;
import ai.bitflow.bitwise.wallet.gsonObjects.apiResponse.GetBalanceResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.apiResponse.ValidateAddressResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.common.BitcoinStringResponse;
import ai.bitflow.bitwise.wallet.utils.ConvertUtil;
import ai.bitflow.bitwise.wallet.utils.JsonRpcUtil;
import ai.bitflow.bitwise.wallet.services.interfaces.LockableAddress;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

/**
 * ETH services
 * 
 * @author sungjoon.kim
 */
public abstract class ERC20AbstractService extends BlockchainCommonService
			implements EthereumConstant, LockableAddress {

	public abstract String getContractaddr();
	private BigInteger gasPrice;
	
	public EthereumService getPlatformService() {
//		return (EthereumService) coinFactory.getService(SYMBOL_ETH);
		return null;
	}
	public Web3j getWeb3j() { return getPlatformService().getWeb3j(); }
	
	@Override public String getRpcUrl() {
		return getPlatformService().getRpcUrl();
	}
	@Override public String getPp() {
		return getPlatformService().getPp();
	}
	@Override public String getOwnerAddress() {
		return getPlatformService().getOwnerAddress();
	}
    @Override public boolean isOwnerAddressExists() {
    	return getPlatformService().isOwnerAddressExists();
    }
	@Override public Object getTransaction(String uid, String txid) {
		return getPlatformService().getTransaction(uid, txid);
	}
	@Transactional
    @Override public void beforeBatchSend() {
		gasPrice = getPlatformService().getGasPrice();
	}
	
	@Transactional
	public boolean syncWalletBalances() {

//        List<String> list = getPlatformService().getAllAddressListFromNode();
//        if (list==null || list.size()<1) { return true; }
//
//        String senderaddr = getOwnerAddress();
//        double totalbal = 0, senderbal = 0;
//        int successcount = 0;
//        int totalcount = list.size();
//
//		for (String addr : list) {
//
//			double actualbal = getBalance(addr);
//            totalbal += actualbal;
//            if (addr.equals(senderaddr)) { senderbal = actualbal; }
//
//			List<TbUserAddress> data = userAddresses.findByAddress(addr);
//			if (data!=null && data.size()>0) {
//            	if (data.size()>1) {
//            		log.error("findBySymbolAndAddr", "this should not be happened");
//            	}
//				TbUserAddress datum = data.get(0);
//				datum.setBalance(actualbal);
//				userAddresses.save(datum);
//			} else if ((data==null || data.size()<1) && actualbal>0) {
//				PkSymbolAddress id = new PkSymbolAddress(getSymbol(), addr);
//			}
//			successcount++;
//		}
//		return (totalcount == successcount);
		return false;
	}

	@Override public boolean sendOneTransaction(TbTrans datum) {
		log.info("SendOneTransaction", "Started");
		try {
			String fromaddr = (datum.getFromAddr()!=null && datum.getFromAddr().length() > 3) 
					? datum.getFromAddr(): getOwnerAddress();
			String method = (datum.getFromAddr()==null||datum.getFromAddr().length()>3)
					?ERC_TRANSFER_CODE:ERC_TRANSFERFROM_CODE;
			String txId = transferWithDataField(method, fromaddr, datum.getToAddr(), 
					datum.getAmount());

			if (txId != null) {
				datum.setTxid(txId);
				if (ERC_TRANSFER_CODE.equals(method)) {
					log.success(ERC_TRANSFER_NAME, "to " + datum.getToAddr() + " amount "
							+ datum.getAmount() + " txid " + txId);
				} else if (ERC_TRANSFERFROM_CODE.equals(method)) {
					log.success(ERC_TRANSFERFROM_NAME, "from " + datum.getFromAddr() + " to " + datum.getToAddr() + " amount "
							+ datum.getAmount() + " txid " + txId);
				}
				return true;
			} else {
				datum.setError("[-1] failed to call transfer contract");
				return false;
			}

		} catch (Exception e) {
			datum.setError("[-1] " + e.getMessage());
			e.printStackTrace();
			log.error(ERC_TRANSFER_NAME, e);
			return false;
		}
	}

	/**
	 * Function constructor arg0: Function name Function constructor arg1:
	 * Parameters to pass as Solidity Types
	 * @param toaddr
	 * @param rawamount
	 * @return
	 * @throws Exception
	 */
	public String transfer(String toaddr, double rawamount) throws Exception {
		return transferWithDataField(ERC_TRANSFER_CODE, getOwnerAddress(), toaddr, rawamount);
	}

	public String transferFrom(String fromaddr, String toaddr, double rawamount) throws Exception {
		return transferWithDataField(ERC_TRANSFERFROM_CODE, fromaddr, toaddr, rawamount);
	}

	private String transferWithDataField(String method, String fromaddr, String toaddr, double rawAmount) throws IOException {
		log.info("transferWithDataField", "Started");
		if (toaddr == null) { return null; }
		try {
			String dataField[] = new String[3];
			
			// contract method: "0xa9059cbb"(transfer) or "0x23b872dd"(transferFrom)
			dataField[0] = method;

			// to address
			dataField[1] = String.format("%64s", toaddr.substring(2)).replace(' ', '0');
			
			// token amount
			String tokenAmtHex = ConvertUtil.tokenAmountToHex(rawAmount, getDecimals());
			dataField[2] = String.format("%64s", tokenAmtHex).replace(' ', '0');

			String data = dataField[0] + dataField[1] + dataField[2];
			log.info("transferWithDataField data field:", data);

			JSONObject params = new JSONObject();
			params.put("from",  	fromaddr);
			params.put("to",    	getContractaddr());
			params.put("gas",   	Numeric.toHexStringWithPrefix(getGasLimit())); // WEI
			params.put("lastGasPrice", 	Numeric.toHexStringWithPrefix(
					ConvertUtil.bigIntegerAddFirst(gasPrice))); // WEI
			params.put("value", 	"0x0"); // Only sending token, not eth
			params.put("data",  	data);
			
			JSONArray paramArr = new JSONArray();
			paramArr.put(params);

			String resStr = JsonRpcUtil.sendJsonRpcJson(getRpcUrl(), METHOD_SENDFROMADDR,
					paramArr);
			BitcoinStringResponse res = gson.fromJson(resStr, BitcoinStringResponse.class);
			String txId = res.getResult();
			log.info("transferWithDataField txid ", txId);
			return txId;
		} catch (Exception e) {
			log.error("transferWithDataField", e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Ex) params {"jsonrpc":"2.0","method":"eth_call", "params":[{"to":
	 * "0xee74110fb5a1007b06282e0de5d73a61bf41d9cd",
	 * "data":"0x70a08231000000000000000000000000c9fbb1691def4d2a54a3eb22e0164359628aa98b"},
	 * "latest"], "id":67}
	 */
	@Override
	public GetBalanceResponse getBalance(String addr) {
		return null;
//		if (addr == null) { return null; }
//		try {
//			Function function = new Function(METHOD_ERC_BALANCEOF,
//					Collections.singletonList(new Address(addr)),
//					Collections.singletonList(new TypeReference<Uint256>() {
//					}));
////			log.debug(getOwnerAddress() + " " + getContractaddr());
//			String resStr = callSmartContract(function, getOwnerAddress(), getContractaddr());
////			log.debug("getBalance " + resStr);
//			List<Type> res = FunctionReturnDecoder.decode(resStr, function.getOutputParameters());
//			return (double) new BigDecimal((BigInteger) res.get(0).getValue())
//					.divide(BigDecimal.TEN.pow(getDecimals()))
//					.doubleValue();
//		} catch (Exception e) {
//			log.error("getBalance", e);
//			e.printStackTrace();
//			return 0;
//		}

	}

	/**
	 * arg0 = caller address
	 * @param function
	 * @param fromAddr
	 * @param contractAddress
	 * @return
	 * @throws Exception
	 */
	private String callSmartContract(Function function, String fromAddr, 
			String contractAddress) throws Exception {
		String encodedFunction = FunctionEncoder.encode(function);
		try {
			EthCall response = getWeb3j()
					.ethCall(Transaction.createEthCallTransaction(fromAddr, contractAddress, 
							encodedFunction), DefaultBlockParameterName.LATEST)
					.sendAsync().get(); // ERROR - java.util.concurrent.ExecutionException: java.net.ConnectException: Failed to connect to
			return response.getValue();
		} catch(Exception e) {
			log.error("callSmartContract", e);
			return null;
		}
	}
	/**
	 */
	@Transactional
    @Override public boolean updateTxConfirmCount() {
		return getPlatformService().updateSendConfirm(getSymbol());
	}

	/**
	 * Transfer.GAS_LIMIT or BigInteger.valueOf(90000);
	 * @return
	 */
	public BigInteger getGasLimit() {
		return BigInteger.valueOf(200000);
	}

	@Override public ValidateAddressResponse validateAddress(PersonalRequest param) {
		return getPlatformService().validateAddress(param);
	}
	
	@Transactional
    @Override public NewAddressResponse newAddress(PersonalRequest req) {
//		NewAddressResponse prev = getSavedAddress(req);
//		if (prev.getResult().getAddress() != null) {
//			return prev;
//		}
		NewAddressResponse ret = getPlatformService().newAddress(req);
//		TbUserAddress datum = new TbUserAddress(
//				ret.getResult().getUid(), ret.getResult().getAddress());
//		userAddresses.save(datum);
		return ret;
	}

	@Override public boolean walletpassphraseWithAddress(String address) {
		return ((LockableAddress) getPlatformService()).walletpassphraseWithAddress(address);
	}

	@Override public boolean walletlock(String address) {
		return ((LockableAddress) getPlatformService()).walletlock(address);
	}

}
