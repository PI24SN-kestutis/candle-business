package lt.viko.eif.kskrebe.candlebusiness.dto;

/**
 * Vadovo sutarties forma.
 */
public class ContractForm {

    private Long id;
    private String contractNumber;
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
