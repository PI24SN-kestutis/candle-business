package lt.viko.eif.kskrebe.candlebusiness;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Bazinis programos konteksto ir MVC puslapių testas.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CandleBusinessApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Patikrina, ar Spring kontekstas sėkmingai pasikrauna.
     */
    @Test
    void contextLoads() {
    }

    /**
     * Patikrina, ar pradinis puslapis pasiekiamas.
     */
    @Test
    void homePageLoads() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Žvakių ir muilo verslo sistema")))
                .andExpect(view().name("home"));
    }

    /**
     * Patikrina, ar vadovo puslapis pasiekiamas.
     */
    @Test
    void managerPageLoads() throws Exception {
        mockMvc.perform(get("/vadovas"))
                .andExpect(status().isOk())
                .andExpect(view().name("manager-dashboard"));
    }

    /**
     * Patikrina, ar kliento puslapis pasiekiamas.
     */
    @Test
    void clientPageLoads() throws Exception {
        mockMvc.perform(get("/klientas"))
                .andExpect(status().isOk())
                .andExpect(view().name("client-page"));
    }

    /**
     * Patikrina, ar darbuotojo puslapis pasiekiamas.
     */
    @Test
    void workerPageLoads() throws Exception {
        mockMvc.perform(get("/darbuotojas"))
                .andExpect(status().isOk())
                .andExpect(view().name("worker-page"));
    }

    /**
     * Patikrina, ar buhalterio puslapis pasiekiamas.
     */
    @Test
    void financierPageLoads() throws Exception {
        mockMvc.perform(get("/buhalteris"))
                .andExpect(status().isOk())
                .andExpect(view().name("financier-page"));
    }
}
