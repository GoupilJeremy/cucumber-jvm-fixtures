package fixtures.common.database;

import cucumber.api.DataTable;
import fixtures.common.transformers.AbstractDataTableBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatatableFromDataBaseComparator extends AbstractDataTableBuilder<Map<String,? extends Comparable>> {

    private DatatableFromDataBaseComparator(final DataTable table, final List<Map<String, ? extends Comparable>> queryResultFromDatabase) {
        super(table,queryResultFromDatabase);
    }

    public static DatatableFromDataBaseComparator from(final DataTable table, final List<Map<String,  ? extends Comparable>> query){
        DatatableFromDataBaseComparator datatableFromDataBaseComparator = new DatatableFromDataBaseComparator(table, query);
        return datatableFromDataBaseComparator;
    }


    @Override
    protected Map<String, String> toMap(final Map<String, ? extends Comparable> databaseRow) {

        Map<String, String> result = new HashMap<String, String>();
        for (Map.Entry<String, ? extends Comparable> entry : databaseRow.entrySet()) {
            result.put(entry.getKey(),entry.getValue().toString());
        }

            return result;
    }


}
