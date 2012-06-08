package fixtures.common.transformers;

import javax.annotation.Nullable;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Objects;

public class VariableFunction implements Function<String, String> {
    private Map<String, String> context;

    public VariableFunction(final Map<String, String> context) {
        this.context = context;
    }

    @Override
    public String apply(@Nullable final String input) {
        return Objects.firstNonNull(context.get(input), input);
    }
}
