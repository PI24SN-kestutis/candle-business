package lt.viko.eif.kskrebe.candlebusiness.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

/**
 * Bazinis asmens modelis.
 * <p>
 * Iš šios klasės gali paveldėti skirtingų tipų asmenys,
 * pvz., sistemos naudotojai.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {

    /** Unikalus asmens identifikatorius. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Asmens vardas. */
    @Column(nullable = false, length = 100)
    private String firstName;

    /** Asmens pavardė. */
    @Column(nullable = false, length = 100)
    private String lastName;

    /** Asmens el. pašto adresas. */
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /** Tuščias konstruktorius, reikalingas JPA. */
    public Person() {
    }

    /**
     * Konstruktorius asmeniui sukurti.
     *
     * @param firstName vardas
     * @param lastName pavardė
     * @param email el. pašto adresas
     */
    public Person(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * @return asmens identifikatorius
     */
    public Long getId() {
        return id;
    }

    /**
     * @return asmens vardas
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Nustato asmens vardą.
     *
     * @param firstName vardas
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return asmens pavardė
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Nustato asmens pavardę.
     *
     * @param lastName pavardė
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return asmens el. paštas
     */
    public String getEmail() {
        return email;
    }

    /**
     * Nustato asmens el. pašto adresą.
     *
     * @param email el. pašto adresas
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
