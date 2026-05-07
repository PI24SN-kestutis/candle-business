package lt.viko.eif.kskrebe.candlebusiness.service;

import lt.viko.eif.kskrebe.candlebusiness.dto.AddressForm;
import lt.viko.eif.kskrebe.candlebusiness.dto.ContractForm;
import lt.viko.eif.kskrebe.candlebusiness.dto.IngredientForm;
import lt.viko.eif.kskrebe.candlebusiness.dto.ProductForm;
import lt.viko.eif.kskrebe.candlebusiness.dto.SupplierForm;
import lt.viko.eif.kskrebe.candlebusiness.model.Address;
import lt.viko.eif.kskrebe.candlebusiness.model.Contract;
import lt.viko.eif.kskrebe.candlebusiness.model.Ingredient;
import lt.viko.eif.kskrebe.candlebusiness.model.Product;
import lt.viko.eif.kskrebe.candlebusiness.model.Supplier;
import lt.viko.eif.kskrebe.candlebusiness.repository.AddressRepository;
import lt.viko.eif.kskrebe.candlebusiness.repository.ContractRepository;
import lt.viko.eif.kskrebe.candlebusiness.repository.CustomerOrderItemRepository;
import lt.viko.eif.kskrebe.candlebusiness.repository.IngredientRepository;
import lt.viko.eif.kskrebe.candlebusiness.repository.ProductRepository;
import lt.viko.eif.kskrebe.candlebusiness.repository.SupplierRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

/**
 * Vadovo katalogo valdymo logika.
 */
@Service
public class CatalogService {

    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;
    private final CustomerOrderItemRepository customerOrderItemRepository;
    private final SupplierRepository supplierRepository;
    private final AddressRepository addressRepository;
    private final ContractRepository contractRepository;

