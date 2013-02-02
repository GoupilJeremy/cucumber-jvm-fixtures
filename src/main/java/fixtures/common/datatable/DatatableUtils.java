package fixtures.common.datatable;

import com.google.common.base.Preconditions;
import cucumber.api.DataTable;

import java.util.List;
import java.util.Locale;

public class DatatableUtils {
    private DatatableUtils() {
    }

    public static DataTable getDatatable(List<String> headers, List<List<String>> values) {
        Preconditions.checkArgument(headers != null, "la liste de header ne peut être null");
        Preconditions.checkArgument(values != null, "values ne peut être null");
        return DataTable.create(values, Locale.getDefault(), headers.toArray(new String[headers.size()]));
    }
}
