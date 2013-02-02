package fixtures.common.transformers.variables;

import com.google.common.base.*;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Now implements Function<String, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Now.class);

	private final static CharMatcher CLEAN_PATTERN = CharMatcher.anyOf("+myMY").precomputed();

    public static final String DEFAULT_PATTERN = "yyyy/MM/dd";

    private static final String NOW = "now";

    private static final Pattern PATTERN = Pattern.compile("\\$\\{("+NOW+"[ +\\-_:/;\\w()]*)}");

    private static final Pattern ADD_DAYS_PATTERN = Pattern.compile("^[-+]{1}[0-9]+$");

    private static final Pattern ADD_MONTH_PATTERN = Pattern.compile("^[-+]{1}[0-9]{1,3}[mM]{1}$");

    private static final Pattern ADD_YEAR_PATTERN = Pattern.compile("^[-+]{1}[0-9]{1,2}[yY]{1}$");

	private static final Splitter SPLITTER = Splitter.on(" ");

    @Override
    public String apply(final String input) {
        String strToProceed = Strings.nullToEmpty(input);
        String datePattern = "";
        Matcher matcher = PATTERN.matcher(strToProceed);
        while (matcher.find()) {
	        String group = matcher.group(1);
	        try {
                Iterable<String> groupSplitted = SPLITTER.split(group);
                //
                checkStart(group);
                int offsetDays = getOffsetFromPattern(groupSplitted, ADD_DAYS_PATTERN);
                int offsetMonth = getOffsetFromPattern(groupSplitted, ADD_MONTH_PATTERN);
                int offsetYear = getOffsetFromPattern(groupSplitted, ADD_YEAR_PATTERN);
                datePattern = getDatePattern(group);
                DateTime now = DateTime.now().plusDays(offsetDays).plusMonths(offsetMonth).plusYears(offsetYear);
	            strToProceed = strToProceed.replace("${" + group + "}", now.toString(datePattern));
            } catch (VariableException ve) {
	            LOGGER.error("Mauvais pattern", ve);
	            strToProceed = strToProceed.replace("${" + group + "}", "$bad{" + group + "}");
            } catch (IllegalArgumentException ie) {
	            LOGGER.error("Illegal pattern pour la date {}", datePattern, ie);
	            strToProceed = strToProceed.replace("${" + group + "}", "$bad{" + group + "}");
            }
	        // mise à jour du matcher avec la nouvelle chaine de caractère
	        matcher.reset(strToProceed);
        }
        return strToProceed;
    }

    private void checkStart(final String group) {
        if (!group.startsWith(NOW + " ") && !group.equals(NOW)) {
            throw new VariableException("Début invalide pour : " + group);
        }
    }

    private String getDatePattern(final String group) {
        String patternDate = getPatternDate(group);
        if (Strings.isNullOrEmpty(patternDate)) {
            return DEFAULT_PATTERN;
        } else {
            return patternDate;
        }
    }

    private String getPatternDate(final String group) {
        if (Strings.isNullOrEmpty(group)) {
            return "";
        }
        int first = group.indexOf('(');
        int second = group.indexOf(')');
        if (first == -1 || second == -1) {
            return "";
        }
        if (second < group.length()) {
            String after = group.substring(second+1);
            if (after.contains("(") && after.contains(")")) {
                throw new VariableException("Trop de pattern pour la date (1 seul doit être précisé): " + group);
            }
        }
        return group.substring(first + 1, second);
    }

    private int getOffsetFromPattern(final Iterable<String> groupSplitted, Pattern pattern) {
        List<String> listPatternDays = Lists
                .newArrayList(Iterables.filter(groupSplitted, new PatternMatcherPredicate(pattern)));
        if (listPatternDays == null || listPatternDays.size() == 0) {
            return 0;
        } else if (listPatternDays.size() == 1) {
            // "+" non géré pour le moment (peut-être en JDK 7)
            return Integer.parseInt(CLEAN_PATTERN.removeFrom(listPatternDays.get(0).trim()));
        } else {
            throw new VariableException(
                    "Trop de pattern (1 seul doit être précisé): " + listPatternDays.toString());
        }
    }

    private static class PatternMatcherPredicate implements Predicate<String> {
        private Pattern patternToMatch;

        private PatternMatcherPredicate(final Pattern patternToMatch) {
            this.patternToMatch = patternToMatch;
        }

        @Override
        public boolean apply(final String input) {
            Matcher matcher = patternToMatch.matcher(Strings.nullToEmpty(input));
            return matcher.find();
        }
    }
}
