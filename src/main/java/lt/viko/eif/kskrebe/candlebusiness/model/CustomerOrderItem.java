package lt.viko.eif.kskrebe.candlebusiness.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;

/**
 * Atskira kliento uzsakymo eilute su produktu ir kiekiu.
 */
@Entity
public class CustomerOrderItem {

    /** Unikalus uzsakymo eilutes identifikatorius. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Uzsakymas, kuriam priklauso eilute. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private CustomerOrder order;

    /** Uzsakytas produktas. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /** Uzsakytas kiekis. */
    @Column(nullable = false)
    private Integer quantity;

    /** Vieneto kaina uzsakymo metu. */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    /** Tustias konstruktorius, reikalingas JPA. */
    public CustomerOrderItem() {
    }

    /**
     * Konstruktorius uzsakymo eilutei sukurti.
     *
     * @param order uzsakymas
     * @param product produktas
     * @param quantity kiekis
     * @param unitPrice vieneto kaina
     */
    public CustomerOrderItem(CustomerOrder order, Product product, Integer quantity, BigDecimal unitPrice) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    /**
     * @return uzsakymo eilutes identifikatorius
     */
    public Long getId() {
        return id;
    }

    /**
     * @return uzsakymas
     */
    public CustomerOrder getOrder() {
        return order;
    }

    /**
     * Nustato uzsakyma.
     *
     * @param order uzsakymas
     */
    public void setOrder(CustomerOrder order) {
        this.order = order;
    }

    /**
     * @return produktas
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Nustato produkta.
     *
     * @param product produktas
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * @return uzsakytas kiekis
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Nustato uzsakyta kieki.
     *
     * @param quantity kiekis
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return vieneto kaina
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * Nustato vieneto kaina.
     *
     * @param unitPrice vieneto kaina
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
