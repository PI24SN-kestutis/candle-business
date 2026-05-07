package lt.viko.eif.kskrebe.candlebusiness.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

/**
 * Darbuotojo uzfiksuotas gamybos ivykdymo irasas.
 */
@Entity
public class ProductionTask {

    /** Unikalus gamybos iraso identifikatorius. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Uzsakymo eilute, kuri buvo pagaminta. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_item_id", nullable = false)
    private CustomerOrderItem orderItem;

    /** Darbuotojas, kuris pagamino produkta. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    /** Klientas, kuriam gaminys buvo pagamintas. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produced_for_client_id", nullable = false)
    private User producedForClient;

    /** Pagaminimo patvirtinimo data ir laikas. */
    @Column(nullable = false)
    private LocalDateTime producedAt;

    /** Papildoma darbuotojo pastaba. */
    @Column(length = 500)
    private String note;

    /** Tustias konstruktorius, reikalingas JPA. */
    public ProductionTask() {
    }

    /**
     * Konstruktorius gamybos irasui sukurti.
     *
     * @param orderItem uzsakymo eilute
     * @param employee darbuotojas
     * @param producedForClient klientas
     * @param producedAt pagaminimo laikas
     * @param note pastaba
     */
    public ProductionTask(CustomerOrderItem orderItem, User employee, User producedForClient, LocalDateTime producedAt, String note) {
        this.orderItem = orderItem;
        this.employee = employee;
        this.producedForClient = producedForClient;
        this.producedAt = producedAt;
        this.note = note;
    }

    /**
     * @return gamybos iraso identifikatorius
     */
    public Long getId() {
        return id;
    }

    /**
     * @return uzsakymo eilute
     */
    public CustomerOrderItem getOrderItem() {
        return orderItem;
    }

    /**
     * Nustato uzsakymo eilute.
     *
     * @param orderItem uzsakymo eilute
     */
    public void setOrderItem(CustomerOrderItem orderItem) {
        this.orderItem = orderItem;
    }

    /**
     * @return darbuotojas
     */
    public User getEmployee() {
        return employee;
    }

    /**
     * Nustato darbuotoja.
     *
     * @param employee darbuotojas
     */
    public void setEmployee(User employee) {
        this.employee = employee;
    }

    /**
     * @return klientas, kuriam pagaminta
     */
    public User getProducedForClient() {
        return producedForClient;
    }

    /**
     * Nustato klienta, kuriam pagaminta.
     *
     * @param producedForClient klientas
     */
    public void setProducedForClient(User producedForClient) {
        this.producedForClient = producedForClient;
    }

    /**
     * @return pagaminimo laikas
     */
    public LocalDateTime getProducedAt() {
        return producedAt;
    }

    /**
     * Nustato pagaminimo laika.
     *
     * @param producedAt pagaminimo laikas
     */
    public void setProducedAt(LocalDateTime producedAt) {
        this.producedAt = producedAt;
    }

    /**
     * @return papildoma pastaba
     */
    public String getNote() {
        return note;
    }

    /**
     * Nustato papildoma pastaba.
     *
     * @param note pastaba
     */
    public void setNote(String note) {
        this.note = note;
    }
}
