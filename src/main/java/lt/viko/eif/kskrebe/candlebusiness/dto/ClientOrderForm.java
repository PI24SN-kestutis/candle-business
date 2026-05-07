package lt.viko.eif.kskrebe.candlebusiness.dto;

import lt.viko.eif.kskrebe.candlebusiness.model.OrderType;

import java.util.ArrayList;
import java.util.List;

/**
 * Kliento uzsakymo forma Thymeleaf puslapiui.
 */
public class ClientOrderForm {

    /** Redaguojamo uzsakymo identifikatorius. */
    private Long orderId;

    /** Pasirinkto kliento identifikatorius. */
    private Long clientId;

    /** Uzsakymo tipas. */
    private OrderType orderType = OrderType.PIRKIMAS_IS_SANDELIO;

    /** Produktu eilutes su kiekiais. */
    private List<ClientOrderItemForm> items = new ArrayList<>();

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public List<ClientOrderItemForm> getItems() {
        return items;
    }

    public void setItems(List<ClientOrderItemForm> items) {
        this.items = items;
    }
}
