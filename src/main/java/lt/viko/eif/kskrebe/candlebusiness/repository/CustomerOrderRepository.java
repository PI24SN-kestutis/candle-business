package lt.viko.eif.kskrebe.candlebusiness.repository;

import lt.viko.eif.kskrebe.candlebusiness.model.CustomerOrder;
import lt.viko.eif.kskrebe.candlebusiness.model.OrderStatus;
import lt.viko.eif.kskrebe.candlebusiness.model.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Duomenu prieiga klientu uzsakymams.
 */
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    /**
     * Suranda kliento uzsakymus pagal kliento identifikatoriu.
     *
     * @param clientId kliento identifikatorius
     * @return uzsakymu sarasas
     */
    List<CustomerOrder> findByClientId(Long clientId);

    /**
     * Suranda kliento uzsakymus ir iskart uzkrauna eilutes kliento puslapiui.
     *
     * @param clientId kliento identifikatorius
     * @return uzsakymu sarasas su eilutemis
     */
    @Query("""
            select distinct o
            from CustomerOrder o
            left join fetch o.items i
            left join fetch i.product
            join fetch o.client
            where o.client.id = :clientId
            order by o.createdAt desc
            """)
    List<CustomerOrder> findByClientIdWithItems(Long clientId);

    /**
     * Suranda kliento užsakymus pagal būseną ir užkrauna visus reikalingus ryšius.
     *
     * @param clientId kliento identifikatorius
     * @param status užsakymo būsena
     * @return užsakymų sąrašas
     */
    @Query("""
            select distinct o
            from CustomerOrder o
            left join fetch o.items i
            left join fetch i.product
            join fetch o.client
            where o.client.id = :clientId and o.status = :status
            order by o.createdAt desc
            """)
    List<CustomerOrder> findByClientIdAndStatusWithItems(Long clientId, OrderStatus status);

    /**
     * Suranda vieną užsakymą su klientu, eilutėmis ir produktais.
     *
     * @param orderId užsakymo identifikatorius
     * @return užsakymas
     */
    @Query("""
            select distinct o
            from CustomerOrder o
            left join fetch o.items i
            left join fetch i.product
            join fetch o.client
            where o.id = :orderId
            """)
    Optional<CustomerOrder> findByIdWithItems(Long orderId);

    /**
     * Suranda visus užsakymus su klientu, eilutėmis ir produktais.
     *
     * @return užsakymų sąrašas
     */
    @Query("""
            select distinct o
            from CustomerOrder o
            left join fetch o.items i
            left join fetch i.product
            join fetch o.client
            order by o.createdAt desc
            """)
    List<CustomerOrder> findAllWithItems();

    /**
     * Suranda visus užsakymus pagal būseną ir užkrauna reikalingus ryšius.
     *
     * @param status užsakymo būsena
     * @return užsakymų sąrašas
     */
    @Query("""
            select distinct o
            from CustomerOrder o
            left join fetch o.items i
            left join fetch i.product
            join fetch o.client
            where o.status = :status
            order by o.createdAt desc
            """)
    List<CustomerOrder> findByStatusWithItems(OrderStatus status);

    /**
     * Suranda uzsakymus pagal busena.
     *
     * @param status uzsakymo busena
     * @return uzsakymu sarasas
     */
    List<CustomerOrder> findByStatus(OrderStatus status);

    /**
     * Suranda uzsakymus pagal tipa.
     *
     * @param orderType uzsakymo tipas
     * @return uzsakymu sarasas
     */
    List<CustomerOrder> findByOrderType(OrderType orderType);

    /**
     * Suranda uzsakymus, sukurtus laikotarpyje.
     *
     * @param start periodo pradzia
     * @param end periodo pabaiga
     * @return uzsakymu sarasas
     */
    List<CustomerOrder> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
