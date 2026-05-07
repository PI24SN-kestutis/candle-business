package lt.viko.eif.kskrebe.candlebusiness.controller;

import lt.viko.eif.kskrebe.candlebusiness.dto.CreateOrderRequest;
import lt.viko.eif.kskrebe.candlebusiness.dto.OrderItemResponse;
import lt.viko.eif.kskrebe.candlebusiness.dto.OrderResponse;
import lt.viko.eif.kskrebe.candlebusiness.model.CustomerOrder;
import lt.viko.eif.kskrebe.candlebusiness.model.CustomerOrderItem;
import lt.viko.eif.kskrebe.candlebusiness.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * HTTP valdiklis kliento uzsakymams valdyti.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * Sukuria valdikli su uzsakymu servisu.
     *
     * @param orderService uzsakymu servisas
     */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Sukuria nauja kliento uzsakyma.
     *
     * @param request uzsakymo duomenys
     * @return sukurtas uzsakymas
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        CustomerOrder createdOrder = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(createdOrder));
    }

    /**
     * Grazina viena uzsakyma pagal jo identifikatoriu.
     *
     * @param orderId uzsakymo identifikatorius
     * @return uzsakymo duomenys
     */
    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable Long orderId) {
        return toResponse(orderService.getOrderById(orderId));
    }

    /**
     * Grazina visus uzsakymus pagal kliento identifikatoriu.
     *
     * @param clientId kliento identifikatorius
     * @return uzsakymu sarasas
     */
    @GetMapping
    public List<OrderResponse> getOrdersByClient(@RequestParam Long clientId) {
        return orderService.getOrdersByClientId(clientId).stream()
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

    private OrderResponse toResponse(CustomerOrder order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setClientId(order.getClient().getId());
        response.setClientUsername(order.getClient().getUsername());
        response.setOrderType(order.getOrderType());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setTotalAmount(order.getTotalAmount());
        response.setItems(order.getItems().stream()
                .map(this::toItemResponse)
                .toList());
        return response;
    }

    private OrderItemResponse toItemResponse(CustomerOrderItem item) {
        OrderItemResponse response = new OrderItemResponse();
        response.setId(item.getId());
        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProduct().getName());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setLineTotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        return response;
    }
}
