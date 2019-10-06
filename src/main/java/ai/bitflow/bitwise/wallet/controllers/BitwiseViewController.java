package ai.bitflow.bitwise.wallet.controllers;

import ai.bitflow.bitwise.wallet.gsonObjects.apiResponse.ListWalletResponse;
import ai.bitflow.bitwise.wallet.services.abstracts.BitcoinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


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
        return "index";
    }

    @RequestMapping(value="/dashboard", method=RequestMethod.GET)
    public String dashboard(Model model) {
        return "dashboard";
    }

    /**
     *
     * @param model
     * @return
     */
    @RequestMapping(value="/page/wallet", method=RequestMethod.GET)
    public String wallet(Model model) {
        model.addAttribute("menuIdx", MENU_IDX_WALLET);
        service.getBlockchainData(model);
        ListWalletResponse wallets = service.listwallets();
//        wallets.getResult().getCount(), wallets.getResult().getWalletnames()
        model.addAttribute("wallets", wallets.getResult());
        return "wallet";
    }

    /**
     *
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
     * SWAGGER iframe page
     * @return
     */
    @RequestMapping(value="/page/api", method=RequestMethod.GET)
    public String api(Model model) {
        model.addAttribute("menuIdx", MENU_IDX_API);
        service.getBlockchainData(model);
        return "api";
    }


}
