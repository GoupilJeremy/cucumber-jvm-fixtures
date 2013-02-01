package fixtures.common.database;

import javax.annotation.Nullable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cucumber.api.DataTable;
import cucumber.runtime.table.TableDiffException;
import fixtures.common.transformers.AbstractDataTableBuilder;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

public class DatatableFromDataBaseComparator extends AbstractDataTableBuilder<Map<String,Object>> {
    private static final String DATE_PATTERN = "dd/MM/yyyy";

    private static final String TRUE_VALUE = "oui";

    private static final String FALSE_VALUE = "non";

    private static final String TRUE_DATABASE_VALUE = "1";

    private DataTable table;

    private List<List<String>> listFiltered;

    private  TableFromDatabaseWrapper wrapper;

    private DatatableFromDataBaseComparator(final DataTable table, final List<Map<String, Object>> queryResultFromDatabase,
            Class<? extends IBaseColumnToTable> baseColumnToTable) {
        super(table,queryResultFromDatabase);
        this.table = table;
        wrapper = new TableFromDatabaseWrapper(baseColumnToTable);

        final Collection<List<String>> filteredTable =

                Collections2.transform(queryResultFromDatabase, new Function<Map<String, Object>, List<String>>() {
                    @Override
                    public List<String> apply(final Map<String, Object> databaseRow) {
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

    public static DatatableFromDataBaseComparator from(final DataTable table, final List<Map<String, Object>> query,
                Class<? extends IBaseColumnToTable> baseColumnToTable){
        return new DatatableFromDataBaseComparator(table,query,baseColumnToTable);
    }

    /**
     * @throws TableDiffException
     */
    public void compare()  {
        List<List<String>> listToCompare = new ArrayList<List<String>>();
        listToCompare.add(headersAsCells);
        listToCompare.addAll(listFiltered);
        table.diff(listToCompare);
    }

    /**
     * tri ascendant par défaut
     */
    public DatatableFromDataBaseComparator sortBy(String column) {
        return sortBy(column, true);
    }

    public DatatableFromDataBaseComparator sortBy(String column, boolean asc) {
        int index = headersAsCells.indexOf(column);
        if (index < 0) {
            throw new IllegalArgumentException("Colonne [" + column + "] inexistante pour le tri");
        }
        Collections.sort(listFiltered, new RowComparator(index, asc));
        return this;
    }

    private IBaseColumnToTable getEnum(final String databaseColumnName, final List<IBaseColumnToTable> columnToTables) {
        return Iterables.find(columnToTables, new Predicate<IBaseColumnToTable>() {
            @Override
            public boolean apply(@Nullable final IBaseColumnToTable input) {
                return input.getBaseColumnName().equals(databaseColumnName);
            }
        });
    }

    private List<String> reorderLine(final List<String> headers, final Map<String, String> line) {
        List<String> reorderedLine = Lists.newArrayList();
        for (String header : headers) {
            reorderedLine.add(line.get(header));
        }
        return reorderedLine;
    }

    private Map<String, Object> filterKeysFromDatabase(final Map<String, Object> input,
            final Collection<String> keysInDatabase) {
        return Maps.filterKeys(input, new Predicate<String>() {
            @Override
            public boolean apply(@Nullable final String key) {
                return keysInDatabase.contains(key);
            }
        });
    }

    @Override
    protected Map<String, String> apply(final Map<String, Object> databaseRow) {
        Map<String, String> result = new HashMap<String, String>();
            Map<String, Object> transformedLine = filterKeysFromDatabase(databaseRow, wrapper.getBaseColumnNames());

            for (Map.Entry<String, Object> cell : transformedLine.entrySet()) {
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

    private class RowComparator implements Comparator<List<String>> {
        private int index;

        private int order = 1;

        public RowComparator(final int index, final boolean asc) {
            this.index = index;
            if (!asc) {
                order = -1;
            }
        }

        @Override
        public int compare(final List<String> list01, final List<String> list02) {
            return (list01.get(index).compareTo(list02.get(index))) * order;
        }
    }


}
