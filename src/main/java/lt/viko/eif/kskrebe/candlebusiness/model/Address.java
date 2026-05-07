package lt.viko.eif.kskrebe.candlebusiness.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Adreso modelis tiekejams ir kitiems verslo subjektams.
 */
@Entity
public class Address {

    /** Unikalus adreso identifikatorius. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Salis. */
    @Column(nullable = false, length = 100)
    private String country;

    /** Gatve. */
    @Column(nullable = false, length = 150)
    private String street;

    /** Namo numeris. */
    @Column(nullable = false, length = 20)
    private String houseNumber;

    /** Patalpos numeris, jei yra. */
    @Column(length = 20)
    private String roomNumber;

    public Address() {
    }

    public Address(String country, String street, String houseNumber, String roomNumber) {
        this.country = country;
        this.street = street;
        this.houseNumber = houseNumber;
        this.roomNumber = roomNumber;
    }

    public Long getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}
