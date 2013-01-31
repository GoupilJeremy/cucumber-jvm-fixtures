package fixtures.common.database.utils;

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
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

public class DataBaseColumn {
    public static final String DATE_PATTERN = "dd/MM/yyyy";

    public static final String TRUE_VALUE = "oui";

    public static final String FALSE_VALUE = "non";

    public static final String TRUE_DATABASE_VALUE = "1";

    private DataTable table;

    private List<List<String>> listFiltered;

    private List<String> headers;

    public DataBaseColumn(final DataTable table, final List<Map<String, Object>> query,
            Class<? extends IBaseColumnToTable> baseColumnToTable) {
        this.table = table;
        final BaseColumnWrapper wrapper = new BaseColumnWrapper(baseColumnToTable);
        headers = this.table.raw().get(0);
        final Collection<List<String>> filteredTable =

                Collections2.transform(query, new Function<Map<String, Object>, List<String>>() {
                    @Override
                    public List<String> apply(@Nullable final Map<String, Object> input) {
                        final Map<String, String> line = replaceKeysFromDatabase(input, wrapper);
                        return reorderLine(headers, line);
                    }
                });

        listFiltered = new ArrayList<List<String>>();
        listFiltered.addAll(filteredTable);
    }

    public void compare() {
        List<List<String>> listToCompare = new ArrayList<List<String>>();
        listToCompare.add(headers);
        listToCompare.addAll(listFiltered);
        table.diff(listToCompare);
    }

    /**
     * tri ascendant par défaut
     */
    public DataBaseColumn sortBy(String column) {
        return sortBy(column, true);
    }

    public DataBaseColumn sortBy(String column, boolean asc) {
        int index = headers.indexOf(column);
        if (index < 0) {
            throw new IllegalArgumentException("Colonne [" + column + "] inexistante pour le tri");
        }
        Collections.sort(listFiltered, new BaseColumnComparator(index, asc));
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

    private Map<String, String> replaceKeysFromDatabase(final Map<String, Object> input,
            final BaseColumnWrapper wrapper) {
        Map<String, String> result = new HashMap<String, String>();
        Map<String, Object> transformedLine = filterKeysFromDatabase(input, wrapper.getBaseColumnNames());

        for (Map.Entry<String, Object> entry : transformedLine.entrySet()) {
            //je remplace la clé en base par la clé du tableau
            Object content = entry.getValue();
            IBaseColumnToTable columnEnum = getEnum(entry.getKey(), wrapper.getBaseColumnToTable());
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

    private Map<String, Object> filterKeysFromDatabase(final Map<String, Object> input,
            final Collection<String> keysInDatabase) {
        return Maps.filterKeys(input, new Predicate<String>() {
            @Override
            public boolean apply(@Nullable final String key) {
                return keysInDatabase.contains(key);
            }
        });
    }

    private class BaseColumnComparator implements Comparator<List<String>> {
        private int index;

        private int order = 1;

        public BaseColumnComparator(final int index, final boolean asc) {
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
