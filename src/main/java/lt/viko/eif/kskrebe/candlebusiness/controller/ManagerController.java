package lt.viko.eif.kskrebe.candlebusiness.controller;

import lt.viko.eif.kskrebe.candlebusiness.dto.AddressForm;
import lt.viko.eif.kskrebe.candlebusiness.dto.ContractForm;
import lt.viko.eif.kskrebe.candlebusiness.dto.IngredientForm;
import lt.viko.eif.kskrebe.candlebusiness.dto.ProductForm;
import lt.viko.eif.kskrebe.candlebusiness.dto.SupplierForm;
import lt.viko.eif.kskrebe.candlebusiness.model.Address;
import lt.viko.eif.kskrebe.candlebusiness.model.Contract;
import lt.viko.eif.kskrebe.candlebusiness.model.IngredientType;
import lt.viko.eif.kskrebe.candlebusiness.model.Product;
import lt.viko.eif.kskrebe.candlebusiness.model.ProductionType;
import lt.viko.eif.kskrebe.candlebusiness.model.Supplier;
import lt.viko.eif.kskrebe.candlebusiness.service.CatalogService;
import lt.viko.eif.kskrebe.candlebusiness.service.ManagerDashboardService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

/**
 * Vadovo puslapio MVC valdiklis.
 */
@Controller
public class ManagerController {

    private final ManagerDashboardService managerDashboardService;
    private final CatalogService catalogService;

    public ManagerController(ManagerDashboardService managerDashboardService,
                             CatalogService catalogService) {
        this.managerDashboardService = managerDashboardService;
        this.catalogService = catalogService;
    }

    @GetMapping("/vadovas")
    public String managerPage(@RequestParam(required = false) Long editProductId,
                              @RequestParam(required = false) Long editIngredientId,
                              @RequestParam(required = false) Long editSupplierId,
                              @RequestParam(required = false) Long editAddressId,
                              @RequestParam(required = false) Long editContractId,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        model.addAttribute("dashboard", managerDashboardService.getDashboard());

        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        model.addAttribute("products", catalogService.getAllProducts(pageable));

        model.addAttribute("priceAudits", managerDashboardService.getPriceAudits());

        model.addAttribute("ingredients", catalogService.getAllIngredients());
        model.addAttribute("suppliers", catalogService.getAllSuppliers());
        model.addAttribute("addresses", catalogService.getAllAddresses());
        model.addAttribute("contracts", catalogService.getAllContracts());
        model.addAttribute("ingredientTypes", IngredientType.values());
        model.addAttribute("productionTypes", ProductionType.values());

        if (!model.containsAttribute("productForm")) {
            model.addAttribute("productForm", editProductId == null ? new ProductForm() : toProductForm(catalogService.getProduct(editProductId)));
        }
        if (!model.containsAttribute("ingredientForm")) {
            model.addAttribute("ingredientForm", editIngredientId == null ? new IngredientForm() : toIngredientForm(editIngredientId));
        }
        if (!model.containsAttribute("supplierForm")) {
            model.addAttribute("supplierForm", editSupplierId == null ? new SupplierForm() : toSupplierForm(catalogService.getSupplier(editSupplierId)));
        }
        if (!model.containsAttribute("addressForm")) {
            model.addAttribute("addressForm", editAddressId == null ? new AddressForm() : toAddressForm(catalogService.getAddress(editAddressId)));
        }
        if (!model.containsAttribute("contractForm")) {
            model.addAttribute("contractForm", editContractId == null ? new ContractForm() : toContractForm(catalogService.getContract(editContractId)));
        }

        return "manager-page";
    }

    @PostMapping("/vadovas/produktai")
    public String saveProduct(@ModelAttribute("productForm") ProductForm productForm,
                              RedirectAttributes redirectAttributes) {
        try {
            Product savedProduct = catalogService.saveProduct(productForm);
            String message = productForm.getId() == null
                    ? "Produktas sukurtas. Numeris: " + savedProduct.getId()
                    : "Produktas atnaujintas. Numeris: " + savedProduct.getId();
            redirectAttributes.addFlashAttribute("successMessage", message);
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            redirectAttributes.addFlashAttribute("productForm", productForm);
        }
        return "redirect:/vadovas";
    }

