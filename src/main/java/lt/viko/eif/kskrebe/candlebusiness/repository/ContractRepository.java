package lt.viko.eif.kskrebe.candlebusiness.repository;

import lt.viko.eif.kskrebe.candlebusiness.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Duomenu prieiga sutartims.
 */
public interface ContractRepository extends JpaRepository<Contract, Long> {

    /**
     * Suranda visas sutartis pagal numeri.
     *
     * @return sutarciu sarasas
     */
    List<Contract> findAllByOrderByContractNumberAsc();

    /**
     * Suranda sutarti pagal numeri.
     *
     * @param contractNumber sutarties numeris
     * @return rasta sutartis
     */
    Optional<Contract> findByContractNumber(String contractNumber);
}
