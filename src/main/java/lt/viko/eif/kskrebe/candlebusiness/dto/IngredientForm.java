package lt.viko.eif.kskrebe.candlebusiness.dto;

import lt.viko.eif.kskrebe.candlebusiness.model.IngredientType;

/**
 * Vadovo ingrediento forma.
 */
public class IngredientForm {

    private Long id;
    private String name;
    private IngredientType type = IngredientType.NATURALUS;
    private Long supplierId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IngredientType getType() {
        return type;
    }

    public void setType(IngredientType type) {
        this.type = type;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }
}
