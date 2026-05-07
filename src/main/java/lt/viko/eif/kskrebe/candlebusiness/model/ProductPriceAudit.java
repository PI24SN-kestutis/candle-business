package lt.viko.eif.kskrebe.candlebusiness.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_price_audit")
public class ProductPriceAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long audit_id;
    private Long product_id;
    private BigDecimal old_price;
    private BigDecimal new_price;
    private LocalDateTime change_timestamp;
    private String changed_by;

    // Getters and setters

    public Long getAudit_id() {
        return audit_id;
    }

    public void setAudit_id(Long audit_id) {
        this.audit_id = audit_id;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public BigDecimal getOld_price() {
        return old_price;
    }

    public void setOld_price(BigDecimal old_price) {
        this.old_price = old_price;
    }

    public BigDecimal getNew_price() {
        return new_price;
    }

    public void setNew_price(BigDecimal new_price) {
        this.new_price = new_price;
    }

    public LocalDateTime getChange_timestamp() {
        return change_timestamp;
    }

    public void setChange_timestamp(LocalDateTime change_timestamp) {
        this.change_timestamp = change_timestamp;
    }

    public String getChanged_by() {
        return changed_by;
    }

    public void setChanged_by(String changed_by) {
        this.changed_by = changed_by;
    }
}
