package lt.viko.eif.kskrebe.candlebusiness.repository;

import lt.viko.eif.kskrebe.candlebusiness.model.Expense;
import lt.viko.eif.kskrebe.candlebusiness.model.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * Duomenu prieiga finansinems islaidoms.
 */
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    /**
     * Suranda islaidas pagal kategorija.
     *
     * @param category islaidos kategorija
     * @return islaidu sarasas
     */
    List<Expense> findByCategory(ExpenseCategory category);

    /**
     * Suranda islaidas pagal buhalteri.
     *
     * @param createdByFinancierId buhalterio identifikatorius
     * @return islaidu sarasas
     */
    List<Expense> findByCreatedByFinancierId(Long createdByFinancierId);

    /**
     * Suranda islaidas pagal buhalteri ir iskart uzkrauna buhalterio duomenis.
     *
     * @param createdByFinancierId buhalterio identifikatorius
     * @return islaidu sarasas
     */
    @Query("""
            select e
            from Expense e
            join fetch e.createdByFinancier
            where e.createdByFinancier.id = :createdByFinancierId
            order by e.expenseDate desc, e.id desc
            """)
    List<Expense> findByCreatedByFinancierIdWithFinancier(Long createdByFinancierId);

    /**
     * Suranda islaidas pagal datos intervala.
     *
     * @param start periodo pradzia
     * @param end periodo pabaiga
     * @return islaidu sarasas
     */
    List<Expense> findByExpenseDateBetween(LocalDate start, LocalDate end);

    /**
     * Suranda islaidas pagal datos intervala ir iskart uzkrauna buhalteri.
     *
     * @param start periodo pradzia
     * @param end periodo pabaiga
     * @return islaidu sarasas
     */
    @Query("""
            select e
            from Expense e
            join fetch e.createdByFinancier
            where e.expenseDate between :start and :end
            order by e.expenseDate desc, e.id desc
            """)
    List<Expense> findByExpenseDateBetweenWithFinancier(LocalDate start, LocalDate end);
}
