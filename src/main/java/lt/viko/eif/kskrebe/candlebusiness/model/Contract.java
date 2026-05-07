package lt.viko.eif.kskrebe.candlebusiness.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Sutarties modelis tiekejams.
 */
@Entity
public class Contract {

    /** Unikalus sutarties identifikatorius. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Unikalus sutarties numeris. */
    @Column(nullable = false, unique = true, length = 50)
    private String contractNumber;

    /** Sutarties tekstas. */
    @Column(nullable = false, length = 2000)
    private String text;

    public Contract() {
    }

    public Contract(String contractNumber, String text) {
        this.contractNumber = contractNumber;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
