package fixtures.common.transformers;

import cucumber.api.DataTable;

public interface IDataTableBuilder<T> {
//    static IDataTableBuilder<T> from(DataTable datatableToCompare,T source);
    DataTable toDataTable();
}
