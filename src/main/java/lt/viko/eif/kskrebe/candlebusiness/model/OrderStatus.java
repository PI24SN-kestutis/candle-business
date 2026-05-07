package lt.viko.eif.kskrebe.candlebusiness.model;

/**
 * Kliento uzsakymo busena.
 */
public enum OrderStatus {
    /** Uzsakymas sukurtas ir lauke apdorojimo. */
    NAUJAS,

    /** Uzsakymas patvirtintas ir vykdomas. */
    VYKDOMAS,

    /** Uzsakymas pagamintas arba paruostas atsiemimui. */
    PARUOSTAS,

    /** Uzsakymas uzbaigtas. */
    UZBAIGTAS,

    /** Uzsakymas atsauktas. */
    ATSAUKTAS
}
