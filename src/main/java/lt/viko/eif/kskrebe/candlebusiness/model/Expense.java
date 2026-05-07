package lt.viko.eif.kskrebe.candlebusiness.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Buhalterio uzregistruota islaida.
 */
@Entity
public class Expense {

    /** Unikalus islaidos identifikatorius. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Islaidos kategorija. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ExpenseCategory category;

    /** Islaidos aprasymas. */
    @Column(nullable = false, length = 250)
    private String description;

    /** Islaidos suma. */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /** Islaidos data. */
    @Column(nullable = false)
    private LocalDate expenseDate;

    /** Buhalteris, uzregistraves islaida. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by_financier_id", nullable = false)
    private User createdByFinancier;

    /** Tustias konstruktorius, reikalingas JPA. */
    public Expense() {
    }

    /**
     * Konstruktorius islaidai sukurti.
     *
     * @param category kategorija
     * @param description aprasymas
     * @param amount suma
     * @param expenseDate data
     * @param createdByFinancier buhalteris
     */
    public Expense(ExpenseCategory category, String description, BigDecimal amount, LocalDate expenseDate, User createdByFinancier) {
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.createdByFinancier = createdByFinancier;
    }

    /**
     * @return islaidos identifikatorius
     */
    public Long getId() {
        return id;
    }

    /**
     * @return islaidos kategorija
     */
    public ExpenseCategory getCategory() {
        return category;
    }

    /**
     * Nustato islaidos kategorija.
     *
     * @param category kategorija
     */
    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    /**
     * @return islaidos aprasymas
     */
    public String getDescription() {
        return description;
    }

    /**
     * Nustato islaidos aprasyma.
     *
     * @param description aprasymas
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return islaidos suma
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Nustato islaidos suma.
     *
     * @param amount suma
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return islaidos data
     */
    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    /**
     * Nustato islaidos data.
     *
     * @param expenseDate data
     */
    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    /**
     * @return buhalteris
     */
    public User getCreatedByFinancier() {
        return createdByFinancier;
    }

    /**
     * Nustato buhalteri.
     *
     * @param createdByFinancier buhalteris
     */
    public void setCreatedByFinancier(User createdByFinancier) {
        this.createdByFinancier = createdByFinancier;
    }
}
