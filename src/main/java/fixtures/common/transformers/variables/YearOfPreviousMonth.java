package fixtures.common.transformers.variables;

import java.util.Locale;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import org.joda.time.MutableDateTime;

public class YearOfPreviousMonth implements Function<String, String> {
    private static final String YEAR_OF_PREVIOUS_MONTH_VARIABLE_NAME = "\\$\\{anneeDuMoisPrecedent\\}";

    private MutableDateTime previousMonth = MutableDateTime.now().monthOfYear().add(-1);

    @Override
    public String apply(final String input) {
        String strToProceed = Strings.nullToEmpty(input);
        return strToProceed
                .replaceAll(YEAR_OF_PREVIOUS_MONTH_VARIABLE_NAME, previousMonth.year().getAsText(Locale.FRENCH));
    }
}
