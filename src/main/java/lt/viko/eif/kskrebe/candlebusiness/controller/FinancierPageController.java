package lt.viko.eif.kskrebe.candlebusiness.controller;

import lt.viko.eif.kskrebe.candlebusiness.dto.FinancierExpenseForm;
import lt.viko.eif.kskrebe.candlebusiness.model.Expense;
import lt.viko.eif.kskrebe.candlebusiness.model.ExpenseCategory;
import lt.viko.eif.kskrebe.candlebusiness.model.UserRole;
import lt.viko.eif.kskrebe.candlebusiness.repository.UserRepository;
import lt.viko.eif.kskrebe.candlebusiness.service.ExpenseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/**
 * Buhalterio puslapio MVC valdiklis.
 */
@Controller
public class FinancierPageController {

    private final UserRepository userRepository;
    private final ExpenseService expenseService;

    public FinancierPageController(UserRepository userRepository, ExpenseService expenseService) {
        this.userRepository = userRepository;
        this.expenseService = expenseService;
    }

    @GetMapping("/buhalteris")
    public String financierPage(@RequestParam(required = false) Long editExpenseId,
                                Model model) {
        if (!model.containsAttribute("expenseForm")) {
            model.addAttribute("expenseForm", editExpenseId == null ? new FinancierExpenseForm() : toForm(expenseService.getExpenseById(editExpenseId)));
        }
        model.addAttribute("financiers", userRepository.findByRole(UserRole.BUHALTERIS));
        model.addAttribute("expenseCategories", ExpenseCategory.values());
        model.addAttribute("expenses", expenseService.getExpensesByDateRange(LocalDate.of(2000, 1, 1), LocalDate.now()));
        return "financier-page";
    }

    @PostMapping("/buhalteris/islaidos")
    public String saveExpense(@ModelAttribute("expenseForm") FinancierExpenseForm expenseForm,
                              RedirectAttributes redirectAttributes) {
        try {
            Expense expense = new Expense();
            expense.setCategory(expenseForm.getCategory());
            expense.setDescription(expenseForm.getDescription());
            expense.setAmount(expenseForm.getAmount());
            expense.setExpenseDate(expenseForm.getExpenseDate());

            Long expenseId = expenseForm.getExpenseId() == null
                    ? expenseService.registerExpense(expense, expenseForm.getFinancierId()).getId()
                    : expenseService.updateExpense(expenseForm.getExpenseId(), expense, expenseForm.getFinancierId()).getId();

            String message = expenseForm.getExpenseId() == null
                    ? "Išlaida išsaugota. Įrašo numeris: " + expenseId
                    : "Išlaida atnaujinta. Įrašo numeris: " + expenseId;
            redirectAttributes.addFlashAttribute("successMessage", message);
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            redirectAttributes.addFlashAttribute("expenseForm", expenseForm);
        }
        return "redirect:/buhalteris";
    }

    @PostMapping("/buhalteris/islaidos/{expenseId}/trinti")
    public String deleteExpense(@PathVariable Long expenseId,
                                RedirectAttributes redirectAttributes) {
        try {
            expenseService.deleteExpense(expenseId);
            redirectAttributes.addFlashAttribute("successMessage", "Išlaida ištrinta.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/buhalteris";
    }

    private FinancierExpenseForm toForm(Expense expense) {
        FinancierExpenseForm form = new FinancierExpenseForm();
        form.setExpenseId(expense.getId());
        form.setCategory(expense.getCategory());
        form.setDescription(expense.getDescription());
        form.setAmount(expense.getAmount());
        form.setExpenseDate(expense.getExpenseDate());
        form.setFinancierId(expense.getCreatedByFinancier().getId());
        return form;
    }
}