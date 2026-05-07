package lt.viko.eif.kskrebe.candlebusiness.config;

import lt.viko.eif.kskrebe.candlebusiness.model.*;
import lt.viko.eif.kskrebe.candlebusiness.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Uzpildo sistema gausiais demo duomenimis, imituojant 6 mėnesių veiklą.
 */
@Configuration
public class DemoDataSeeder {

    @Bean
    CommandLineRunner seedDemoData(UserRepository userRepository,
                                   AddressRepository addressRepository,
                                   ContractRepository contractRepository,
                                   IngredientRepository ingredientRepository,
                                   ProductRepository productRepository,
                                   CustomerOrderRepository customerOrderRepository,
                                   ExpenseRepository expenseRepository,
                                   ProductionTaskRepository productionTaskRepository,
                                   SupplierRepository supplierRepository) {
        return args -> {
            if (userRepository.count() > 0 || productRepository.count() > 0) {
                return;
            }

            Random random = new Random();

            // 1. Sutartys ir Tiekejai
            Contract contractA = contractRepository.save(new Contract("T-2026-001", "Žaliavų tiekimo sutartis."));
            Contract contractB = contractRepository.save(new Contract("T-2026-002", "Kvapų ir dažų tiekimo sutartis."));

            Address kaunas = addressRepository.save(new Address("Lietuva", "Pramonės g.", "12", null));
            Address vilnius = addressRepository.save(new Address("Lietuva", "Vilniaus g.", "8", "2"));

            Supplier aromatika = new Supplier("Aromatika");
            aromatika.setAddress(vilnius);
            aromatika.setContract(contractB);
            aromatika = supplierRepository.save(aromatika);

            Supplier balticRaw = new Supplier("Baltic Raw");
            balticRaw.setAddress(kaunas);
            balticRaw.setContract(contractA);
            balticRaw = supplierRepository.save(balticRaw);

            // 2. Vartotojai
            User vadovas = userRepository.save(new User("Asta", "Vadovė", "asta@ambera.lt", "asta.vadove", UserRole.VADOVAS));
            User darbuotojas = userRepository.save(new User("Mantas", "Meistras", "mantas@ambera.lt", "mantas.darbuotojas", UserRole.DARBUOTOJAS));
            User buhalteris = userRepository.save(new User("Rūta", "Finansai", "ruta@ambera.lt", "ruta.finansai", UserRole.BUHALTERIS));

            List<User> klientai = new ArrayList<>();
            String[] varda = {"Jonas", "Petras", "Eglė", "Lina", "Andrius", "Kristina", "Marius", "Dovilė", "Tomas", "Rasa", "Giedrius", "Simona", "Karolis", "Laura", "Vytas"};
            String[] pavardes = {"Jonaitis", "Petraitis", "Eglinskė", "Linienė", "Andriulis", "Kristinaitė", "Marijonas", "Dovilienė", "Tomaitis", "Rasienė", "Giedraitis", "Simonavičė", "Karolaitis", "Laurinaitė", "Vytautas"};

            for (int i = 0; i < varda.length; i++) {
                klientai.add(userRepository.save(new User(varda[i], pavardes[i], varda[i].toLowerCase() + "@pavyzdys.lt", varda[i].toLowerCase() + "." + i, UserRole.KLIENTAS)));
            }

            // 3. Ingredientai
            Ingredient vaskas = ingredientRepository.save(new Ingredient("Sojų vaškas", IngredientType.NATURALUS));
            vaskas.setSupplier(balticRaw);
            ingredientRepository.save(vaskas);

            Ingredient levandos = ingredientRepository.save(new Ingredient("Levandų aliejus", IngredientType.NATURALUS));
            levandos.setSupplier(aromatika);
            ingredientRepository.save(levandos);

            Ingredient vanile = ingredientRepository.save(new Ingredient("Vanilės kvapas", IngredientType.PRAMONINIS));
            vanile.setSupplier(aromatika);
            ingredientRepository.save(vanile);

            // 4. Produktai
            List<Product> produktai = new ArrayList<>();
            produktai.add(productRepository.save(new Product("Gintarinė žvakė", "Klasikinė žvakė.", new BigDecimal("12.50"), 50, ProductionType.RANKU_DARBO, true)));
            produktai.add(productRepository.save(new Product("Levandų muilas", "Kvepiantis muilas.", new BigDecimal("6.00"), 100, ProductionType.MASINE_GAMYBA, true)));
            produktai.add(productRepository.save(new Product("Vanilinis rinkinys", "Dovanų rinkinys.", new BigDecimal("25.00"), 20, ProductionType.RANKU_DARBO, true)));

            for (int i = 1; i <= 20; i++) {
                Product p = new Product("Produktas #" + i, "Automatiškai sugeneruotas produktas.", 
                        new BigDecimal(5 + random.nextInt(30) + "." + random.nextInt(99)), 
                        random.nextInt(100), 
                        random.nextBoolean() ? ProductionType.RANKU_DARBO : ProductionType.MASINE_GAMYBA, 
                        true);
                produktai.add(productRepository.save(p));
            }

            // 5. Užsakymai, Gamyba ir Išlaidos per 6 mėnesius
            LocalDateTime startData = LocalDateTime.now().minusMonths(6);
            LocalDateTime current = startData;

            while (current.isBefore(LocalDateTime.now())) {
                // Generuojame užsakymus šiai dienai (0-3 užsakymai per dieną)
                int ordersToday = random.nextInt(4);
                for (int i = 0; i < ordersToday; i++) {
                    User klientas = klientai.get(random.nextInt(klientai.size()));
                    OrderStatus status;
                    if (current.isBefore(LocalDateTime.now().minusWeeks(2))) {
                        status = random.nextDouble() < 0.9 ? OrderStatus.UZBAIGTAS : OrderStatus.ATSAUKTAS;
                    } else if (current.isBefore(LocalDateTime.now().minusDays(3))) {
                        status = OrderStatus.PARUOSTAS;
                    } else {
                        status = OrderStatus.VYKDOMAS;
                    }

                    CustomerOrder order = new CustomerOrder(klientas, 
                            random.nextBoolean() ? OrderType.PIRKIMAS_IS_SANDELIO : OrderType.GAMYBA_PAGAL_UZSAKYMA, 
                            status, current, BigDecimal.ZERO);
                    
                    int itemsCount = 1 + random.nextInt(4);
                    BigDecimal total = BigDecimal.ZERO;
                    List<CustomerOrderItem> items = new ArrayList<>();
                    
                    for (int j = 0; j < itemsCount; j++) {
                        Product p = produktai.get(random.nextInt(produktai.size()));
                        int qty = 1 + random.nextInt(5);
                        BigDecimal price = p.getPrice();
                        CustomerOrderItem item = new CustomerOrderItem(order, p, qty, price);
                        items.add(item);
                        total = total.add(price.multiply(new BigDecimal(qty)));
                    }
                    order.setItems(items);
                    order.setTotalAmount(total);
                    customerOrderRepository.save(order);

                    // Jei užbaigtas arba paruoštas, sukuriam gamybos įrašus
                    if (status == OrderStatus.UZBAIGTAS || status == OrderStatus.PARUOSTAS) {
                        for (CustomerOrderItem item : items) {
                            productionTaskRepository.save(new ProductionTask(item, darbuotojas, klientas, 
                                    current.plusHours(1 + random.nextInt(24)), "Atlikta sėkmingai."));
                        }
                    }
                }

                // Išlaidos (vidutiniškai viena per 3 dienas)
                if (random.nextInt(3) == 0) {
                    expenseRepository.save(new Expense(
                            ExpenseCategory.values()[random.nextInt(ExpenseCategory.values().length)],
                            "Eilinės išlaidos",
                            new BigDecimal(10 + random.nextInt(200) + "." + random.nextInt(99)),
                            current.toLocalDate(),
                            buhalteris
                    ));
                }

                current = current.plusDays(1).plusHours(random.nextInt(4));
            }
        };
    }
}
