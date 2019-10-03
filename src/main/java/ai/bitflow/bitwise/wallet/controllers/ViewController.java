package ai.bitflow.bitwise.wallet.controllers;

import ai.bitflow.bitwise.wallet.services.abstracts.BitcoinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Slf4j
@Controller
public class ViewController {

    @Autowired
    private BitcoinService service;

    /**
     * Dashboard
     */
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String index(Model model) {
        service.getBlockchainData(model);
        return "index";
    }

    @RequestMapping(value="/page/wallet", method=RequestMethod.GET)
    public String wallet(Model model) {
        service.getBlockchainData(model);
        return "wallet";
    }

    @RequestMapping(value="/page/tx", method=RequestMethod.GET)
    public String tx(Model model) {
        service.getBlockchainData(model);
        return "tx";
    }

    @RequestMapping(value="/page/transfer", method=RequestMethod.GET)
    public String transfer(Model model) {
        service.getBlockchainData(model);
        return "transfer";
    }

    @RequestMapping(value="/page/api", method=RequestMethod.GET)
    public String api(Model model) {
        service.getBlockchainData(model);
        return "api";
    }

    @RequestMapping(value="/wallet", method=RequestMethod.GET)
    public String wallet() {
        return "wallet";
    }

    @RequestMapping(value="/dashboard", method=RequestMethod.GET)
    public String dashboard() {
        return "dashboard";
    }

}
