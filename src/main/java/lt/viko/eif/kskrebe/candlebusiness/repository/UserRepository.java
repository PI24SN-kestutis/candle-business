package lt.viko.eif.kskrebe.candlebusiness.repository;

import lt.viko.eif.kskrebe.candlebusiness.model.User;
import lt.viko.eif.kskrebe.candlebusiness.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Duomenu prieiga naudotojams.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Suranda naudotoja pagal prisijungimo varda.
     *
     * @param username naudotojo vardas
     * @return rastas naudotojas
     */
    Optional<User> findByUsername(String username);

    /**
     * Suranda naudotojus pagal role.
     *
     * @param role naudotojo role
     * @return naudotoju sarasas
     */
    List<User> findByRole(UserRole role);

    /**
     * Suskaiciuoja naudotojus pagal role.
     *
     * @param role naudotojo role
     * @return naudotoju kiekis
     */
    long countByRole(UserRole role);
}
