package lt.viko.eif.kskrebe.candlebusiness.repository;

import lt.viko.eif.kskrebe.candlebusiness.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Duomenu prieiga adresams.
 */
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * Suranda visus adresus rikiuotus pateikimui.
     *
     * @return adresu sarasas
     */
    List<Address> findAllByOrderByCountryAscStreetAscHouseNumberAsc();
}
