package fixtures.common.transformers;

import cucumber.table.DataTable;

public interface IDataTableTransformer<T> {
    DataTable toDataTable(T source);
}
