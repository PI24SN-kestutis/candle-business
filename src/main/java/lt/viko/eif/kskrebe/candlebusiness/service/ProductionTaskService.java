package lt.viko.eif.kskrebe.candlebusiness.service;

import lt.viko.eif.kskrebe.candlebusiness.dto.CreateProductionTaskRequest;
import lt.viko.eif.kskrebe.candlebusiness.model.CustomerOrder;
import lt.viko.eif.kskrebe.candlebusiness.model.CustomerOrderItem;
import lt.viko.eif.kskrebe.candlebusiness.model.OrderStatus;
import lt.viko.eif.kskrebe.candlebusiness.model.OrderType;
import lt.viko.eif.kskrebe.candlebusiness.model.ProductionTask;
import lt.viko.eif.kskrebe.candlebusiness.model.User;
import lt.viko.eif.kskrebe.candlebusiness.model.UserRole;
import lt.viko.eif.kskrebe.candlebusiness.repository.CustomerOrderItemRepository;
import lt.viko.eif.kskrebe.candlebusiness.repository.CustomerOrderRepository;
import lt.viko.eif.kskrebe.candlebusiness.repository.ProductionTaskRepository;
import lt.viko.eif.kskrebe.candlebusiness.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Verslo logika darbuotojo gamybos pažymėjimams.
 */
@Service
public class ProductionTaskService {

    private final ProductionTaskRepository productionTaskRepository;
    private final CustomerOrderItemRepository customerOrderItemRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final UserRepository userRepository;

    public ProductionTaskService(ProductionTaskRepository productionTaskRepository,
                                 CustomerOrderItemRepository customerOrderItemRepository,
                                 CustomerOrderRepository customerOrderRepository,
                                 UserRepository userRepository) {
        this.productionTaskRepository = productionTaskRepository;
        this.customerOrderItemRepository = customerOrderItemRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.userRepository = userRepository;
    }

    /**
     * Užregistruoja vienos užsakymo eilutės pagaminimą.
     */
    @Transactional
    public ProductionTask createProductionTask(CreateProductionTaskRequest request) {
        validateRequest(request);

        CustomerOrderItem orderItem = customerOrderItemRepository.findById(request.getOrderItemId())
                .orElseThrow(() -> new IllegalArgumentException("Užsakymo eilutė nerasta: " + request.getOrderItemId()));

        User employee = findEmployee(request.getEmployeeId());
        User client = findClient(request.getProducedForClientId());

        Long orderClientId = orderItem.getOrder().getClient().getId();
        if (!orderClientId.equals(client.getId())) {
            throw new IllegalArgumentException("Pasirinkta užsakymo eilutė nepriklauso nurodytam klientui");
        }

        ProductionTask task = new ProductionTask();
        task.setOrderItem(orderItem);
        task.setEmployee(employee);
        task.setProducedForClient(client);
        task.setProducedAt(LocalDateTime.now());
        task.setNote(request.getNote());

        ProductionTask savedTask = productionTaskRepository.save(task);
        orderItem.getOrder().setStatus(OrderStatus.PARUOSTAS);
        customerOrderRepository.save(orderItem.getOrder());

        return savedTask;
    }

    /**
     * Pažymi visą užsakymą kaip pagamintą.
     */
    @Transactional
    public List<ProductionTask> produceOrder(Long orderId, Long employeeId, String note) {
        CustomerOrder order = customerOrderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Užsakymas nerastas: " + orderId));

        if (order.getOrderType() != OrderType.GAMYBA_PAGAL_UZSAKYMA) {
            throw new IllegalArgumentException("Gaminti galima tik užsakymus, kurie gaminami pagal užsakymą");
        }
        if (order.getStatus() == OrderStatus.ATSAUKTAS || order.getStatus() == OrderStatus.UZBAIGTAS) {
            throw new IllegalArgumentException("Šio užsakymo gaminti negalima");
        }

        User employee = findEmployee(employeeId);
        List<ProductionTask> tasks = new ArrayList<>();

        order.setStatus(OrderStatus.VYKDOMAS);
        for (CustomerOrderItem item : order.getItems()) {
            ProductionTask task = new ProductionTask();
            task.setOrderItem(item);
            task.setEmployee(employee);
            task.setProducedForClient(order.getClient());
            task.setProducedAt(LocalDateTime.now());
            task.setNote(note);
            tasks.add(productionTaskRepository.save(task));
        }

        order.setStatus(OrderStatus.PARUOSTAS);
        customerOrderRepository.save(order);
        return tasks;
    }

    @Transactional(readOnly = true)
    public List<ProductionTask> getProductionTasksByEmployee(Long employeeId) {
        return productionTaskRepository.findByEmployeeId(employeeId);
    }

    @Transactional(readOnly = true)
    public List<ProductionTask> getProductionTasksByClient(Long clientId) {
        return productionTaskRepository.findByProducedForClientId(clientId);
    }

    private User findEmployee(Long employeeId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Darbuotojas nerastas: " + employeeId));

        if (employee.getRole() != UserRole.DARBUOTOJAS) {
            throw new IllegalArgumentException("Gamybą gali pažymėti tik naudotojas su role DARBUOTOJAS");
        }
        return employee;
    }

    private User findClient(Long clientId) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Klientas nerastas: " + clientId));

        if (client.getRole() != UserRole.KLIENTAS) {
            throw new IllegalArgumentException("Pagaminta galima žymėti tik klientui su role KLIENTAS");
        }
        return client;
    }

    private void validateRequest(CreateProductionTaskRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Gamybos pažymėjimo duomenys negali būti null");
        }
        if (request.getOrderItemId() == null) {
            throw new IllegalArgumentException("Užsakymo eilutės identifikatorius yra privalomas");
        }
        if (request.getEmployeeId() == null) {
            throw new IllegalArgumentException("Darbuotojo identifikatorius yra privalomas");
        }
        if (request.getProducedForClientId() == null) {
            throw new IllegalArgumentException("Kliento identifikatorius yra privalomas");
        }
    }
}