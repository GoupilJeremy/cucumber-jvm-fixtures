package fixtures.common.database;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cucumber.api.DataTable;
import fixtures.common.transformers.AbstractDataTableBuilder;
import fixtures.common.transformers.LineComparator;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import javax.annotation.Nullable;
import java.sql.Types;
import java.util.*;

public class DatatableFromDataBaseComparator extends AbstractDataTableBuilder<Map<String,? extends Comparable>> {
    private static final String DATE_PATTERN = "dd/MM/yyyy";

    private static final String TRUE_VALUE = "oui";

    private static final String FALSE_VALUE = "non";

    private static final String TRUE_DATABASE_VALUE = "1";


    private List<List<String>> listFiltered;

    private  TableFromDatabaseWrapper wrapper;

    private DatatableFromDataBaseComparator(final DataTable table, final List<Map<String, ? extends Comparable>> queryResultFromDatabase,
            Class<? extends IBaseColumnToTable> baseColumnToTable) {
        super(table,queryResultFromDatabase);
        wrapper = new TableFromDatabaseWrapper(baseColumnToTable);

        final Collection<List<String>> filteredTable =

                Collections2.transform(queryResultFromDatabase, new Function<Map<String, ? extends Comparable>, List<String>>() {
                    @Override
                    public List<String> apply(final Map<String, ? extends Comparable> databaseRow) {
                        if(databaseRow!=null){
                        final Map<String, String> line = DatatableFromDataBaseComparator.this.apply(databaseRow);
                        return reorderLine(headersAsCells, line);
                        }else {
                            return Lists.newArrayList();
                        }
                    }
                });

        listFiltered = new ArrayList<List<String>>();
        listFiltered.addAll(filteredTable);
    }

    public static DatatableFromDataBaseComparator from(final DataTable table, final List<Map<String,  ? extends Comparable>> query,
                Class<? extends IBaseColumnToTable> baseColumnToTable){
        DatatableFromDataBaseComparator datatableFromDataBaseComparator = new DatatableFromDataBaseComparator(table, query, baseColumnToTable);
        datatableFromDataBaseComparator.compareWith(new RowComparator());
        return datatableFromDataBaseComparator;
    }



    private IBaseColumnToTable getEnum(final String databaseColumnName, final List<IBaseColumnToTable> columnToTables) {
        return Iterables.find(columnToTables, new Predicate<IBaseColumnToTable>() {
            @Override
            public boolean apply(@Nullable final IBaseColumnToTable input) {
                return input.getBaseColumnName().equals(databaseColumnName);
            }
        });
    }



    private Map<String,? extends Comparable> filterKeysFromDatabase(final Map<String,? extends Comparable> input,
                                                           final Collection<String> keysInDatabase) {
        return Maps.filterKeys(input, new Predicate<String>() {
            @Override
            public boolean apply(@Nullable final String key) {
                return keysInDatabase.contains(key);
            }
        });
    }



    @Override
    protected Map<String, String> apply(final Map<String, ? extends Comparable> databaseRow) {
        Map<String, String> result = new HashMap<String, String>();
            Map<String,  ? extends Comparable> transformedLine = filterKeysFromDatabase(databaseRow, wrapper.getBaseColumnNames());

            for (Map.Entry<String,  ? extends Comparable> cell : transformedLine.entrySet()) {
                //je remplace la clé en base par la clé du tableau
                Object content = cell.getValue();
                IBaseColumnToTable columnEnum = getEnum(cell.getKey(), wrapper.getBaseColumnToTable());
                String finalContent = (content == null) ? StringUtils.EMPTY : content.toString();
                if (columnEnum.getColumnType() == Types.BOOLEAN) {
                    finalContent = finalContent.equals(TRUE_DATABASE_VALUE) ? TRUE_VALUE : FALSE_VALUE;
                } else if (columnEnum.getColumnType() == Types.DATE && content != null) {
                    Date date = (Date) content;
                    finalContent = new LocalDate(date.getTime()).toString(DATE_PATTERN);
                }
                result.put(columnEnum.getDatatableColumnName(), finalContent);
            }

            return result;
    }

    private static class RowComparator extends LineComparator<Map<String,? extends Comparable>> {

        @Override
        protected Comparable getValue(Map<String, ? extends Comparable> row,String column) {
            return  row.get(column);
        }
    }


}
