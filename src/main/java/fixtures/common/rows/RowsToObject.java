package fixtures.common.rows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ObjectArrays;
import cucumber.runtime.CucumberException;
import cucumber.table.DataTable;
import fixtures.common.RowToObjectDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * transforme un tableau {@see DataTable} en object.
 */
public class RowsToObject<Res> {
    private DataTable dataTable;

    private Class<? extends RowToObject> clazz;

    private RowToObjectDataSource rowToObjectDataSource;

    private Map<String, Object> context = new HashMap<String, Object>();

    private Object[] args;

    private static final Logger LOGGER = LoggerFactory.getLogger(RowsToObject.class);

    /**
     * à utiliser si on souhaite partager des variables entre steps via la Map context.
     */
    public RowsToObject(DataTable dataTable, RowToObjectDataSource rowToObjectDataSource,
            Class<? extends RowToObject> clazz) {
        this.dataTable = dataTable;
        this.clazz = clazz;
        this.rowToObjectDataSource = rowToObjectDataSource;
    }

    public List<Res> executeInRows() {
        List<List<String>> rows = dataTable.raw();
        Map<String, Integer> headers = createMapHeaders(rows.get(0));
        List<Res> results = new ArrayList<Res>();
        for (int i = 1; i < rows.size(); i++) {
            RowToObject<RowToObjectDataSource, Res> rowToObject;
            List<Object> argsForConstructor = new ArrayList<Object>();
            argsForConstructor.add(headers);
            argsForConstructor.add(rows.get(i));
            argsForConstructor.add(rowToObjectDataSource);

            rowToObject = buildRowToObject(clazz, argsForConstructor, Map.class, List.class,
                    rowToObjectDataSource.getClass());

            if (rowToObject == null) {
                throw new IllegalStateException("pas de constructeur trouvé pour la classe " + clazz.getName());
            }
            rowToObject.setArgs(args);
            rowToObject.setContext(context);

            results.add(rowToObject.mapRowToObject());
        }
        return results;
    }

    /**
     * @param clazz   class implémentant RowToObject
     * @param args    arguments du constructeur
     * @param classes classes des arguments du constructeur, afin de retrouver ce constructeur dans la classe
     * @return objet RowToObject instancié
     */
    private RowToObject buildRowToObject(Class clazz, List<Object> args, Class... classes) {
        RowToObject rowToObject = null;
        final StringBuilder message = new StringBuilder(
                "la classe " + clazz.getCanonicalName() + " n'a pas de constructeur ayant pour arguments:");
        for (Class aClass : classes) {
            message.append(aClass.getCanonicalName());
            message.append(",");
        }

        try {
            Constructor constructor = clazz.getConstructor(classes);
            if (constructor != null) {
                final List<Object> list = args.subList(0, classes.length);
                final Object[] classes1 = list.toArray(new Object[list.size()]);
                rowToObject = (RowToObject) constructor.newInstance(classes1);
            } else {
                LOGGER.debug(message.toString());
            }
        } catch (NoSuchMethodException e) {
            LOGGER.debug(message.toString());
            throw new CucumberException(message.toString(), e);
        } catch (InvocationTargetException e) {
            throw new CucumberException(e);
        } catch (InstantiationException e) {
            throw new CucumberException(e);
        } catch (IllegalAccessException e) {
            throw new CucumberException(e);
        } catch (IllegalArgumentException e) {
            LOGGER.debug("classes:" + classes);
            LOGGER.debug("args:" + args);
            throw new CucumberException(e);
        }
        return rowToObject;
    }

    private Map<String, Integer> createMapHeaders(final List<String> headers) {
        HashMap<String, Integer> mapHeaders = new HashMap<String, Integer>();
        if (CollectionUtils.isNotEmpty(headers)) {
            for (int i = 0; i < headers.size(); i++) {
                mapHeaders.put(headers.get(i), i);
            }
        }
        return mapHeaders;
    }

    public void setContext(final Map<String, Object> context) {
        this.context = context;
    }

    public void setArgs(final Object[] args) {
         this.args = (args == null)? null : ObjectArrays.newArray(args, args.length);
    }
}
