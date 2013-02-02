package fixtures.common.transformers.variables;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import org.joda.time.MutableDateTime;

import java.util.Locale;

public class PreviousMonthUpper implements Function<String, String> {
    private static final String PREVIOUS_MONTH_VARIABLE_NAME = "\\$\\{moisPrecedentMajuscule\\}";

    private MutableDateTime previousMonth = MutableDateTime.now().monthOfYear().add(-1);

    @Override
    public String apply(final String input) {
        String strToProceed = Strings.nullToEmpty(input);
        return strToProceed.replaceAll(PREVIOUS_MONTH_VARIABLE_NAME, getPreviousMonth().toUpperCase());
    }

    public String getPreviousMonth() {
        return previousMonth.monthOfYear().getAsText(Locale.FRENCH);
    }
}
