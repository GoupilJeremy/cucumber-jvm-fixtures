package fixtures.common.transformers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import fixtures.common.transformers.variables.CapitalizePreviousMonth;
import fixtures.common.transformers.variables.LocalDateCurrentMonthAndYearInContext;
import fixtures.common.transformers.variables.NumericMonth;
import fixtures.common.transformers.variables.PreviousMonth;
import fixtures.common.transformers.variables.PreviousMonthAndYear;
import fixtures.common.transformers.variables.PreviousMonthUpper;
import fixtures.common.transformers.variables.VariableFunction;
import fixtures.common.transformers.variables.YearOfPreviousMonth;

public class VariableResolverStringDecorator {

    private VariableResolverStringDecorator() {}

    public static String resolveVariables(String stringWithVariables, Map<String, String> context) {
        FluentIterable<String> fluentIterable = FluentIterable.from(Arrays.asList(stringWithVariables));
        List<Function<String, String>> functions = getFunctions(context);
        for (Function<String, String> function : functions) {
            fluentIterable = fluentIterable.transform(function);
        }
        return fluentIterable.get(0);
    }

    public static List<Function<String, String>> getFunctions(Map<String, String> context) {
        return Arrays.asList(//
                new CapitalizePreviousMonth()//
                , new PreviousMonthAndYear() //
                , new VariableFunction(context)//
                , new PreviousMonth()//
                , new YearOfPreviousMonth()//
                , new PreviousMonthUpper() //
                , new LocalDateCurrentMonthAndYearInContext(context)//
                , new NumericMonth()//
        );
    }
}
