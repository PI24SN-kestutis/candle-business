package lt.viko.eif.kskrebe.candlebusiness.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Konvertuoja naudotojo roles ir leidzia perskaityti senus duomenu bazes irasus.
 */
@Converter
public class UserRoleConverter implements AttributeConverter<UserRole, String> {

    private static final String LEGACY_ACCOUNTING_ROLE = "FINANS" + "ININKAS";

    @Override
    public String convertToDatabaseColumn(UserRole role) {
        return role == null ? null : role.name();
    }

    @Override
    public UserRole convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        if (LEGACY_ACCOUNTING_ROLE.equals(value)) {
            return UserRole.BUHALTERIS;
        }
        return UserRole.valueOf(value);
    }
}
