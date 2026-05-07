package lt.viko.eif.kskrebe.candlebusiness.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;

/**
 * Sistemos naudotojo modelis.
 * <p>
 * Paveldi bendrus asmens duomenis iš {@link Person}
 * ir prideda prisijungimo vardą bei rolę.
 */
@Entity
public class User extends Person {

    /** Unikalus naudotojo vardas sistemoje. */
    @Column(nullable = false, unique = true, length = 80)
    private String username;

    /** Naudotojo rolė verslo procese. */
    @Convert(converter = UserRoleConverter.class)
    @Column(nullable = false, length = 30)
    private UserRole role;

    /** Tuščias konstruktorius, reikalingas JPA. */
    public User() {
    }

    /**
     * Konstruktorius naudotojui sukurti.
     *
     * @param firstName vardas
     * @param lastName pavardė
     * @param email el. pašto adresas
     * @param username naudotojo vardas
     * @param role naudotojo rolė
     */
    public User(String firstName, String lastName, String email, String username, UserRole role) {
        super(firstName, lastName, email);
        this.username = username;
        this.role = role;
    }

    /**
     * @return naudotojo vardas
     */
    public String getUsername() {
        return username;
    }

    /**
     * Nustato naudotojo vardą.
     *
     * @param username naudotojo vardas
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return naudotojo rolė
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Nustato naudotojo rolę.
     *
     * @param role naudotojo rolė
     */
    public void setRole(UserRole role) {
        this.role = role;
    }
}
