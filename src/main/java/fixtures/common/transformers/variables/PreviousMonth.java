package fixtures.common.transformers.variables;

import java.util.Locale;

import com.google.common.base.Function;
import org.joda.time.MutableDateTime;

public class PreviousMonth implements Function<String, String> {
    private static final String PREVIOUS_MONTH_VARIABLE_NAME = "\\$\\{moisPrecedent\\}";

    private MutableDateTime previousMonth = MutableDateTime.now().monthOfYear().add(-1);

    @Override
    public String apply(final String input) {
        return input.replaceAll(PREVIOUS_MONTH_VARIABLE_NAME, getPreviousMonth());
    }

    public String getPreviousMonth() {
        return previousMonth.monthOfYear().getAsText(Locale.FRENCH);
    }
}
