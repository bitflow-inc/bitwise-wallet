package ai.bitflow.bitwise.wallet.repositories;

import ai.bitflow.bitwise.wallet.domains.TbUserAddress;
import ai.bitflow.bitwise.wallet.domains.primarykeys.PkUidAddress;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository extends PagingAndSortingRepository
        <TbUserAddress, PkUidAddress> {
    // findOne where defaultYn = 'Y' and orderBy regDt desc;
    List<TbUserAddress> findByUid(String uid);
    Optional<TbUserAddress> findFirstByAddress(String address);
}