    @PostMapping("/vadovas/produktai/{productId}/trinti")
    public String deleteProduct(@PathVariable Long productId,
                                RedirectAttributes redirectAttributes) {
        try {
            catalogService.deleteProduct(productId);
            redirectAttributes.addFlashAttribute("successMessage", "Produktas ištrintas.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/vadovas";
    }

    @PostMapping("/vadovas/ingredientai")
    public String saveIngredient(@ModelAttribute("ingredientForm") IngredientForm ingredientForm,
                                 RedirectAttributes redirectAttributes) {
        try {
            Long ingredientId = catalogService.saveIngredient(ingredientForm).getId();
            String message = ingredientForm.getId() == null
                    ? "Ingredientas sukurtas. Numeris: " + ingredientId
                    : "Ingredientas atnaujintas. Numeris: " + ingredientId;
            redirectAttributes.addFlashAttribute("successMessage", message);
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            redirectAttributes.addFlashAttribute("ingredientForm", ingredientForm);
        }
        return "redirect:/vadovas";
    }

    @PostMapping("/vadovas/ingredientai/{ingredientId}/trinti")
    public String deleteIngredient(@PathVariable Long ingredientId,
                                   RedirectAttributes redirectAttributes) {
        try {
            catalogService.deleteIngredient(ingredientId);
            redirectAttributes.addFlashAttribute("successMessage", "Ingredientas ištrintas.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/vadovas";
    }

    @PostMapping("/vadovas/tiekejai")
    public String saveSupplier(@ModelAttribute("supplierForm") SupplierForm supplierForm,
                               RedirectAttributes redirectAttributes) {
        try {
            Long supplierId = catalogService.saveSupplier(supplierForm).getId();
            String message = supplierForm.getId() == null
                    ? "Tiekėjas sukurtas. Numeris: " + supplierId
                    : "Tiekėjas atnaujintas. Numeris: " + supplierId;
            redirectAttributes.addFlashAttribute("successMessage", message);
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            redirectAttributes.addFlashAttribute("supplierForm", supplierForm);
        }
        return "redirect:/vadovas";
    }

    @PostMapping("/vadovas/tiekejai/{supplierId}/trinti")
    public String deleteSupplier(@PathVariable Long supplierId,
                                 RedirectAttributes redirectAttributes) {
        try {
            catalogService.deleteSupplier(supplierId);
            redirectAttributes.addFlashAttribute("successMessage", "Tiekėjas ištrintas.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/vadovas";
    }

    @PostMapping("/vadovas/adresai")
    public String saveAddress(@ModelAttribute("addressForm") AddressForm addressForm,
                              RedirectAttributes redirectAttributes) {
        try {
            Long addressId = catalogService.saveAddress(addressForm).getId();
            String message = addressForm.getId() == null
                    ? "Adresas sukurtas. Numeris: " + addressId
                    : "Adresas atnaujintas. Numeris: " + addressId;
            redirectAttributes.addFlashAttribute("successMessage", message);
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            redirectAttributes.addFlashAttribute("addressForm", addressForm);
        }
        return "redirect:/vadovas";
    }

    @PostMapping("/vadovas/adresai/{addressId}/trinti")
    public String deleteAddress(@PathVariable Long addressId,
                                RedirectAttributes redirectAttributes) {
        try {
            catalogService.deleteAddress(addressId);
            redirectAttributes.addFlashAttribute("successMessage", "Adresas ištrintas.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/vadovas";
    }

    @PostMapping("/vadovas/sutartys")
    public String saveContract(@ModelAttribute("contractForm") ContractForm contractForm,
                               RedirectAttributes redirectAttributes) {
        try {
            Long contractId = catalogService.saveContract(contractForm).getId();
            String message = contractForm.getId() == null
                    ? "Sutartis sukurta. Numeris: " + contractId
                    : "Sutartis atnaujinta. Numeris: " + contractId;
            redirectAttributes.addFlashAttribute("successMessage", message);
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            redirectAttributes.addFlashAttribute("contractForm", contractForm);
        }
        return "redirect:/vadovas";
    }

    @PostMapping("/vadovas/sutartys/{contractId}/trinti")
    public String deleteContract(@PathVariable Long contractId,
                                 RedirectAttributes redirectAttributes) {
        try {
            catalogService.deleteContract(contractId);
            redirectAttributes.addFlashAttribute("successMessage", "Sutartis ištrinta.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/vadovas";
    }

    private ProductForm toProductForm(Product product) {
        ProductForm form = new ProductForm();
        form.setId(product.getId());
        form.setName(product.getName());
        form.setDescription(product.getDescription());
        form.setPrice(product.getPrice());
        form.setStockQuantity(product.getStockQuantity());
        form.setProductionType(product.getProductionType());
        form.setCustomMade(product.isCustomMade());
        form.setIngredientIds(new ArrayList<>(product.getIngredients().stream().map(ingredient -> ingredient.getId()).toList()));
        return form;
    }

    private IngredientForm toIngredientForm(Long ingredientId) {
        var ingredient = catalogService.getIngredient(ingredientId);
        IngredientForm form = new IngredientForm();
        form.setId(ingredient.getId());
        form.setName(ingredient.getName());
        form.setType(ingredient.getType());
        form.setSupplierId(ingredient.getSupplier() == null ? null : ingredient.getSupplier().getId());
        return form;
    }

    private SupplierForm toSupplierForm(Supplier supplier) {
        SupplierForm form = new SupplierForm();
        form.setId(supplier.getId());
        form.setName(supplier.getName());
        form.setAddressId(supplier.getAddress() == null ? null : supplier.getAddress().getId());
        form.setContractId(supplier.getContract() == null ? null : supplier.getContract().getId());
        return form;
    }

    private AddressForm toAddressForm(Address address) {
        AddressForm form = new AddressForm();
        form.setId(address.getId());
        form.setCountry(address.getCountry());
        form.setStreet(address.getStreet());
        form.setHouseNumber(address.getHouseNumber());
        form.setRoomNumber(address.getRoomNumber());
        return form;
    }

    private ContractForm toContractForm(Contract contract) {
        ContractForm form = new ContractForm();
        form.setId(contract.getId());
        form.setContractNumber(contract.getContractNumber());
        form.setText(contract.getText());
        return form;
    }
}
