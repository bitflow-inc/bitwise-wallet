package ai.bitflow.bitwise.wallet.repositories;

import ai.bitflow.bitwise.wallet.domains.TbSetting;
import ai.bitflow.bitwise.wallet.domains.primarykeys.PkSymbolTestnet;
import org.springframework.data.repository.CrudRepository;

public interface SettingRepository extends CrudRepository<TbSetting, PkSymbolTestnet> {
}