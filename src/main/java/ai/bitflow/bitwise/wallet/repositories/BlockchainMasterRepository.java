package ai.bitflow.bitwise.wallet.repositories;

import ai.bitflow.bitwise.wallet.domains.TbBlockchainMaster;
import ai.bitflow.bitwise.wallet.domains.primarykeys.PkSymbolTestnet;
import org.springframework.data.repository.CrudRepository;


public interface BlockchainMasterRepository extends
		CrudRepository <TbBlockchainMaster, PkSymbolTestnet> {
}