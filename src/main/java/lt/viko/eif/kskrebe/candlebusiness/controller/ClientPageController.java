package lt.viko.eif.kskrebe.candlebusiness.controller;

import lt.viko.eif.kskrebe.candlebusiness.dto.ClientOrderForm;
import lt.viko.eif.kskrebe.candlebusiness.dto.ClientOrderItemForm;
import lt.viko.eif.kskrebe.candlebusiness.dto.CreateOrderItemRequest;
import lt.viko.eif.kskrebe.candlebusiness.dto.CreateOrderRequest;
import lt.viko.eif.kskrebe.candlebusiness.model.CustomerOrder;
import lt.viko.eif.kskrebe.candlebusiness.model.OrderStatus;
import lt.viko.eif.kskrebe.candlebusiness.model.OrderType;
import lt.viko.eif.kskrebe.candlebusiness.model.Product;
import lt.viko.eif.kskrebe.candlebusiness.model.UserRole;
import lt.viko.eif.kskrebe.candlebusiness.repository.ProductRepository;
import lt.viko.eif.kskrebe.candlebusiness.repository.UserRepository;
import lt.viko.eif.kskrebe.candlebusiness.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Kliento puslapio MVC valdiklis.
 */
@Controller
public class ClientPageController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;

    public ClientPageController(ProductRepository productRepository,
                                UserRepository userRepository,
                                OrderService orderService) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    @GetMapping("/klientas")
    public String clientPage(@RequestParam(required = false) Long clientId,
                             @RequestParam(required = false) OrderStatus status,
                             @RequestParam(required = false) Long editOrderId,
                             Model model) {
        List<Product> products = productRepository.findAllWithIngredients();
        model.addAttribute("products", products);
        model.addAttribute("clients", userRepository.findByRole(UserRole.KLIENTAS));
        model.addAttribute("orderTypes", OrderType.values());
        model.addAttribute("orderStatuses", OrderStatus.values());
        model.addAttribute("selectedClientId", clientId);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("clientOrders", clientId == null ? List.of() : orderService.getOrdersByClientId(clientId, status));

        if (!model.containsAttribute("orderForm")) {
            if (editOrderId != null) {
                model.addAttribute("orderForm", createOrderFormFromOrder(orderService.getOrderById(editOrderId), products));
            } else {
                model.addAttribute("orderForm", createDefaultOrderForm(products, clientId));
            }
        }

        return "client-page";
    }

    @PostMapping("/klientas/uzsakymai")
    public String saveClientOrder(@ModelAttribute("orderForm") ClientOrderForm orderForm,
                                  RedirectAttributes redirectAttributes) {
        try {
            List<CreateOrderItemRequest> items = orderForm.getItems().stream()
                    .filter(item -> item.getQuantity() != null && item.getQuantity() > 0)
                    .map(item -> new CreateOrderItemRequest(item.getProductId(), item.getQuantity()))
                    .toList();

            CreateOrderRequest request = new CreateOrderRequest(orderForm.getClientId(), orderForm.getOrderType(), items);
            CustomerOrder savedOrder = orderForm.getOrderId() == null
                    ? orderService.createOrder(request)
                    : orderService.updateOrder(orderForm.getOrderId(), request);

            String message = orderForm.getOrderId() == null
                    ? "Užsakymas sukurtas. Numeris: " + savedOrder.getId()
                    : "Užsakymas atnaujintas. Numeris: " + savedOrder.getId();
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/klientas?clientId=" + orderForm.getClientId();
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            redirectAttributes.addFlashAttribute("orderForm", orderForm);
            return "redirect:/klientas";
        }
    }

    @PostMapping("/klientas/uzsakymai/{orderId}/trinti")
    public String deleteClientOrder(@PathVariable Long orderId,
                                    @RequestParam Long clientId,
                                    RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteOrder(orderId);
            redirectAttributes.addFlashAttribute("successMessage", "Užsakymas ištrintas.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/klientas?clientId=" + clientId;
    }

    @PostMapping("/klientas/uzsakymai/{orderId}/atsaukti")
    public String cancelClientOrder(@PathVariable Long orderId,
                                    @RequestParam Long clientId,
                                    RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(orderId);
            redirectAttributes.addFlashAttribute("successMessage", "Užsakymas atšauktas.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/klientas?clientId=" + clientId;
    }

    @PostMapping("/klientas/uzsakymai/{orderId}/atsiimti")
    public String completeClientOrder(@PathVariable Long orderId,
                                      @RequestParam Long clientId,
                                      RedirectAttributes redirectAttributes) {
        try {
            orderService.completeOrder(orderId);
            redirectAttributes.addFlashAttribute("successMessage", "Užsakymas atsiimtas ir apmokėtas.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/klientas?clientId=" + clientId;
    }

    private ClientOrderForm createDefaultOrderForm(List<Product> products, Long clientId) {
        ClientOrderForm form = new ClientOrderForm();
        form.setClientId(clientId);
        form.setItems(products.stream().map(product -> {
            ClientOrderItemForm item = new ClientOrderItemForm();
            item.setProductId(product.getId());
            item.setQuantity(0);
            return item;
        }).toList());
        return form;
    }

    private ClientOrderForm createOrderFormFromOrder(CustomerOrder order, List<Product> products) {
        ClientOrderForm form = createDefaultOrderForm(products, order.getClient().getId());
        form.setOrderId(order.getId());
        form.setOrderType(order.getOrderType());

        for (ClientOrderItemForm itemForm : form.getItems()) {
            order.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(itemForm.getProductId()))
                    .findFirst()
                    .ifPresent(item -> itemForm.setQuantity(item.getQuantity()));
        }

        return form;
    }
}