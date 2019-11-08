package ai.bitflow.bitwise.wallet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ai.bitflow.bitwise.wallet.constants.abstracts.BlockchainConstant;
import ai.bitflow.bitwise.wallet.services.abstracts.BitcoinService;
import ai.bitflow.bitwise.wallet.services.abstracts.BlockchainCommonService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ServiceFactory implements BlockchainConstant {

	@Autowired
	private BitcoinService bitcoinService;
	
	public BlockchainCommonService getService(String symbol) {
		
		symbol = symbol.toUpperCase();
		
		if ( SYMBOL_BTC.equals(symbol)
			|| SYMBOL_LTC.equals(symbol)
			|| SYMBOL_BCH.equals(symbol)
			|| SYMBOL_BSV.equals(symbol)
			|| SYMBOL_BTG.equals(symbol)
			|| SYMBOL_DOGE.equals(symbol)
			|| SYMBOL_BCD.equals(symbol) ) {
			log.debug("service " + bitcoinService.getClass());
			return bitcoinService;
		} else {
			log.debug("service is null");
			return null;
		}
	}
	
}