    public CatalogService(ProductRepository productRepository,
                          IngredientRepository ingredientRepository,
                          CustomerOrderItemRepository customerOrderItemRepository,
                          SupplierRepository supplierRepository,
                          AddressRepository addressRepository,
                          ContractRepository contractRepository) {
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
        this.customerOrderItemRepository = customerOrderItemRepository;
        this.supplierRepository = supplierRepository;
        this.addressRepository = addressRepository;
        this.contractRepository = contractRepository;
    }

    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAllWithIngredients(pageable);
    }

    @Transactional(readOnly = true)
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public List<Address> getAllAddresses() {
        return addressRepository.findAllByOrderByCountryAscStreetAscHouseNumberAsc();
    }

    @Transactional(readOnly = true)
    public List<Contract> getAllContracts() {
        return contractRepository.findAllByOrderByContractNumberAsc();
    }

    @Transactional(readOnly = true)
    public Supplier getSupplier(Long supplierId) {
        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Tiekėjas nerastas: " + supplierId));
    }

    @Transactional(readOnly = true)
    public Address getAddress(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("Adresas nerastas: " + addressId));
    }

    @Transactional(readOnly = true)
    public Contract getContract(Long contractId) {
        return contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Sutartis nerasta: " + contractId));
    }

    @Transactional(readOnly = true)
    public Product getProduct(Long productId) {
        return productRepository.findByIdWithIngredients(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produktas nerastas: " + productId));
    }

    @Transactional(readOnly = true)
    public Ingredient getIngredient(Long ingredientId) {
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IllegalArgumentException("Ingredientas nerastas: " + ingredientId));
    }

    @Transactional
    public Product saveProduct(ProductForm form) {
        validateProductForm(form);

        Product product = form.getId() == null
                ? new Product()
                : productRepository.findByIdWithIngredients(form.getId())
                .orElseThrow(() -> new IllegalArgumentException("Produktas nerastas: " + form.getId()));

        product.setName(form.getName());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setStockQuantity(form.getStockQuantity());
        product.setProductionType(form.getProductionType());
        product.setCustomMade(form.isCustomMade());
        product.setIngredients(new HashSet<>(ingredientRepository.findAllById(form.getIngredientIds())));

        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        if (!customerOrderItemRepository.findByProductId(productId).isEmpty()) {
            throw new IllegalArgumentException("Produkto trinti negalima, nes jis jau panaudotas užsakymuose");
        }
        productRepository.deleteById(productId);
    }

    @Transactional
    public Ingredient saveIngredient(IngredientForm form) {
        if (form == null) {
            throw new IllegalArgumentException("Ingrediento duomenys negali būti null");
        }
        if (form.getName() == null || form.getName().isBlank()) {
            throw new IllegalArgumentException("Ingrediento pavadinimas yra privalomas");
        }
        if (form.getType() == null) {
            throw new IllegalArgumentException("Ingrediento tipas yra privalomas");
        }

        Ingredient ingredient = form.getId() == null
                ? new Ingredient()
                : ingredientRepository.findById(form.getId())
                .orElseThrow(() -> new IllegalArgumentException("Ingredientas nerastas: " + form.getId()));

        ingredient.setName(form.getName());
        ingredient.setType(form.getType());
        ingredient.setSupplier(resolveSupplier(form.getSupplierId()));
        return ingredientRepository.save(ingredient);
    }

    @Transactional
    public void deleteIngredient(Long ingredientId) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IllegalArgumentException("Ingredientas nerastas: " + ingredientId));

        List<Product> products = productRepository.findAllWithIngredients();
        for (Product product : products) {
            if (product.getIngredients().remove(ingredient)) {
                productRepository.save(product);
            }
        }

        ingredientRepository.delete(ingredient);
    }

    @Transactional
    public Supplier saveSupplier(SupplierForm form) {
        if (form == null) {
            throw new IllegalArgumentException("Tiekėjo duomenys negali būti null");
        }
        if (form.getName() == null || form.getName().isBlank()) {
            throw new IllegalArgumentException("Tiekėjo pavadinimas yra privalomas");
        }

        String normalizedName = form.getName().trim();
        supplierRepository.findByName(normalizedName)
                .filter(existing -> !existing.getId().equals(form.getId()))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Tiekėjas tokiu pavadinimu jau egzistuoja");
                });

        Supplier supplier = form.getId() == null
                ? new Supplier()
                : getSupplier(form.getId());

        supplier.setName(normalizedName);
        supplier.setAddress(resolveAddress(form.getAddressId()));
        supplier.setContract(resolveContract(form.getContractId()));
        return supplierRepository.save(supplier);
    }

    @Transactional
    public void deleteSupplier(Long supplierId) {
        Supplier supplier = getSupplier(supplierId);
        if (ingredientRepository.existsBySupplierId(supplierId)) {
            throw new IllegalArgumentException("Tiekėjo trinti negalima, nes jis naudojamas ingredientuose");
        }
        supplierRepository.delete(supplier);
    }

    @Transactional
    public Address saveAddress(AddressForm form) {
        if (form == null) {
            throw new IllegalArgumentException("Adreso duomenys negali būti null");
        }
        if (form.getCountry() == null || form.getCountry().isBlank()) {
            throw new IllegalArgumentException("Šalis yra privaloma");
        }
        if (form.getStreet() == null || form.getStreet().isBlank()) {
            throw new IllegalArgumentException("Gatvė yra privaloma");
        }
        if (form.getHouseNumber() == null || form.getHouseNumber().isBlank()) {
            throw new IllegalArgumentException("Namo numeris yra privalomas");
        }

        Address address = form.getId() == null
                ? new Address()
                : getAddress(form.getId());

        address.setCountry(form.getCountry().trim());
        address.setStreet(form.getStreet().trim());
        address.setHouseNumber(form.getHouseNumber().trim());
        address.setRoomNumber(form.getRoomNumber() == null || form.getRoomNumber().isBlank() ? null : form.getRoomNumber().trim());
        return addressRepository.save(address);
    }

    @Transactional
    public void deleteAddress(Long addressId) {
        Address address = getAddress(addressId);
        if (supplierRepository.existsByAddressId(addressId)) {
            throw new IllegalArgumentException("Adreso trinti negalima, nes jis naudojamas tiekėjuose");
        }
        addressRepository.delete(address);
    }

    @Transactional
    public Contract saveContract(ContractForm form) {
        if (form == null) {
            throw new IllegalArgumentException("Sutarties duomenys negali būti null");
        }
        if (form.getContractNumber() == null || form.getContractNumber().isBlank()) {
            throw new IllegalArgumentException("Sutarties numeris yra privalomas");
        }
        if (form.getText() == null || form.getText().isBlank()) {
            throw new IllegalArgumentException("Sutarties tekstas yra privalomas");
        }

        String normalizedNumber = form.getContractNumber().trim();
        contractRepository.findByContractNumber(normalizedNumber)
                .filter(existing -> !existing.getId().equals(form.getId()))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Sutartis tokiu numeriu jau egzistuoja");
                });

        Contract contract = form.getId() == null
                ? new Contract()
                : getContract(form.getId());

        contract.setContractNumber(normalizedNumber);
        contract.setText(form.getText().trim());
        return contractRepository.save(contract);
    }

    @Transactional
    public void deleteContract(Long contractId) {
        Contract contract = getContract(contractId);
        if (supplierRepository.existsByContractId(contractId)) {
            throw new IllegalArgumentException("Sutarties trinti negalima, nes ji naudojama tiekėjuose");
        }
        contractRepository.delete(contract);
    }

    private void validateProductForm(ProductForm form) {
        if (form == null) {
            throw new IllegalArgumentException("Produkto duomenys negali būti null");
        }
        if (form.getName() == null || form.getName().isBlank()) {
            throw new IllegalArgumentException("Produkto pavadinimas yra privalomas");
        }
        if (form.getPrice() == null || form.getPrice().signum() < 0) {
            throw new IllegalArgumentException("Produkto kaina turi būti neneigiama");
        }
        if (form.getStockQuantity() == null || form.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Sandėlio kiekis turi būti neneigiamas");
        }
        if (form.getProductionType() == null) {
            throw new IllegalArgumentException("Gamybos tipas yra privalomas");
        }
    }

    private Supplier resolveSupplier(Long supplierId) {
        if (supplierId == null) {
            return null;
        }

        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Tiekėjas nerastas: " + supplierId));
    }

    private Address resolveAddress(Long addressId) {
        if (addressId == null) {
            return null;
        }

        return addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("Adresas nerastas: " + addressId));
    }

    private Contract resolveContract(Long contractId) {
        if (contractId == null) {
            return null;
        }

        return contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Sutartis nerasta: " + contractId));
    }
}
