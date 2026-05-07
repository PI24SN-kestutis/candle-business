package lt.viko.eif.kskrebe.candlebusiness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Pagrindinė programos paleidimo klasė.
 * <p>
 * Ji inicijuoja Spring Boot aplikaciją.
 */
@SpringBootApplication
public class CandleBusinessApplication {

    /**
     * Programos paleidimo taškas.
     *
     * @param args paleidimo argumentai iš komandų eilutės
     */
    public static void main(String[] args) {
        SpringApplication.run(CandleBusinessApplication.class, args);
    }

}
