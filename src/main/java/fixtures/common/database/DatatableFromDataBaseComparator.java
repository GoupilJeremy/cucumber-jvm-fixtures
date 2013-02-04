package fixtures.common.database;

import com.google.common.collect.Maps;
import cucumber.api.DataTable;
import fixtures.common.transformers.AbstractDataTableBuilder;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatatableFromDataBaseComparator extends AbstractDataTableBuilder<Map<String, ? extends Comparable>> {
    private static final String DATE_PATTERN = "dd/MM/yyyy";

    private static final String TRUE_VALUE = "oui";

    private static final String FALSE_VALUE = "non";

    private static final String TRUE_DATABASE_VALUE = "1";

    private DatatableFromDataBaseComparator(final DataTable table,
            final List<Map<String, ? extends Comparable>> queryResultFromDatabase) {
        super(table, queryResultFromDatabase);
    }

    public static DatatableFromDataBaseComparator from(final DataTable table,
            final List<Map<String, ? extends Comparable>> query) {
        DatatableFromDataBaseComparator datatableFromDataBaseComparator = new DatatableFromDataBaseComparator(table,
                query);
        return datatableFromDataBaseComparator;
    }

    @Override
    protected Map<String, Object> toMap(final Map<String, ? extends Comparable> databaseRow) {

        Map<String, Object> result = Maps.newHashMap();
        for (Map.Entry<String, ? extends Comparable> entry : databaseRow.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @Override
    protected String replaceValue(final String type, Object valueFromDataSource) {
        String replacedValue = StringUtils.EMPTY;
        Integer intType = Integer.parseInt(type);
        if (Types.BOOLEAN == intType) {
            replacedValue = valueFromDataSource.equals(TRUE_DATABASE_VALUE) ? TRUE_VALUE : FALSE_VALUE;
        } else if (Types.DATE == intType && valueFromDataSource != null) {
            Date date = (Date) valueFromDataSource;
            replacedValue = new LocalDate(date.getTime()).toString(DATE_PATTERN);
        }else{
            replacedValue = valueFromDataSource.toString();
        }
       return replacedValue;
    }
}
