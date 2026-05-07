package lt.viko.eif.kskrebe.candlebusiness.dto;

import java.math.BigDecimal;

/**
 * Uzsakymo eilutes duomenys atsakymui.
 */
public class OrderItemResponse {

    /** Uzsakymo eilutes identifikatorius. */
    private Long id;

    /** Produkto identifikatorius. */
    private Long productId;

    /** Produkto pavadinimas. */
    private String productName;

    /** Uzsakytas kiekis. */
    private Integer quantity;

    /** Vieneto kaina. */
    private BigDecimal unitPrice;

    /** Eilutes suma. */
    private BigDecimal lineTotal;

    /** Tustias konstruktorius. */
    public OrderItemResponse() {
    }

    /**
     * @return eilutes identifikatorius
     */
    public Long getId() {
        return id;
    }

    /**
     * Nustato eilutes identifikatoriu.
     *
     * @param id eilutes identifikatorius
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return produkto identifikatorius
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * Nustato produkto identifikatoriu.
     *
     * @param productId produkto identifikatorius
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * @return produkto pavadinimas
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Nustato produkto pavadinima.
     *
     * @param productName produkto pavadinimas
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return kiekis
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Nustato kieki.
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

    /**
     * @return eilutes suma
     */
    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    /**
     * Nustato eilutes suma.
     *
     * @param lineTotal eilutes suma
     */
    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }
}
