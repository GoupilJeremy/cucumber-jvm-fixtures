package fixtures.common.transformers.variables;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.primitives.Ints;
import org.joda.time.MutableDateTime;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumericMonth implements Function<String, String> {
    private static final String MONTH_PATTERN = "MM";

    private static final Pattern PATTERN = Pattern.compile("\\$\\{moisNumeric( [-+]{1}[0-9]+)?\\}");

    @Override
    public String apply(final String input) {
        String strToProceed = Strings.nullToEmpty(input);

        Matcher matcher = PATTERN.matcher(strToProceed);
        boolean found = matcher.find();
        if (found) {
            MutableDateTime now = MutableDateTime.now();
            // on récupère la valeur ( [-+]{1}[0-9]+) si elle existe
            String group = matcher.group(1);
            if (!Strings.isNullOrEmpty(group)) {
                // non géré pour le moment (peut-être en JDK 7)
                Integer offset = Ints.tryParse(group.trim().replace("+", ""));
                if (offset != null) {
                    now.addMonths(offset);
                }
            }
            strToProceed = matcher.replaceAll(now.toString(MONTH_PATTERN));
        }
        return strToProceed;
    }
}
