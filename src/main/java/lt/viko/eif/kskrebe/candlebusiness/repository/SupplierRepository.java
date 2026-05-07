package lt.viko.eif.kskrebe.candlebusiness.repository;

import lt.viko.eif.kskrebe.candlebusiness.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Duomenu prieiga tiekejams.
 */
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    /**
     * Suranda visus tiekejus pagal pavadinima.
     *
     * @return tiekeju sarasas
     */
    List<Supplier> findAllByOrderByNameAsc();

    /**
     * Suranda tiekeja pagal pavadinima.
     *
     * @param name tiekejo pavadinimas
     * @return rastas tiekejas
     */
    Optional<Supplier> findByName(String name);

    /**
     * Patikrina, ar bent vienas tiekejas naudoja nurodyta adresa.
     *
     * @param addressId adreso identifikatorius
     * @return ar adresas naudojamas
     */
    boolean existsByAddressId(Long addressId);

    /**
     * Patikrina, ar bent vienas tiekejas naudoja nurodyta sutarti.
     *
     * @param contractId sutarties identifikatorius
     * @return ar sutartis naudojama
     */
    boolean existsByContractId(Long contractId);
}
