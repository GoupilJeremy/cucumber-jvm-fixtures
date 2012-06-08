package fixtures.common.transformers.variables;

import java.util.Locale;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import org.apache.commons.lang.StringUtils;
import org.joda.time.MutableDateTime;

public class PreviousMonthAndYear implements Function<String, String> {
    private static final String PREVIOUS_MONTH_VARIABLE_NAME = "\\$\\{moisPrecedentEtAnnee\\}";

    private static final String SPACE = " ";

    private MutableDateTime previousMonth = MutableDateTime.now().monthOfYear().add(-1);

    @Override
    public String apply(final String input) {
        return input.replaceAll(PREVIOUS_MONTH_VARIABLE_NAME, Joiner.on(SPACE)
                .join(StringUtils.capitalize(previousMonth.monthOfYear().getAsText(Locale.FRENCH)),
                        previousMonth.year().getAsText(Locale.FRENCH)));
    }
}

