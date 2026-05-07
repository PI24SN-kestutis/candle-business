package lt.viko.eif.kskrebe.candlebusiness.dto;

import java.time.LocalDateTime;

/**
 * Gamybos pazymejimo duomenys atsakymui.
 */
public class ProductionTaskResponse {

    /** Gamybos iraso identifikatorius. */
    private Long id;

    /** Uzsakymo eilutes identifikatorius. */
    private Long orderItemId;

    /** Uzsakymo identifikatorius. */
    private Long orderId;

    /** Produkto identifikatorius. */
    private Long productId;

    /** Produkto pavadinimas. */
    private String productName;

    /** Darbuotojo identifikatorius. */
    private Long employeeId;

    /** Darbuotojo naudotojo vardas. */
    private String employeeUsername;

    /** Kliento identifikatorius. */
    private Long producedForClientId;

    /** Kliento naudotojo vardas. */
    private String producedForClientUsername;

    /** Pagaminimo laikas. */
    private LocalDateTime producedAt;

    /** Papildoma pastaba. */
    private String note;

    public ProductionTaskResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeUsername() {
        return employeeUsername;
    }

    public void setEmployeeUsername(String employeeUsername) {
        this.employeeUsername = employeeUsername;
    }

    public Long getProducedForClientId() {
        return producedForClientId;
    }

    public void setProducedForClientId(Long producedForClientId) {
        this.producedForClientId = producedForClientId;
    }

    public String getProducedForClientUsername() {
        return producedForClientUsername;
    }

    public void setProducedForClientUsername(String producedForClientUsername) {
        this.producedForClientUsername = producedForClientUsername;
    }

    public LocalDateTime getProducedAt() {
        return producedAt;
    }

    public void setProducedAt(LocalDateTime producedAt) {
        this.producedAt = producedAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
