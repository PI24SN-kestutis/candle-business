package lt.viko.eif.kskrebe.candlebusiness.repository;

import lt.viko.eif.kskrebe.candlebusiness.model.IngredientType;
import lt.viko.eif.kskrebe.candlebusiness.model.Product;
import lt.viko.eif.kskrebe.candlebusiness.model.ProductionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Duomenu prieiga gaminiams.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Suranda gaminius pagal gamybos tipa.
     *
     * @param productionType gamybos tipas
     * @return gaminiu sarasas
     */
    List<Product> findByProductionType(ProductionType productionType);

    /**
     * Suranda gaminius pagal personalizavimo pozymi.
     *
     * @param customMade ar gaminys pagal uzsakyma
     * @return gaminiu sarasas
     */
    List<Product> findByCustomMade(boolean customMade);

    /**
     * Suranda gaminius, turincius nurodyto tipo ingredientu.
     *
     * @param ingredientType ingrediento tipas
     * @return gaminiu sarasas
     */
    List<Product> findDistinctByIngredientsType(IngredientType ingredientType);

    /**
     * Suranda visus gaminius su ingredientais.
     *
     * @return gaminiu sarasas
     */
    @Query(value = """
            select distinct p
            from Product p
            left join fetch p.ingredients
            """, countQuery = "select count(p) from Product p")
    Page<Product> findAllWithIngredients(Pageable pageable);

    @Query("""
            select distinct p
            from Product p
            left join fetch p.ingredients
            order by p.name
            """)
    List<Product> findAllWithIngredients();

    /**
     * Suranda viena gamini su ingredientais.
     *
     * @param productId gaminio identifikatorius
     * @return gaminys
     */
    @Query("""
            select distinct p
            from Product p
            left join fetch p.ingredients
            where p.id = :productId
            """)
    Optional<Product> findByIdWithIngredients(Long productId);
}
