package fixtures.common.transformers.variables;

import javax.annotation.Nullable;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Strings;

public class VariableFunction implements Function<String, String> {
    private Map<String, String> context;

    public VariableFunction(final Map<String, String> context) {
        this.context = context;
    }

    @Override
    public String apply(@Nullable final String input) {
        String strToProceed = Strings.nullToEmpty(input);
        if (context == null) {
            return strToProceed;
        }
        return Objects.firstNonNull(context.get(input), strToProceed);
    }
}
