package lt.viko.eif.kskrebe.candlebusiness.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Gaminio modelis, pvz. žvakė ar muilas.
 */
@Entity
public class Product {

    /** Unikalus gaminio identifikatorius. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Gaminio pavadinimas. */
    @Column(nullable = false, length = 150)
    private String name;

    /** Gaminio aprašymas. */
    @Column(length = 500)
    private String description;

    /** Gaminio kaina. */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /** Kiek vienetų šiuo metu yra sandėlyje. */
    @Column(nullable = false)
    private Integer stockQuantity = 0;

    /** Gaminio gamybos tipas: rankų darbo arba masinė gamyba. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductionType productionType;

    /** Ar gaminys gaminamas pagal individualų užsakymą. */
    @Column(nullable = false)
    private boolean customMade;

    /** Su gaminiu susieti ingredientai. */
    @ManyToMany
    @JoinTable(
            name = "product_ingredient",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private Set<Ingredient> ingredients = new HashSet<>();

    /** Tuščias konstruktorius, reikalingas JPA. */
    public Product() {
    }

    /**
     * Konstruktorius gaminiui sukurti.
     *
     * @param name gaminio pavadinimas
     * @param description gaminio aprašymas
     * @param price gaminio kaina
     * @param stockQuantity kiekis sandėlyje
     * @param productionType gamybos tipas
     * @param customMade ar gaminys gaminamas pagal užsakymą
     */
    public Product(String name, String description, BigDecimal price, Integer stockQuantity, ProductionType productionType, boolean customMade) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.productionType = productionType;
        this.customMade = customMade;
    }

    /**
     * @return gaminio identifikatorius
     */
    public Long getId() {
        return id;
    }

    /**
     * @return gaminio pavadinimas
     */
    public String getName() {
        return name;
    }

    /**
     * Nustato gaminio pavadinimą.
     *
     * @param name gaminio pavadinimas
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return gaminio aprašymas
     */
    public String getDescription() {
        return description;
    }

    /**
     * Nustato gaminio aprašymą.
     *
     * @param description gaminio aprašymas
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return gaminio kaina
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Nustato gaminio kainą.
     *
     * @param price gaminio kaina
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return kiekis sandėlyje
     */
    public Integer getStockQuantity() {
        return stockQuantity;
    }

    /**
     * Nustato kiekį sandėlyje.
     *
     * @param stockQuantity kiekis sandėlyje
     */
    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    /**
     * @return gamybos tipas
     */
    public ProductionType getProductionType() {
        return productionType;
    }

    /**
     * Nustato gamybos tipą.
     *
     * @param productionType gamybos tipas
     */
    public void setProductionType(ProductionType productionType) {
        this.productionType = productionType;
    }

    /**
     * @return {@code true}, jei gaminys gaminamas pagal užsakymą
     */
    public boolean isCustomMade() {
        return customMade;
    }

    /**
     * Nustato, ar gaminys gaminamas pagal užsakymą.
     *
     * @param customMade personalizavimo požymis
     */
    public void setCustomMade(boolean customMade) {
        this.customMade = customMade;
    }

    /**
     * @return su gaminiu susietų ingredientų rinkinys
     */
    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Nustato gaminio ingredientus.
     *
     * @param ingredients ingredientų rinkinys
     */
    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}