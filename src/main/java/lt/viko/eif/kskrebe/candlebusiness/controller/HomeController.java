package lt.viko.eif.kskrebe.candlebusiness.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Pradinio puslapio ir bendrų maršrutų MVC valdiklis.
 */
@Controller
public class HomeController {

    /**
     * Atvaizduoja pradinį puslapį su rolių pasirinkimais.
     *
     * @return pradinio puslapio šablonas
     */
    @GetMapping("/")
    public String home() {
        return "home";
    }

    /**
     * Atvaizduoja bendrą rolės puslapį, jei konkreti zona dar neįgyvendinta.
     *
     * @param role rolės identifikatorius URL adrese
     * @param model duomenys šablonui
     * @return rolės puslapio šablonas
     */
    @GetMapping("/{role:^[^.]+$}")
    public String rolePage(@PathVariable String role, Model model) {
        String title = switch (role) {
            case "klientas" -> "Kliento zona";
            default -> "Nežinoma zona";
        };

        String description = switch (role) {
            case "klientas" -> "Čia bus užsakymų pateikimas ir užsakymų peržiūra.";
            default -> "Pasirinkta nuoroda dar nėra aprašyta.";
        };

        model.addAttribute("role", role);
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        return "role";
    }
}