package ai.bitflow.bitwise.wallet.controllers;

import ai.bitflow.bitwise.wallet.gsonObjects.apiResponse.ListWalletResponse;
import ai.bitflow.bitwise.wallet.services.BlockchainConfig;
import ai.bitflow.bitwise.wallet.services.abstracts.BitcoinService;
import ai.bitflow.bitwise.wallet.utils.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 본 API(SDK)에서 블록체인노드를 관리하거나, 단순 기능
 * (입출금, 잔고조회, 지갑생성, 주소생성)들을 사용하기 위한
 * 웹 대시보드 화면들
 */
@Slf4j
@Controller
public class BitwiseViewController {

    private final int MENU_IDX_DASHBOARD    = 0;
    private final int MENU_IDX_WALLET       = 1;
    private final int MENU_IDX_TRANSACTION  = 2;
    private final int MENU_IDX_API          = 3;

    @Autowired
    private BitcoinService service;

    /**
     * Main dashboard
     */
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String index(Model model) {
        service.getBlockchainData(model);
        service.getBlockchainData(model);
        model.addAttribute("menuIdx", MENU_IDX_DASHBOARD);

        try {
            log.debug("blockchain config " + ConvertUtil.getBlockchainConfig().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "index";
    }

    /**
     * 현재 미사용
     * @param model
     * @return
     */
    @RequestMapping(value="/dashboard", method=RequestMethod.GET)
    public String dashboard(Model model) {
        return "dashboard";
    }

    /**
     * 지갑(월렛) 관리 화면
     * @param model
     * @return
     */
    @RequestMapping(value="/page/wallet", method=RequestMethod.GET)
    public String wallet(Model model) {
        model.addAttribute("menuIdx", MENU_IDX_WALLET);
        service.getBlockchainData(model);
        ListWalletResponse wallets = service.listwallets();
        model.addAttribute("wallets", wallets.getResult());
        return "wallet";
    }

    /**
     * 입출금 트랜잭션 조회 화면
     * (개발 중)
     * @param model
     * @return
     */
    @RequestMapping(value="/page/tx", method=RequestMethod.GET)
    public String tx(Model model) {
        service.getBlockchainData(model);
        model.addAttribute("menuIdx", MENU_IDX_TRANSACTION);
        return "tx";
    }

    /**
     * API 명세를 웹화면을 보여주기 위한 SWAGGER embed page
     * @return
     */
    @RequestMapping(value="/page/api", method=RequestMethod.GET)
    public String api(Model model) {
        model.addAttribute("menuIdx", MENU_IDX_API);
        service.getBlockchainData(model);
        return "api";
    }

}
