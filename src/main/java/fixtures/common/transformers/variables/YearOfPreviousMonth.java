package fixtures.common.transformers.variables;

import javax.annotation.Nullable;
import java.util.Locale;

import com.google.common.base.Function;
import org.joda.time.MutableDateTime;

public class YearOfPreviousMonth implements Function<String, String> {
    private static final String YEAR_OF_PREVIOUS_MONTH_VARIABLE_NAME = "\\$\\{anneeDuMoisPrecedent\\}";

    private MutableDateTime previousMonth = MutableDateTime.now().monthOfYear().add(-1);

    @Override
    public String apply(final String input) {
        return input.replaceAll(YEAR_OF_PREVIOUS_MONTH_VARIABLE_NAME, previousMonth.year().getAsText(Locale.FRENCH));
    }
}
