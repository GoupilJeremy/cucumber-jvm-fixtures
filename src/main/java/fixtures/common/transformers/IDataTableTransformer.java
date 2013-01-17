package fixtures.common.transformers;

import cucumber.api.DataTable;

public interface IDataTableTransformer<T> {
    DataTable toDataTable(T source);
}
