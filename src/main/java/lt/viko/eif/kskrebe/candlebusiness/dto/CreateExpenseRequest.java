package lt.viko.eif.kskrebe.candlebusiness.dto;

import lt.viko.eif.kskrebe.candlebusiness.model.ExpenseCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Islaidos sukurimo duomenys.
 */
public class CreateExpenseRequest {

    /** Islaidos kategorija. */
    private ExpenseCategory category;

    /** Islaidos aprasymas. */
    private String description;

    /** Islaidos suma. */
    private BigDecimal amount;

    /** Islaidos data. */
    private LocalDate expenseDate;

    /** Buhalterio identifikatorius. */
    private Long financierId;

    public CreateExpenseRequest() {
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public Long getFinancierId() {
        return financierId;
    }

    public void setFinancierId(Long financierId) {
        this.financierId = financierId;
    }
}
