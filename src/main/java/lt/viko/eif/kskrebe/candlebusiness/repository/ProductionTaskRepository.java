package lt.viko.eif.kskrebe.candlebusiness.repository;

import lt.viko.eif.kskrebe.candlebusiness.model.ProductionTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Duomenu prieiga gamybos irasams.
 */
public interface ProductionTaskRepository extends JpaRepository<ProductionTask, Long> {

    /**
     * Suranda gamybos irasus pagal darbuotoja.
     *
     * @param employeeId darbuotojo identifikatorius
     * @return irasu sarasas
     */
    List<ProductionTask> findByEmployeeId(Long employeeId);

    /**
     * Suranda gamybos irasus pagal klienta.
     *
     * @param producedForClientId kliento identifikatorius
     * @return irasu sarasas
     */
    List<ProductionTask> findByProducedForClientId(Long producedForClientId);

    /**
     * Suranda gamybos irasus pagal uzsakymo eilute.
     *
     * @param orderItemId uzsakymo eilutes identifikatorius
     * @return irasu sarasas
     */
    List<ProductionTask> findByOrderItemId(Long orderItemId);
}
