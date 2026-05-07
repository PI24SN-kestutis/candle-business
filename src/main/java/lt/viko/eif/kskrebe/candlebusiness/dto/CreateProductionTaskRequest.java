package lt.viko.eif.kskrebe.candlebusiness.dto;

/**
 * Darbuotojo gamybos pazymejimo duomenys.
 */
public class CreateProductionTaskRequest {

    /** Uzsakymo eilutes identifikatorius. */
    private Long orderItemId;

    /** Darbuotojo identifikatorius. */
    private Long employeeId;

    /** Kliento identifikatorius. */
    private Long producedForClientId;

    /** Papildoma darbuotojo pastaba. */
    private String note;

    /** Tustias konstruktorius. */
    public CreateProductionTaskRequest() {
    }

    /**
     * Konstruktorius gamybos pazymejimo uzklausai.
     *
     * @param orderItemId uzsakymo eilutes identifikatorius
     * @param employeeId darbuotojo identifikatorius
     * @param producedForClientId kliento identifikatorius
     * @param note papildoma pastaba
     */
    public CreateProductionTaskRequest(Long orderItemId, Long employeeId, Long producedForClientId, String note) {
        this.orderItemId = orderItemId;
        this.employeeId = employeeId;
        this.producedForClientId = producedForClientId;
        this.note = note;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getProducedForClientId() {
        return producedForClientId;
    }

    public void setProducedForClientId(Long producedForClientId) {
        this.producedForClientId = producedForClientId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
