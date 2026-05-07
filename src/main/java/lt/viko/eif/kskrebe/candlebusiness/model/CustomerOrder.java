package lt.viko.eif.kskrebe.candlebusiness.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Kliento uzsakymas produktams pirkti arba gaminti.
 */
@Entity
public class CustomerOrder {

    /** Unikalus uzsakymo identifikatorius. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Klientas, pateikes uzsakyma. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    /** Uzsakymo tipas. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderType orderType;

    /** Dabartine uzsakymo busena. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    /** Uzsakymo sukurimo data ir laikas. */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /** Bendra uzsakymo suma. */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /** Uzsakymo eilutes. */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerOrderItem> items = new ArrayList<>();

    /** Tustias konstruktorius, reikalingas JPA. */
    public CustomerOrder() {
    }

    /**
     * Konstruktorius uzsakymui sukurti.
     *
     * @param client klientas
     * @param orderType uzsakymo tipas
     * @param status uzsakymo busena
     * @param createdAt sukurimo data
     * @param totalAmount bendra suma
     */
    public CustomerOrder(User client, OrderType orderType, OrderStatus status, LocalDateTime createdAt, BigDecimal totalAmount) {
        this.client = client;
        this.orderType = orderType;
        this.status = status;
        this.createdAt = createdAt;
        this.totalAmount = totalAmount;
    }

    /**
     * @return uzsakymo identifikatorius
     */
    public Long getId() {
        return id;
    }

    /**
     * @return klientas
     */
    public User getClient() {
        return client;
    }

    /**
     * Nustato klienta.
     *
     * @param client klientas
     */
    public void setClient(User client) {
        this.client = client;
    }

    /**
     * @return uzsakymo tipas
     */
    public OrderType getOrderType() {
        return orderType;
    }

    /**
     * Nustato uzsakymo tipa.
     *
     * @param orderType uzsakymo tipas
     */
    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    /**
     * @return uzsakymo busena
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Nustato uzsakymo busena.
     *
     * @param status uzsakymo busena
     */
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    /**
     * @return uzsakymo sukurimo laikas
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Nustato uzsakymo sukurimo laika.
     *
     * @param createdAt sukurimo laikas
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return bendra uzsakymo suma
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * Nustato bendra uzsakymo suma.
     *
     * @param totalAmount bendra suma
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return uzsakymo eilutes
     */
    public List<CustomerOrderItem> getItems() {
        return items;
    }

    /**
     * Nustato uzsakymo eilutes.
     *
     * @param items uzsakymo eilutes
     */
    public void setItems(List<CustomerOrderItem> items) {
        this.items = items;
    }
}
