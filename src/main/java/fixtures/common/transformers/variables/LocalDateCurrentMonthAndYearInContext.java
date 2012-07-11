package fixtures.common.transformers.variables;

import javax.annotation.Nullable;
import java.util.Map;

import com.google.common.base.Function;

public class LocalDateCurrentMonthAndYearInContext implements Function<String, String> {
    public static final String CURRENT_MONTH = "currentMonth";

    public static final String CURRENT_YEAR = "currentYear";

    private static final String LOCAL_DATE_VARIABLE = "yyyy-mm-";

    private String currentMonth;

    private String currentYear;

    public LocalDateCurrentMonthAndYearInContext(Map<String, String> context) {
        if (context != null) {
            currentMonth = context.get(CURRENT_MONTH);
            currentYear = context.get(CURRENT_YEAR);
        }
    }

    @Override
    public String apply(@Nullable final String input) {
        if (currentMonth == null || currentYear == null || input == null || !input.startsWith(LOCAL_DATE_VARIABLE)) {
            return input;
        }
        String[] strings = input.split("-");
        String day = strings[strings.length - 1];
        return currentYear + "-" + currentMonth + "-" + day;
    }
}
