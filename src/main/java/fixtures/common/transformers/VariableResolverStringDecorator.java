package fixtures.common.transformers;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import fixtures.common.transformers.variables.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class VariableResolverStringDecorator {
    private VariableResolverStringDecorator() {
    }

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
                , new Now()
        );
    }
}
