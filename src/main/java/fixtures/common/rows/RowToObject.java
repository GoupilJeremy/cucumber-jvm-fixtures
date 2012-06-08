package fixtures.common.rows;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fixtures.common.RowToObjectDataSource;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

/**
 * map une ligne de tableau cucumber vers un objet.
 */
public abstract class RowToObject<T extends RowToObjectDataSource> {
    private Map<String, Integer> headers;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(RowToObject.class);

    protected List<String> row;

    private T rowToObjectDataSource;

    protected Map<String, Object> context;

    protected Object[] args;

    public RowToObject(Map<String, Integer> headers, final List<String> row, final T dataSource) {
        if (headers.isEmpty()) {
            throw new IllegalArgumentException(
                    "la map de correspondance entre le nom de la colonne et l'index est vide ");
        }
        Set<Integer> valuesSet = new HashSet<Integer>();
        valuesSet.addAll(headers.values());
        for (Integer integer : valuesSet) {
            if (row.size() - 1 < integer) {
                throw new IllegalArgumentException(
                        "la map de headers fait référence à un index trop grand (" + integer +
                                ") alors que le nombre de colonne de la ligne est de " + row.size());
            }
        }
        this.headers = headers;
        this.row = row;
        this.rowToObjectDataSource = dataSource;
    }

    protected abstract void mapRowToObject();

    protected abstract Object execute();

    private String getValue(final List<String> row, final String column, final Map<String, Integer> headers,
            String defaultValue) {
        String value = defaultValue;
        Integer index = MapUtils.getInteger(headers, column);
        if (index != null) {
            String v = row.get(index);
            if (!StringUtils.isBlank(v)) {
                value = v;
            }
        } else {
            LOGGER.debug("nom de colonne non présent dans le step:'" + column + "'");
        }
        return value;
    }

    protected String getValue(String... columns) {
        for (String column : columns) {
            String value = StringUtils.trim(getValue(row, column, headers, StringUtils.EMPTY));
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return StringUtils.EMPTY;
    }

    protected String getValueForColumn(String column, String defaultValue) {
        return StringUtils.trim(getValue(row, column, headers, defaultValue));
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(final Map<String, Object> context) {
        this.context = context;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(final Object[] args) {
        this.args = args;
    }

    public T getRowToObjectDataSource() {
        return rowToObjectDataSource;
    }
}
