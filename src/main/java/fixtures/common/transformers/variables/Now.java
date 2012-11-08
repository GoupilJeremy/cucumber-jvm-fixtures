package fixtures.common.transformers.variables;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Now implements Function<String, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Now.class);

    public static final String DEFAULT_PATTERN = "yyyy/MM/dd";

    private static final String NOW = "now";

    private static final Pattern PATTERN = Pattern.compile("\\$\\{(" + NOW + ".*)\\}");

    private static final Pattern ADD_DAYS_PATTERN = Pattern.compile("^[-+]{1}[0-9]+$");

    private static final Pattern DATE_PATTERN = Pattern.compile("^\\([\\w -/:]+\\)");

    private static final Splitter SPLITTER = Splitter.on(" ");

    @Override
    public String apply(final String input) {
        String strToProceed = Strings.nullToEmpty(input);
        String datePattern = "";
        Matcher matcher = PATTERN.matcher(strToProceed);
        boolean found = matcher.find();
        if (found) {
            try {
                String group = matcher.group(1);
                Iterable<String> groupSplitted = SPLITTER.split(group);
                //
                checkStart(group);
                int offset = getOffset(groupSplitted);
                datePattern = getDatePattern(group);
                DateTime now = DateTime.now().plusDays(offset);
                strToProceed = matcher.replaceAll(now.toString(datePattern));
            } catch (VariableException ve) {
                LOGGER.error("Mauvais pattern", ve);
            } catch (IllegalArgumentException ie) {
                LOGGER.error("Illegal pattern pour la date {}", datePattern, ie);
            }
        }
        return strToProceed;
    }

    private void checkStart(final String group) {
        if (!group.startsWith(NOW + " ") && !group.equals(NOW)) {
            throw new VariableException("Boom = " + group);
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
        int first = group.indexOf("(");
        int second = group.indexOf(")");
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

    private int getOffset(final Iterable<String> groupSplitted) {

        List<String> listPatternDays = Lists
                .newArrayList(Iterables.filter(groupSplitted, new PatternMatcherPredicate(ADD_DAYS_PATTERN)));
        if (listPatternDays == null || listPatternDays.size() == 0) {
            return 0;
        } else if (listPatternDays.size() == 1) {
            // "+" non géré pour le moment (peut-être en JDK 7)
            return Integer.parseInt(listPatternDays.get(0).trim().replace("+", ""));
        } else {
            throw new VariableException(
                    "Trop de pattern pour l'ajout de jour (1 seul doit être précisé): " + listPatternDays.toString());
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
