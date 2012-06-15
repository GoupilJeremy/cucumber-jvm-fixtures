package fixtures.common.datatable;

import java.util.List;
import java.util.Locale;

import cucumber.table.DataTable;

public class DatatableUtils {

    public static DataTable getDatatable(List<String> headers, List<List<String>> values){
        return DataTable.create(values, Locale.getDefault(),headers.toArray(new String[headers.size()]));
    }
}
