package lt.viko.eif.kskrebe.candlebusiness.service;

import lt.viko.eif.kskrebe.candlebusiness.model.Expense;
import lt.viko.eif.kskrebe.candlebusiness.model.User;
import lt.viko.eif.kskrebe.candlebusiness.model.UserRole;
import lt.viko.eif.kskrebe.candlebusiness.repository.ExpenseRepository;
import lt.viko.eif.kskrebe.candlebusiness.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Verslo logika buhalterio registruojamoms išlaidoms.
 */
@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    /**
     * Išsaugo naują išlaidą.
     */
    @Transactional
    public Expense registerExpense(Expense expense, Long financierId) {
        if (expense == null) {
            throw new IllegalArgumentException("Išlaidos duomenys negali būti null");
        }
        User financier = findFinancier(financierId);
        expense.setCreatedByFinancier(financier);
        return expenseRepository.save(expense);
    }

    /**
     * Atnaujina išlaidą.
     */
    @Transactional
    public Expense updateExpense(Long expenseId, Expense expenseData, Long financierId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Išlaida nerasta: " + expenseId));

        User financier = findFinancier(financierId);
        expense.setCategory(expenseData.getCategory());
        expense.setDescription(expenseData.getDescription());
        expense.setAmount(expenseData.getAmount());
        expense.setExpenseDate(expenseData.getExpenseDate());
        expense.setCreatedByFinancier(financier);
        return expenseRepository.save(expense);
    }

    /**
     * Pašalina išlaidą.
     */
    @Transactional
    public void deleteExpense(Long expenseId) {
        if (!expenseRepository.existsById(expenseId)) {
            throw new IllegalArgumentException("Išlaida nerasta: " + expenseId);
        }
        expenseRepository.deleteById(expenseId);
    }

    @Transactional(readOnly = true)
    public List<Expense> getExpensesByFinancier(Long financierId) {
        return expenseRepository.findByCreatedByFinancierIdWithFinancier(financierId);
    }

    @Transactional(readOnly = true)
    public List<Expense> getExpensesByDateRange(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Datos intervalui privaloma nurodyti abi datas");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Periodo pradžia negali būti vėlesnė už pabaigą");
        }
        return expenseRepository.findByExpenseDateBetweenWithFinancier(start, end);
    }

    @Transactional(readOnly = true)
    public Expense getExpenseById(Long expenseId) {
        return expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Išlaida nerasta: " + expenseId));
    }

    private User findFinancier(Long financierId) {
        if (financierId == null) {
            throw new IllegalArgumentException("Buhalterio identifikatorius yra privalomas");
        }

        User financier = userRepository.findById(financierId)
                .orElseThrow(() -> new IllegalArgumentException("Buhalteris nerastas: " + financierId));

        if (financier.getRole() != UserRole.BUHALTERIS) {
            throw new IllegalArgumentException("Išlaidą gali registruoti tik naudotojas su role BUHALTERIS");
        }
        return financier;
    }
}