package lt.viko.eif.kskrebe.candlebusiness.dto;

/**
 * Vieno produkto kiekio laukas kliento uzsakymo formai.
 */
public class ClientOrderItemForm {

    /** Produkto identifikatorius. */
    private Long productId;

    /** Kliento pasirinktas kiekis. */
    private Integer quantity = 0;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
