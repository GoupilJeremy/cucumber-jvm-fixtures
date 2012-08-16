package fixtures.common.transformers.variables;

import com.google.common.base.Strings;

public class VariableException extends RuntimeException {
    public VariableException(final String cause) {
        super("Cette variable ne peut être calculé : " + Strings.nullToEmpty(cause));
    }
}
