package fixtures.common.database.utils;

import java.util.List;
import java.util.Map;

import cucumber.api.DataTable;

public class DataBaseColumnToTableUtils {


    public static DataBaseColumn newBaseColumn(final DataTable table, final List<Map<String, Object>> query,
            Class<? extends IBaseColumnToTable> baseColumnToTable) {
        return new DataBaseColumn(table, query, baseColumnToTable);
    }

    public static void compareBaseToTable(final DataTable table, final List<Map<String, Object>> resultSetAsMap,
            Class<? extends IBaseColumnToTable> mapColumnsFromDataBaseToDatatableEnum) {
        new DataBaseColumn(table, resultSetAsMap, mapColumnsFromDataBaseToDatatableEnum).compare();
    }


}
