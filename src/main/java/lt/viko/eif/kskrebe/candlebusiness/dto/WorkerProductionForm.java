package lt.viko.eif.kskrebe.candlebusiness.dto;

/**
 * Darbuotojo gamybos patvirtinimo forma Thymeleaf puslapiui.
 */
public class WorkerProductionForm {

    /** Uzsakymo eilutes identifikatorius. */
    private Long orderItemId;

    /** Darbuotojo identifikatorius. */
    private Long employeeId;

    /** Kliento identifikatorius. */
    private Long producedForClientId;

    /** Papildoma pastaba. */
    private String note;

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
