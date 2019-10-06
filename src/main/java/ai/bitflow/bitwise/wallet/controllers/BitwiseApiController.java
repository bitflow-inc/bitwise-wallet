package ai.bitflow.bitwise.wallet.controllers;

import ai.bitflow.bitwise.wallet.constants.abstracts.BlockchainConstant;
import ai.bitflow.bitwise.wallet.converters.TbTransListToTransList;
import ai.bitflow.bitwise.wallet.domains.TbTrans;
import ai.bitflow.bitwise.wallet.gsonObjects.NewAddressResponse;
import ai.bitflow.bitwise.wallet.gsonObjects.apiParameters.*;
import ai.bitflow.bitwise.wallet.gsonObjects.apiResponse.*;
import ai.bitflow.bitwise.wallet.gsonObjects.bitcoin.GetTransaction;
import ai.bitflow.bitwise.wallet.gsonObjects.bitcoin.GetWalletInfo;
import ai.bitflow.bitwise.wallet.gsonObjects.common.BitcoinDoubleResponse;
import ai.bitflow.bitwise.wallet.services.abstracts.BitcoinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * 모바일월렛, 거래소, 블록익스플로러 등 블록체인과 연계된 응용프로그램의
 * FRONT-END에 데이터들을 제공하기 위한 API 집합들
 */
@Slf4j
@Controller
@RequestMapping(value="/api/v1")
public class BitwiseApiController implements BlockchainConstant {

    @Autowired
    private BitcoinService service;

    /**
     * Dashboard page API
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/dashboard", method= RequestMethod.POST)
    public DashboardResponse dashboard() {
        return service.getDashboardData();
    }

    // SettingRepository

    /**
     * Dashboard page API
     * @return
     */
//    @ResponseBody
//    @RequestMapping(value="/wallet", method= RequestMethod.POST)
//    public WalletAjaxResponse wallet() {
//        return service.getWalletsInfo();
//    }

    /**
     * Dashboard page API
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/lasttransactions", method= RequestMethod.POST)
    public GetTransactionsResponse lasttransactions() {
        Page<TbTrans> txs = service.getLastTransactions();
        GetTransactionsResponse ret = new GetTransactionsResponse(
                txs.getTotalElements(), TbTransListToTransList.convert(txs.getContent()));
        return ret;
    }

    /**
     * Dashboard page API
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/geterrors", method= RequestMethod.POST)
    public GetErrorsResponse geterrors() {
        GetErrorsResponse ret = service.getErrors();
        return ret;
    }

    /**
     * 1) General API
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/getblockcount", method= RequestMethod.POST)
    public GetBlockCountResponse getblockcount() {
        return service.getBlockCount();
    }

    /**
     * 1) General API
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/estimatefee", method= RequestMethod.POST)
    public BitcoinDoubleResponse estimatefee() {
        return service.estimateFee();
    }

//    @ResponseBody
//    @RequestMapping(value="/encryptwallet", method= RequestMethod.POST)
//    public void encryptwallet() {
//        service.encryptwallet();
//    }

    /**
     * 새 지갑 생성
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/createwallet", method= RequestMethod.POST)
    public CreateWalletResponse createwallet(@RequestBody PersonalRequest param) {
        if (param.getUid()==null || param.getUid().length()<1
                || param.getPassphrase()==null || param.getPassphrase().length()<1) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            return null;
        }
        return service.createWallet(param.getUid(), param.getPassphrase());
    }

    @ResponseBody
    @RequestMapping(value="/getwalletinfo/", method= RequestMethod.POST)
    public GetWalletInfo getwalletinfo() {
        return service.getWalletInfo("");
    }

    @ResponseBody
    @RequestMapping(value="/getwalletinfo/{uid}", method= RequestMethod.POST)
    public GetWalletInfo getwalletinfo(@PathVariable String uid) {
        return service.getWalletInfo(uid);
    }

    @ResponseBody
    @RequestMapping(value="/listwallets", method= RequestMethod.POST)
    public ListWalletResponse listwallets() {
        return service.listwallets();
    }


    /**
     * 새 주소 생성
     */
    @ResponseBody
    @RequestMapping(value="/getnewaddress/", method= RequestMethod.POST)
    public NewAddressResponse getnewaddress(@RequestBody PersonalRequest param) {
        param.setUid("");
        return service.newAddress(param);
    }

