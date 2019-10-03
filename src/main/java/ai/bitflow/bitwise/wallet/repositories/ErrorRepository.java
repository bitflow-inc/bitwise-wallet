package ai.bitflow.bitwise.wallet.repositories;

import ai.bitflow.bitwise.wallet.domains.TbError;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ErrorRepository extends PagingAndSortingRepository
        <TbError, Long> {
}