package lt.viko.eif.kskrebe.candlebusiness.repository;

import lt.viko.eif.kskrebe.candlebusiness.model.CustomerOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Duomenu prieiga klientu uzsakymu eilutems.
 */
public interface CustomerOrderItemRepository extends JpaRepository<CustomerOrderItem, Long> {

    /**
     * Suranda visas uzsakymo eilutes ir iskart uzkrauna darbuotojo puslapiui
     * reikalingus rysius.
     *
     * @return eiluciu sarasas su uzsakymu, klientu ir produktu
     */
    @Query("""
            select item
            from CustomerOrderItem item
            join fetch item.order o
            join fetch o.client
            join fetch item.product
            """)
    List<CustomerOrderItem> findAllWithOrderAndProduct();

    /**
     * Suranda visas uzsakymo eilutes pagal uzsakymo identifikatoriu.
     *
     * @param orderId uzsakymo identifikatorius
     * @return eiluciu sarasas
     */
    List<CustomerOrderItem> findByOrderId(Long orderId);

    /**
     * Suranda visas eilutes pagal produkto identifikatoriu.
     *
     * @param productId produkto identifikatorius
     * @return eiluciu sarasas
     */
    List<CustomerOrderItem> findByProductId(Long productId);
}
