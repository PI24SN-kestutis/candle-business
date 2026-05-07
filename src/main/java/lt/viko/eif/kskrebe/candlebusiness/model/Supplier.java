package lt.viko.eif.kskrebe.candlebusiness.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Tiekejo modelis ingredientams ir kitoms zaliavoms.
 */
@Entity
public class Supplier {

    /** Unikalus tiekejo identifikatorius. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Tiekejo pavadinimas. */
    @Column(nullable = false, unique = true, length = 150)
    private String name;

    /** Tiekejo adresas. */
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    /** Tiekejo sutartis. */
    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

    /** Tustias konstruktorius, reikalingas JPA. */
    public Supplier() {
    }

    /**
     * Konstruktorius tiekejui sukurti.
     *
     * @param name tiekejo pavadinimas
     */
    public Supplier(String name) {
        this.name = name;
    }

    /**
     * @return tiekejo identifikatorius
     */
    public Long getId() {
        return id;
    }

    /**
     * @return tiekejo pavadinimas
     */
    public String getName() {
        return name;
    }

    /**
     * Nustato tiekejo pavadinima.
     *
     * @param name tiekejo pavadinimas
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return tiekejo adresas
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Nustato tiekejo adresa.
     *
     * @param address adresas
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * @return tiekejo sutartis
     */
    public Contract getContract() {
        return contract;
    }

    /**
     * Nustato tiekejo sutarti.
     *
     * @param contract sutartis
     */
    public void setContract(Contract contract) {
        this.contract = contract;
    }
}
