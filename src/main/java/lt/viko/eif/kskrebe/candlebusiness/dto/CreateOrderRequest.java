package lt.viko.eif.kskrebe.candlebusiness.dto;

import lt.viko.eif.kskrebe.candlebusiness.model.OrderType;

import java.util.ArrayList;
import java.util.List;

/**
 * Kliento uzsakymo sukurimo duomenys.
 */
public class CreateOrderRequest {

    /** Kliento identifikatorius. */
    private Long clientId;

    /** Kuriamo uzsakymo tipas. */
    private OrderType orderType;

    /** Uzsakymo eilutes. */
    private List<CreateOrderItemRequest> items = new ArrayList<>();

    /** Tustias konstruktorius. */
    public CreateOrderRequest() {
    }

    /**
     * Konstruktorius uzsakymo uzklausai sukurti.
     *
     * @param clientId kliento identifikatorius
     * @param orderType uzsakymo tipas
     * @param items uzsakymo eilutes
     */
    public CreateOrderRequest(Long clientId, OrderType orderType, List<CreateOrderItemRequest> items) {
        this.clientId = clientId;
        this.orderType = orderType;
        this.items = items;
    }

    /**
     * @return kliento identifikatorius
     */
    public Long getClientId() {
        return clientId;
    }

    /**
     * Nustato kliento identifikatoriu.
     *
     * @param clientId kliento identifikatorius
     */
    public void setClientId(Long clientId) {
        this.clientId = clientId;
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
     * @return uzsakymo eilutes
     */
    public List<CreateOrderItemRequest> getItems() {
        return items;
    }

    /**
     * Nustato uzsakymo eilutes.
     *
     * @param items uzsakymo eilutes
     */
    public void setItems(List<CreateOrderItemRequest> items) {
        this.items = items;
    }
}
