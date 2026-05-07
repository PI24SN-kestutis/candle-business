package lt.viko.eif.kskrebe.candlebusiness.dto;

import lt.viko.eif.kskrebe.candlebusiness.model.OrderStatus;
import lt.viko.eif.kskrebe.candlebusiness.model.OrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Kliento uzsakymo duomenys atsakymui.
 */
public class OrderResponse {

    /** Uzsakymo identifikatorius. */
    private Long id;

    /** Kliento identifikatorius. */
    private Long clientId;

    /** Kliento naudotojo vardas. */
    private String clientUsername;

    /** Uzsakymo tipas. */
    private OrderType orderType;

    /** Uzsakymo busena. */
    private OrderStatus status;

    /** Sukurimo laikas. */
    private LocalDateTime createdAt;

    /** Bendra uzsakymo suma. */
    private BigDecimal totalAmount;

    /** Uzsakymo eilutes. */
    private List<OrderItemResponse> items = new ArrayList<>();

    /** Tustias konstruktorius. */
    public OrderResponse() {
    }

    /**
     * @return uzsakymo identifikatorius
     */
    public Long getId() {
        return id;
    }

    /**
     * Nustato uzsakymo identifikatoriu.
     *
     * @param id uzsakymo identifikatorius
     */
    public void setId(Long id) {
        this.id = id;
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
     * @return kliento naudotojo vardas
     */
    public String getClientUsername() {
        return clientUsername;
    }

    /**
     * Nustato kliento naudotojo varda.
     *
     * @param clientUsername kliento naudotojo vardas
     */
    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
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
     * @return sukurimo laikas
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Nustato sukurimo laika.
     *
     * @param createdAt sukurimo laikas
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return bendra suma
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * Nustato bendra suma.
     *
     * @param totalAmount bendra suma
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return uzsakymo eilutes
     */
    public List<OrderItemResponse> getItems() {
        return items;
    }

    /**
     * Nustato uzsakymo eilutes.
     *
     * @param items uzsakymo eilutes
     */
    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }
}
