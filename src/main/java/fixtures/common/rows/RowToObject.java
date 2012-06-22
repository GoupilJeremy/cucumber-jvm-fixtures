package fixtures.common.rows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;
import fixtures.common.RowToObjectDataSource;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * map une ligne de tableau cucumber vers un objet.
 */
public abstract class RowToObject<D extends RowToObjectDataSource,Res> {
    protected Map<String, Integer> headers;

    private static final Logger LOGGER = LoggerFactory.getLogger(RowToObject.class);

    protected List<String> row;

    private D rowToObjectDataSource;

    protected Map<String, Object> context;

    protected Object[] args;

    public RowToObject(Map<String, Integer> headers, final List<String> row, final D dataSource) {
        if ( headers == null  || headers.isEmpty()) {
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

    protected abstract Res mapRowToObject();


    protected static String getValue(final List<String> row, final String column, final Map<String, Integer> headers,
            String defaultValue) {
        String value = defaultValue;
        Integer index = MapUtils.getInteger(headers, column);
        if (index != null) {
            String v = row.get(index);
            if (!StringUtils.isBlank(v)) {
                value = v;
            }
        } else {
            LOGGER.debug("column name no present into step:'" + column + "'");
        }
        return value;
    }


    protected static  <E> E getValueAsObject(final List<String> row, final String column, final Map<String, Integer> headers,
             String defaultValue,Map<String,Class> columnNamesAndTypes) {
         String value = getValue(row,column,headers,defaultValue);
        Class clazz = columnNamesAndTypes.get(column);
        if(clazz == null){
              throw new IllegalArgumentException("Column "+column+" type not defined in columnNamesAndTypes");
        }
        try {

            Constructor constructor = clazz.getConstructor(String.class);

        return (E) constructor.newInstance(value);
        } catch (NoSuchMethodException e) {
            LOGGER.error(e.getMessage());
        } catch (InvocationTargetException e) {
            LOGGER.error(e.getMessage());
        } catch (InstantiationException e) {
            LOGGER.error(e.getMessage());
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage());

        }
              throw new IllegalArgumentException("value" +value+" of column "+column+" cannot be cast ");

     }

    public String getValue(String column) {

            String value = StringUtils.trim(getValue(row, column, headers, StringUtils.EMPTY));
            if (StringUtils.isNotBlank(value)) {
                return value;
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

    public D getRowToObjectDataSource() {
        return rowToObjectDataSource;
    }
}
