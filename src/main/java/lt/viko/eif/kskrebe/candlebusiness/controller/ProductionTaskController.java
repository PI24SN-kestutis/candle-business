package lt.viko.eif.kskrebe.candlebusiness.controller;

import lt.viko.eif.kskrebe.candlebusiness.dto.CreateProductionTaskRequest;
import lt.viko.eif.kskrebe.candlebusiness.dto.ProductionTaskResponse;
import lt.viko.eif.kskrebe.candlebusiness.model.ProductionTask;
import lt.viko.eif.kskrebe.candlebusiness.service.ProductionTaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * HTTP valdiklis darbuotojo gamybos pazymejimams.
 */
@RestController
@RequestMapping("/api/production-tasks")
public class ProductionTaskController {

    private final ProductionTaskService productionTaskService;

    /**
     * Sukuria valdikli su gamybos servisu.
     *
     * @param productionTaskService gamybos servisas
     */
    public ProductionTaskController(ProductionTaskService productionTaskService) {
        this.productionTaskService = productionTaskService;
    }

    /**
     * Uzregistruoja nauja pagaminimo irasa.
     *
     * @param request gamybos duomenys
     * @return sukurtas irasas
     */
    @PostMapping
    public ResponseEntity<ProductionTaskResponse> createProductionTask(@RequestBody CreateProductionTaskRequest request) {
        ProductionTask createdTask = productionTaskService.createProductionTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(createdTask));
    }

    /**
     * Grazina visus gamybos irasus pagal darbuotoja.
     *
     * @param employeeId darbuotojo identifikatorius
     * @return irasu sarasas
     */
    @GetMapping(params = "employeeId")
    public List<ProductionTaskResponse> getByEmployee(@RequestParam Long employeeId) {
        return productionTaskService.getProductionTasksByEmployee(employeeId).stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Grazina visus gamybos irasus pagal klienta.
     *
     * @param clientId kliento identifikatorius
     * @return irasu sarasas
     */
    @GetMapping(params = "clientId")
    public List<ProductionTaskResponse> getByClient(@RequestParam Long clientId) {
        return productionTaskService.getProductionTasksByClient(clientId).stream()
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

    private ProductionTaskResponse toResponse(ProductionTask task) {
        ProductionTaskResponse response = new ProductionTaskResponse();
        response.setId(task.getId());
        response.setOrderItemId(task.getOrderItem().getId());
        response.setOrderId(task.getOrderItem().getOrder().getId());
        response.setProductId(task.getOrderItem().getProduct().getId());
        response.setProductName(task.getOrderItem().getProduct().getName());
        response.setEmployeeId(task.getEmployee().getId());
        response.setEmployeeUsername(task.getEmployee().getUsername());
        response.setProducedForClientId(task.getProducedForClient().getId());
        response.setProducedForClientUsername(task.getProducedForClient().getUsername());
        response.setProducedAt(task.getProducedAt());
        response.setNote(task.getNote());
        return response;
    }
}
