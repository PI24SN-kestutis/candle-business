package lt.viko.eif.kskrebe.candlebusiness.controller;

import lt.viko.eif.kskrebe.candlebusiness.dto.CreateExpenseRequest;
import lt.viko.eif.kskrebe.candlebusiness.dto.ExpenseResponse;
import lt.viko.eif.kskrebe.candlebusiness.model.Expense;
import lt.viko.eif.kskrebe.candlebusiness.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * HTTP valdiklis buhalterio islaidoms valdyti.
 */
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    /**
     * Sukuria valdikli su islaidu servisu.
     *
     * @param expenseService islaidu servisas
     */
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    /**
     * Uzregistruoja nauja islaida.
     *
     * @param request islaidos duomenys
     * @return sukurta islaida
     */
    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@RequestBody CreateExpenseRequest request) {
        Expense expense = new Expense();
        expense.setCategory(request.getCategory());
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(request.getExpenseDate());

        Expense createdExpense = expenseService.registerExpense(expense, request.getFinancierId());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(createdExpense));
    }

    /**
     * Grazina visas islaidas pagal buhalteri.
     *
     * @param financierId buhalterio identifikatorius
     * @return islaidu sarasas
     */
    @GetMapping(params = "financierId")
    public List<ExpenseResponse> getByFinancier(@RequestParam Long financierId) {
        return expenseService.getExpensesByFinancier(financierId).stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Grazina visas islaidas pagal datos intervala.
     *
     * @param start periodo pradzia
     * @param end periodo pabaiga
     * @return islaidu sarasas
     */
    @GetMapping(params = {"start", "end"})
    public List<ExpenseResponse> getByDateRange(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        return expenseService.getExpensesByDateRange(start, end).stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Sugrazina validacijos arba verslo logikos klaida kaip HTTP 400.
     *
     * @param exception ivyki sukeles klaidos objektas
     * @return klaidos tekstas
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    private ExpenseResponse toResponse(Expense expense) {
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setCategory(expense.getCategory());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setExpenseDate(expense.getExpenseDate());
        response.setFinancierId(expense.getCreatedByFinancier().getId());
        response.setFinancierUsername(expense.getCreatedByFinancier().getUsername());
        return response;
    }
}
