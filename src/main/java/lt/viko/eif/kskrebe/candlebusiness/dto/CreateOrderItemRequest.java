package lt.viko.eif.kskrebe.candlebusiness.dto;

/**
 * Vienos uzsakymo eilutes duomenys, naudojami kuriant uzsakyma.
 */
public class CreateOrderItemRequest {

    /** Uzsakomo produkto identifikatorius. */
    private Long productId;

    /** Uzsakomas kiekis. */
    private Integer quantity;

    /** Tustias konstruktorius. */
    public CreateOrderItemRequest() {
    }

    /**
     * Konstruktorius uzsakymo eilutes uzklausai sukurti.
     *
     * @param productId produkto identifikatorius
     * @param quantity uzsakomas kiekis
     */
    public CreateOrderItemRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
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
     * @return uzsakomas kiekis
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Nustato uzsakoma kieki.
     *
     * @param quantity uzsakomas kiekis
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