    @ResponseBody
    @RequestMapping(value="/getnewaddress/{uid}", method= RequestMethod.POST)
    public NewAddressResponse getnewaddress(@PathVariable String uid,
                            @RequestBody PersonalRequest param) {
        if (uid==null || uid.length()<1) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            return null;
        }
        param.setUid(uid);
        return service.newAddress(param);
    }

    /**
     * 사용자용) 토큰, 암호화폐, 원화, 잔고 조회
     */
    @ResponseBody
    @RequestMapping(value="/getbalance", method= RequestMethod.POST)
    public GetBalanceResponse getbalance(@RequestBody PersonalRequest param) {
        if (param.getUid()==null) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            return null;
        }
        // UID로 조회
        try {
            GetBalanceResponse res = service.getBalance(param.getUid());
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            return null;
        }
    }

    /**
     * 지갑주소 유효성 검증
     */
    @ResponseBody
    @RequestMapping(value="/validateaddress", method= RequestMethod.POST)
    public ValidateAddressResponse validateaddress(
            @RequestBody PersonalRequest param) {
        if (param.getAddress()==null) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            return null;
        } else if (!param.getAddress().equals(param.getAddress().trim())) {
        	// 공백이 있는 경우
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            return null;
    	} else {
    		// 정상
            return service.validateAddress(param);
    	}
    }

    @ResponseBody
    @RequestMapping(value="/gettransaction", method= RequestMethod.POST)
    public GetTransactionResponse gettransaction(
            @RequestBody GetTransactionRequest param) {
        GetTransaction tx = (GetTransaction) service.getTransaction
                (param.getUid(), param.getTxid());
        GetTransactionResponse ret = new GetTransactionResponse(tx);
        return ret;
    }

    @ResponseBody
    @RequestMapping(value="/gettransactions", method= RequestMethod.POST)
    public GetTransactionsResponse gettransactions(
            @RequestBody GetTransactionsRequest param) {
        if (param.getUid()==null || param.getUid().length()<1) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            return null;
        }
        if (param.getPageno()<1) { param.setPageno(1); }
        if (param.getSize()<1) { param.setSize(15); }
        Page<TbTrans> txs = service.getTransactions(param.getUid(),
                param.getPageno(), param.getSize());
        GetTransactionsResponse ret = new GetTransactionsResponse(
                txs.getTotalElements(), TbTransListToTransList.convert(txs.getContent()));
        return ret;
    }

    /**
     * 출금요청
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/sendtoaddress", method= RequestMethod.POST)
    public SendToAddressResponse sendtoaddress(
            @RequestBody SendToAddressRequest param) {
        param.setUid("");
        if (param.getToAddress()==null || param.getAmount()<=0
                || !param.getToAddress().trim().equals(param.getToAddress())
                || param.getPp()==null || param.getPp().length()<1) {
            // 주소 앞뒤 공백 방어 필요
            SendToAddressResponse res = new SendToAddressResponse();
            res.setCode(CODE_FAIL_PARAM);
            return res;
        } else {
            try {
                return service.sendToAddress(param);
            } catch (Exception e) {
                e.printStackTrace();
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                return null;
            }
        }
    }

    /**
     * 출금요청
     * @param uid, toAddress, amount, pp
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/sendtoaddress/{uid}", method= RequestMethod.POST)
    public SendToAddressResponse sendtoaddress(
            @PathVariable String uid, @RequestBody SendToAddressRequest param) {
        param.setUid(uid);
        if (param.getToAddress()==null || param.getAmount()<=0
                || !param.getToAddress().trim().equals(param.getToAddress())) {
            // 주소 앞뒤 공백 방어 필요
            SendToAddressResponse res = new SendToAddressResponse();
            res.setCode(CODE_FAIL_PARAM);
            return res;
        } else {
            try {
                return service.sendToAddress(param);
            } catch (Exception e) {
                e.printStackTrace();
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                return null;
            }
        }
    }

    /**
     * 출금요청
     */
    @ResponseBody
    @RequestMapping(value="/sendmany", method= RequestMethod.POST)
    public SendToAddressResponse sendmany(
            @RequestBody SendManyRequest param) {

        if (param.getUid()==null || param.getUid().length()<1
                || param.getPp()==null || param.getPp().length()<1) {
            // 주소 앞뒤 공백 방어 필요
            SendToAddressResponse res = new SendToAddressResponse();
            res.setCode(CODE_FAIL_PARAM);
            return res;
        } else {
            try {
                return service.sendMany(param.getUid(), param.getPp(), param.getTo());
            } catch (Exception e) {
                e.printStackTrace();
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                return null;
            }
        }
    }
    
    /**
     * 출금요청(내부용)
     */
    @ResponseBody
    @RequestMapping(value="/sendfrom", method= RequestMethod.POST)
    public SendFromResponse sendfrom(@RequestBody SendToAddressRequest param) {
    	if (param.getOrderId()==null || param.getToAddress()==null
            || (param.getFromAddress()==null && param.getFromAccount()==null) || param.getAmount()<=0 
            || param.getBrokerId()==null || param.getBrokerId().length()!=4) {
            SendFromResponse res = new SendFromResponse();
            res.setCode(CODE_FAIL_PARAM);
            return res;
        } else {
            // 앞뒤 공백주소 방어로직
            if (param.getFromAccount()!=null && param.getFromAccount().length()>0) {
                param.setFromAccount(param.getFromAccount().trim());  
            }
            if (param.getFromAddress()!=null && param.getFromAddress().length()>0) {
                param.setFromAddress(param.getFromAddress().trim());  
            }
            if (param.getFromTag()!=null && param.getFromTag().length()>0) {
                param.setFromTag(param.getFromTag().trim());  
            }
            param.setToAddress(param.getToAddress().trim());
            if ("".equals(param.getToTag())) { param.setToTag(null); }
            return service.requestSendTransaction(param);
        }
    }

}
