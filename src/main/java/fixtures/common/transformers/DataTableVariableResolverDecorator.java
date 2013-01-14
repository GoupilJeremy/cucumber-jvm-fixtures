package fixtures.common.transformers;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import cucumber.api.DataTable;
import cucumber.runtime.table.TableConverter;
import cucumber.runtime.xstream.LocalizedXStreams;
import gherkin.formatter.model.DataTableRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DataTableVariableResolverDecorator extends DataTable {
    private DataTable dataTableToDecorate;

    private Map<String, String> context;

    public DataTableVariableResolverDecorator(DataTable dataTable) {
        this(dataTable, Maps.<String, String>newHashMap());
    }

    public DataTableVariableResolverDecorator(DataTable dataTable, Map<String, String> context) {
        super(new ArrayList<DataTableRow>(), new TableConverter(getXStream(), null));
        Preconditions.checkArgument(dataTable != null, "la datatable ne peut ëtre null");

        this.dataTableToDecorate = dataTable;
        this.context = context;
    }

    private static LocalizedXStreams.LocalizedXStream getXStream() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return new LocalizedXStreams(classLoader).get(Locale.getDefault());
    }

    /**
     * En attendant de trouver mieux  : pour la compatibilité 1.0.10
     *
     * @return DataTable
     */
    public DataTable getDataTableDecorated() {
        return new DataTable(decorate(), new TableConverter(getXStream(), null));
    }

    protected List<DataTableRow> decorate() {
        List<DataTableRow> dataTableRows = new ArrayList<DataTableRow>();
        dataTableRows
                .addAll(Collections2.transform(dataTableToDecorate.getGherkinRows(), new VariableFunction(context)));
        return dataTableRows;
    }

    private static class VariableFunction implements Function<DataTableRow, DataTableRow> {
        private Map<String, String> innerContext;

        private VariableFunction(Map<String, String> innerContext) {
            this.innerContext = innerContext;
        }

        @Override
        public DataTableRow apply(final DataTableRow dataTableRow) {
            return new VariableResolverRowDecorator(dataTableRow, innerContext);
        }
    }
}
