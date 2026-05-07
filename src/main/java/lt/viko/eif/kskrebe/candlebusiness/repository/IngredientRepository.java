package lt.viko.eif.kskrebe.candlebusiness.repository;

import lt.viko.eif.kskrebe.candlebusiness.model.Ingredient;
import lt.viko.eif.kskrebe.candlebusiness.model.IngredientType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Duomenu prieiga ingredientams.
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    /**
     * Suranda ingredienta pagal pavadinima.
     *
     * @param name ingrediento pavadinimas
     * @return rastas ingredientas
     */
    Optional<Ingredient> findByName(String name);

    /**
     * Suranda ingredientus pagal tipa.
     *
     * @param type ingrediento tipas
     * @return ingredientu sarasas
     */
    List<Ingredient> findByType(IngredientType type);

    /**
     * Patikrina, ar bent vienas ingredientas naudoja nurodyta tiekeja.
     *
     * @param supplierId tiekejo identifikatorius
     * @return ar tiekejas naudojamas
     */
    boolean existsBySupplierId(Long supplierId);
}
