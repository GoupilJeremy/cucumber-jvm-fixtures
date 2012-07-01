package fixtures.common.transformers.variables;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;

public class CapitalizePreviousMonth implements Function<String, String> {
    private static final String PREVIOUS_MONTH_VARIABLE_NAME_UPPER = "\\$\\{majMoisPrecedent\\}";

    private PreviousMonth previousMonth = new PreviousMonth();

    @Override
    public String apply(final String input) {
        String strToProceed = Strings.nullToEmpty(input);
        return strToProceed.replaceAll(PREVIOUS_MONTH_VARIABLE_NAME_UPPER,
                StringUtils.capitalize(previousMonth.getPreviousMonth()));
    }
}
