package fixtures.common.transformers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import cucumber.runtime.converters.LocalizedXStreams;
import cucumber.table.DataTable;
import cucumber.table.TableConverter;
import gherkin.formatter.model.DataTableRow;
import gherkin.formatter.model.Row;

public class DataTableVariableResolverDecorator extends DataTable {
    public DataTableVariableResolverDecorator(DataTable dataTable) {
        this(dataTable, Maps.<String, String>newHashMap());
    }

    public DataTableVariableResolverDecorator(DataTable dataTable, Map<String, String> context) {
        super(new ArrayList<DataTableRow>(), new TableConverter(getXStream(), null));
        getGherkinRows().addAll(decorate(dataTable.getGherkinRows(), context));
        populateRaw();
    }

    private static LocalizedXStreams.LocalizedXStream getXStream() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return new LocalizedXStreams(classLoader).get(Locale.getDefault());
    }

    private void populateRaw() {
        for (Row row : getGherkinRows()) {
            List<String> list = new ArrayList<String>();
            list.addAll(row.getCells());
            raw().add(list);
        }
    }

    protected List<DataTableRow> decorate(List<DataTableRow> rows, final Map<String, String> context) {
        List<DataTableRow> dataTableRows = new ArrayList<DataTableRow>();
        dataTableRows.addAll(Collections2.transform(rows, new Function<DataTableRow, DataTableRow>() {
            @Override
            public DataTableRow apply(final DataTableRow dataTableRow) {
                return new VariableResolverRowDecorator(dataTableRow, context);
            }
        }));
        return dataTableRows;
    }
}
