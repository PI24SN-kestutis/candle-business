package lt.viko.eif.kskrebe.candlebusiness.service;

import lt.viko.eif.kskrebe.candlebusiness.dto.CreateOrderItemRequest;
import lt.viko.eif.kskrebe.candlebusiness.dto.CreateOrderRequest;
import lt.viko.eif.kskrebe.candlebusiness.model.CustomerOrder;
import lt.viko.eif.kskrebe.candlebusiness.model.CustomerOrderItem;
import lt.viko.eif.kskrebe.candlebusiness.model.OrderStatus;
import lt.viko.eif.kskrebe.candlebusiness.model.OrderType;
import lt.viko.eif.kskrebe.candlebusiness.model.Product;
import lt.viko.eif.kskrebe.candlebusiness.model.User;
import lt.viko.eif.kskrebe.candlebusiness.model.UserRole;
import lt.viko.eif.kskrebe.candlebusiness.repository.CustomerOrderRepository;
import lt.viko.eif.kskrebe.candlebusiness.repository.ProductRepository;
import lt.viko.eif.kskrebe.candlebusiness.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Verslo logika kliento užsakymams.
 */
@Service
public class OrderService {

    private final CustomerOrderRepository customerOrderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderService(CustomerOrderRepository customerOrderRepository,
                        UserRepository userRepository,
                        ProductRepository productRepository) {
        this.customerOrderRepository = customerOrderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    /**
     * Sukuria naują kliento užsakymą.
     */
    @Transactional
    public CustomerOrder createOrder(CreateOrderRequest request) {
        validateRequest(request);

        User client = findClient(request.getClientId());
        CustomerOrder order = new CustomerOrder();
        order.setClient(client);
        order.setOrderType(request.getOrderType());
        order.setCreatedAt(LocalDateTime.now());

        applyItemsAndTotals(order, request);
        order.setStatus(resolveInitialStatus(order.getOrderType()));

        return customerOrderRepository.save(order);
    }

    /**
     * Atnaujina esamą užsakymą.
     */
    @Transactional
    public CustomerOrder updateOrder(Long orderId, CreateOrderRequest request) {
        validateRequest(request);

        CustomerOrder order = customerOrderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Užsakymas nerastas: " + orderId));

        validateEditable(order);
        User client = findClient(request.getClientId());

        order.setClient(client);
        order.setOrderType(request.getOrderType());
        applyItemsAndTotals(order, request);
        order.setStatus(resolveInitialStatus(order.getOrderType()));

        return customerOrderRepository.save(order);
    }

    /**
     * Pašalina užsakymą.
     */
    @Transactional
    public void deleteOrder(Long orderId) {
        CustomerOrder order = customerOrderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Užsakymas nerastas: " + orderId));

        if (order.getStatus() == OrderStatus.UZBAIGTAS) {
            throw new IllegalArgumentException("Užbaigto užsakymo trinti negalima");
        }

        customerOrderRepository.delete(order);
    }

    /**
     * Atšaukia užsakymą.
     */
    @Transactional
    public CustomerOrder cancelOrder(Long orderId) {
        CustomerOrder order = customerOrderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Užsakymas nerastas: " + orderId));

        if (order.getStatus() == OrderStatus.UZBAIGTAS) {
            throw new IllegalArgumentException("Užbaigto užsakymo atšaukti negalima");
        }

        order.setStatus(OrderStatus.ATSAUKTAS);
        return customerOrderRepository.save(order);
    }

    /**
     * Klientas atsiima ir apmoka užsakymą.
     */
    @Transactional
    public CustomerOrder completeOrder(Long orderId) {
        CustomerOrder order = customerOrderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Užsakymas nerastas: " + orderId));

        if (order.getStatus() == OrderStatus.ATSAUKTAS) {
            throw new IllegalArgumentException("Atšaukto užsakymo užbaigti negalima");
        }
        if (order.getStatus() == OrderStatus.UZBAIGTAS) {
            throw new IllegalArgumentException("Užsakymas jau užbaigtas");
        }

        if (order.getOrderType() == OrderType.GAMYBA_PAGAL_UZSAKYMA && order.getStatus() != OrderStatus.PARUOSTAS) {
            throw new IllegalArgumentException("Pagal užsakymą gamintas produktas dar neparuoštas atsiėmimui");
        }

        if (order.getOrderType() == OrderType.PIRKIMAS_IS_SANDELIO) {
            decrementStock(order.getItems());
        }

        order.setStatus(OrderStatus.UZBAIGTAS);
        return customerOrderRepository.save(order);
    }

    /**
     * Perskaičiuoja bendrą sumą.
     */
    @Transactional
    public CustomerOrder recalculateOrderTotal(Long orderId) {
        CustomerOrder order = customerOrderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Užsakymas nerastas: " + orderId));

        order.setTotalAmount(calculateTotalAmount(order.getItems()));
        return customerOrderRepository.save(order);
    }

    /**
     * Suranda vieną užsakymą.
     */
    @Transactional(readOnly = true)
    public CustomerOrder getOrderById(Long orderId) {
        return customerOrderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Užsakymas nerastas: " + orderId));
    }

    /**
     * Suranda kliento užsakymus, galima filtruoti pagal būseną.
     */
    @Transactional(readOnly = true)
    public List<CustomerOrder> getOrdersByClientId(Long clientId, OrderStatus status) {
        if (status == null) {
            return customerOrderRepository.findByClientIdWithItems(clientId);
        }
        return customerOrderRepository.findByClientIdAndStatusWithItems(clientId, status);
    }

    /**
     * Suranda kliento užsakymus.
     */
    @Transactional(readOnly = true)
    public List<CustomerOrder> getOrdersByClientId(Long clientId) {
        return getOrdersByClientId(clientId, null);
    }

    /**
     * Suranda visus užsakymus darbuotojo puslapiui.
     */
    @Transactional(readOnly = true)
    public List<CustomerOrder> getAllOrders(OrderStatus status) {
        if (status == null) {
            return customerOrderRepository.findAllWithItems();
        }
        return customerOrderRepository.findByStatusWithItems(status);
    }

    public BigDecimal calculateTotalAmount(List<CustomerOrderItem> orderItems) {
        return orderItems.stream()
                .map(this::calculateLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void applyItemsAndTotals(CustomerOrder order, CreateOrderRequest request) {
        List<CustomerOrderItem> orderItems = buildOrderItems(order, request.getItems(), request.getOrderType());
        BigDecimal totalAmount = calculateTotalAmount(orderItems);
        order.getItems().clear();
        order.getItems().addAll(orderItems);
        order.setTotalAmount(totalAmount);
    }

    private List<CustomerOrderItem> buildOrderItems(CustomerOrder order,
                                                    List<CreateOrderItemRequest> itemRequests,
                                                    OrderType orderType) {
        List<CustomerOrderItem> orderItems = new ArrayList<>();

        for (CreateOrderItemRequest itemRequest : itemRequests) {
            validateItemRequest(itemRequest);

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Produktas nerastas: " + itemRequest.getProductId()));

            if (orderType == OrderType.PIRKIMAS_IS_SANDELIO) {
                validateStock(product, itemRequest.getQuantity());
            }

            CustomerOrderItem orderItem = new CustomerOrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItems.add(orderItem);
        }

        return orderItems;
    }

    private BigDecimal calculateLineTotal(CustomerOrderItem orderItem) {
        return orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
    }

    private void decrementStock(List<CustomerOrderItem> items) {
        for (CustomerOrderItem item : items) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Produktas nerastas: " + item.getProduct().getId()));
            validateStock(product, item.getQuantity());
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }
    }

    private void validateStock(Product product, Integer quantity) {
        int stock = product.getStockQuantity() == null ? 0 : product.getStockQuantity();
        if (stock < quantity) {
            throw new IllegalArgumentException("Produkto " + product.getName() + " sandėlyje nepakanka. Likutis: " + stock);
        }
    }

    private User findClient(Long clientId) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Klientas nerastas: " + clientId));

        if (client.getRole() != UserRole.KLIENTAS) {
            throw new IllegalArgumentException("Užsakymą gali kurti tik naudotojas su role KLIENTAS");
        }

        return client;
    }

    private OrderStatus resolveInitialStatus(OrderType orderType) {
        return orderType == OrderType.PIRKIMAS_IS_SANDELIO ? OrderStatus.PARUOSTAS : OrderStatus.NAUJAS;
    }

    private void validateEditable(CustomerOrder order) {
        if (order.getStatus() == OrderStatus.ATSAUKTAS || order.getStatus() == OrderStatus.UZBAIGTAS) {
            throw new IllegalArgumentException("Šio užsakymo redaguoti nebegalima");
        }
        if (order.getOrderType() == OrderType.GAMYBA_PAGAL_UZSAKYMA
                && (order.getStatus() == OrderStatus.VYKDOMAS || order.getStatus() == OrderStatus.PARUOSTAS)) {
            throw new IllegalArgumentException("Gaminamo arba jau paruošto užsakymo redaguoti negalima");
        }
    }

    private void validateRequest(CreateOrderRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Užsakymo duomenys negali būti null");
        }
        if (request.getClientId() == null) {
            throw new IllegalArgumentException("Kliento identifikatorius yra privalomas");
        }
        if (request.getOrderType() == null) {
            throw new IllegalArgumentException("Užsakymo tipas yra privalomas");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Užsakymas privalo turėti bent vieną eilutę");
        }
    }

    private void validateItemRequest(CreateOrderItemRequest itemRequest) {
        if (itemRequest == null) {
            throw new IllegalArgumentException("Užsakymo eilutė negali būti null");
        }
        if (itemRequest.getProductId() == null) {
            throw new IllegalArgumentException("Produkto identifikatorius yra privalomas");
        }
        if (itemRequest.getQuantity() == null || itemRequest.getQuantity() <= 0) {
            throw new IllegalArgumentException("Produkto kiekis turi būti didesnis už nulį");
        }
    }
}
