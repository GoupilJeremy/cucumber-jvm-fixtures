package fixtures.common.datatable;

import java.util.List;
import java.util.Locale;

import cucumber.table.DataTable;

public class DatatableUtils {

    public static DataTable getDatatable(List<Object> headers, List<List<Object>> values){
        return DataTable.create(values, Locale.getDefault(),headers.toArray(new String[headers.size()]));
    }
}
