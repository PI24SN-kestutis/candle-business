package lt.viko.eif.kskrebe.candlebusiness.model;

/**
 * Kliento uzsakymo tipas.
 */
public enum OrderType {
    /** Klientas perka jau pagaminta produkta. */
    PIRKIMAS_IS_SANDELIO,

    /** Klientas uzsako produkta pagaminti individualiai. */
    GAMYBA_PAGAL_UZSAKYMA
}
