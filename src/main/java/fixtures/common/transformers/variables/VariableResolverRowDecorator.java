package fixtures.common.transformers.variables;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import gherkin.formatter.model.DataTableRow;

import java.util.List;
import java.util.Map;

public class VariableResolverRowDecorator extends DataTableRow {
    private static final long serialVersionUID = 1L;

    private DataTableRow decoratedRow;

    private Map<String, String> context;

    public VariableResolverRowDecorator(DataTableRow decoratedRow, Map<String, String> context) {
        super(decoratedRow.getComments(), Lists.<String>newArrayList(), decoratedRow.getLine());
        this.decoratedRow = decoratedRow;
        this.context = context;
        getCells().addAll(resolveVariables(decoratedRow.getCells()));
    }

    @Override
    public DiffType getDiffType() {
        return decoratedRow.getDiffType();
    }

    private List<String> resolveVariables(List<String> stringsWithVariables) {
        if (stringsWithVariables == null || stringsWithVariables.isEmpty()) {
            return Lists.newArrayList();
        }

        FluentIterable<String> fluentIterable = FluentIterable.from(stringsWithVariables);
        List<Function<String, String>> functions = VariableResolverStringDecorator.getFunctions(context);
        for (Function<String, String> function : functions) {
            fluentIterable = fluentIterable.transform(function);
        }

        return Lists.newArrayList(fluentIterable);
    }
}
