package lt.viko.eif.kskrebe.candlebusiness.service;

import lt.viko.eif.kskrebe.candlebusiness.dto.ManagerDashboardResponse;
import lt.viko.eif.kskrebe.candlebusiness.model.ProductPriceAudit;
import lt.viko.eif.kskrebe.candlebusiness.model.UserRole;
import lt.viko.eif.kskrebe.candlebusiness.repository.CustomerOrderRepository;
import lt.viko.eif.kskrebe.candlebusiness.repository.ExpenseRepository;
import lt.viko.eif.kskrebe.candlebusiness.repository.IngredientRepository;
import lt.viko.eif.kskrebe.candlebusiness.repository.ProductPriceAuditRepository;
import lt.viko.eif.kskrebe.candlebusiness.repository.ProductRepository;
import lt.viko.eif.kskrebe.candlebusiness.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Verslo logika vadovo suvestinei.
 */
@Service
public class ManagerDashboardService {

    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final ExpenseRepository expenseRepository;
    private final ProductPriceAuditRepository productPriceAuditRepository;

    /**
     * Sukuria servisa su reikalingomis saugyklomis.
     *
     * @param productRepository produktu saugykla
     * @param ingredientRepository ingredientu saugykla
     * @param userRepository naudotoju saugykla
     * @param customerOrderRepository uzsakymu saugykla
     * @param expenseRepository islaidu saugykla
     * @param productPriceAuditRepository kainu audito saugykla
     */
    public ManagerDashboardService(ProductRepository productRepository,
                                   IngredientRepository ingredientRepository,
                                   UserRepository userRepository,
                                   CustomerOrderRepository customerOrderRepository,
                                   ExpenseRepository expenseRepository,
                                   ProductPriceAuditRepository productPriceAuditRepository) {
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
        this.userRepository = userRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.expenseRepository = expenseRepository;
        this.productPriceAuditRepository = productPriceAuditRepository;
    }

    /**
     * Surenka vadovo suvestines duomenis is DB.
     *
     * @return suvestines duomenys
     */
    @Transactional(readOnly = true)
    public ManagerDashboardResponse getDashboard() {
        ManagerDashboardResponse response = new ManagerDashboardResponse();
        response.setProductCount(productRepository.count());
        response.setIngredientCount(ingredientRepository.count());
        response.setClientCount(userRepository.countByRole(UserRole.KLIENTAS));
        response.setEmployeeCount(userRepository.countByRole(UserRole.DARBUOTOJAS));

        BigDecimal totalRevenue = customerOrderRepository.findAll().stream()
                .map(order -> order.getTotalAmount() == null ? BigDecimal.ZERO : order.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = expenseRepository.findAll().stream()
                .map(expense -> expense.getAmount() == null ? BigDecimal.ZERO : expense.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        response.setTotalRevenue(totalRevenue);
        response.setTotalExpenses(totalExpenses);
        response.setBalance(totalRevenue.subtract(totalExpenses));
        return response;
    }

    public List<ProductPriceAudit> getPriceAudits() {
        return productPriceAuditRepository.findAll();
    }
}
