package fixtures.common.database;

import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import cucumber.api.DataTable;
import fixtures.common.transformers.AbstractDataTableTransformer;
import gherkin.formatter.model.DataTableRow;

public class DatatableFromDatabaseTransformer extends AbstractDataTableTransformer<Map<String,Object>> {
    public DatatableFromDatabaseTransformer(final DataTable dataTableFromFeatureFileToCompare) {
        super(dataTableFromFeatureFileToCompare);
    }

    @Override
    protected Map<String, String> apply(final Map<String, Object> object) {
        return null;
    }
}
