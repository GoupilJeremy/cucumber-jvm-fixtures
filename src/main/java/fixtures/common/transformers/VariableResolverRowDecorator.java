package fixtures.common.transformers;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import fixtures.common.transformers.variables.CapitalizePreviousMonth;
import fixtures.common.transformers.variables.PreviousMonth;
import fixtures.common.transformers.variables.PreviousMonthAndYear;
import fixtures.common.transformers.variables.PreviousMonthUpper;
import fixtures.common.transformers.variables.YearOfPreviousMonth;
import gherkin.formatter.model.DataTableRow;

public class VariableResolverRowDecorator extends DataTableRow{
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

    protected List<String> resolveVariables(List<String> stringsWithVariables) {
        List<String> internalStringsWithVariables = checkNotNull(stringsWithVariables, Lists.newArrayList());
        return Lists.newArrayList(FluentIterable  //
                .from(internalStringsWithVariables) //
                .transform(new CapitalizePreviousMonth()) //
                .transform(new PreviousMonth()) //
                .transform(new PreviousMonthUpper()) //
                .transform(new PreviousMonthAndYear()) //
                .transform(new YearOfPreviousMonth())
                .transform(new VariableFunction(context)));
    }

}
