package lt.viko.eif.kskrebe.candlebusiness.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Ingrediento modelis, naudojamas gaminių sudėčiai aprašyti.
 */
@Entity
public class Ingredient {

    /** Unikalus ingrediento identifikatorius. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ingrediento pavadinimas. */
    @Column(nullable = false, unique = true, length = 120)
    private String name;

    /** Ingrediento tipas: natūralus arba pramoninis. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IngredientType type;

    /** Ingrediento tiekejas. */
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    /** Tuščias konstruktorius, reikalingas JPA. */
    public Ingredient() {
    }

    /**
     * Konstruktorius ingredientui sukurti.
     *
     * @param name ingrediento pavadinimas
     * @param type ingrediento tipas
     */
    public Ingredient(String name, IngredientType type) {
        this.name = name;
        this.type = type;
    }

    /**
     * @return ingrediento identifikatorius
     */
    public Long getId() {
        return id;
    }

    /**
     * @return ingrediento pavadinimas
     */
    public String getName() {
        return name;
    }

    /**
     * Nustato ingrediento pavadinimą.
     *
     * @param name ingrediento pavadinimas
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return ingrediento tipas
     */
    public IngredientType getType() {
        return type;
    }

    /**
     * Nustato ingrediento tipą.
     *
     * @param type ingrediento tipas
     */
    public void setType(IngredientType type) {
        this.type = type;
    }

    /**
     * @return ingrediento tiekejas
     */
    public Supplier getSupplier() {
        return supplier;
    }

    /**
     * Nustato ingrediento tiekeja.
     *
     * @param supplier tiekejas
     */
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
