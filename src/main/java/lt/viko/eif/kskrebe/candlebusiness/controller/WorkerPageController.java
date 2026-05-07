package lt.viko.eif.kskrebe.candlebusiness.controller;

import lt.viko.eif.kskrebe.candlebusiness.model.OrderStatus;
import lt.viko.eif.kskrebe.candlebusiness.model.UserRole;
import lt.viko.eif.kskrebe.candlebusiness.repository.UserRepository;
import lt.viko.eif.kskrebe.candlebusiness.service.OrderService;
import lt.viko.eif.kskrebe.candlebusiness.service.ProductionTaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Darbuotojo puslapio MVC valdiklis.
 */
@Controller
public class WorkerPageController {

    private final UserRepository userRepository;
    private final OrderService orderService;
    private final ProductionTaskService productionTaskService;

    public WorkerPageController(UserRepository userRepository,
                                OrderService orderService,
                                ProductionTaskService productionTaskService) {
        this.userRepository = userRepository;
        this.orderService = orderService;
        this.productionTaskService = productionTaskService;
    }

    @GetMapping("/darbuotojas")
    public String workerPage(@RequestParam(required = false) Long employeeId,
                             @RequestParam(required = false) OrderStatus status,
                             Model model) {
        model.addAttribute("employees", userRepository.findByRole(UserRole.DARBUOTOJAS));
        model.addAttribute("orders", orderService.getAllOrders(status));
        model.addAttribute("orderStatuses", OrderStatus.values());
        model.addAttribute("selectedEmployeeId", employeeId);
        model.addAttribute("selectedStatus", status);
        return "worker-page";
    }

    @PostMapping("/darbuotojas/uzsakymai/{orderId}/gaminti")
    public String produceOrder(@PathVariable Long orderId,
                               @RequestParam Long employeeId,
                               @RequestParam(required = false) String note,
                               @RequestParam(required = false) OrderStatus status,
                               RedirectAttributes redirectAttributes) {
        try {
            productionTaskService.produceOrder(orderId, employeeId, note);
            redirectAttributes.addFlashAttribute("successMessage", "Užsakymas pagamintas ir paruoštas atsiėmimui.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return buildRedirect(employeeId, status);
    }

    @PostMapping("/darbuotojas/uzsakymai/{orderId}/atsaukti")
    public String cancelOrder(@PathVariable Long orderId,
                              @RequestParam(required = false) Long employeeId,
                              @RequestParam(required = false) OrderStatus status,
                              RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(orderId);
            redirectAttributes.addFlashAttribute("successMessage", "Užsakymas atšauktas.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return buildRedirect(employeeId, status);
    }

    private String buildRedirect(Long employeeId, OrderStatus status) {
        StringBuilder redirect = new StringBuilder("redirect:/darbuotojas");
        boolean hasParam = false;
        if (employeeId != null) {
            redirect.append(hasParam ? '&' : '?').append("employeeId=").append(employeeId);
            hasParam = true;
        }
        if (status != null) {
            redirect.append(hasParam ? '&' : '?').append("status=").append(status.name());
        }
        return redirect.toString();
    }
}