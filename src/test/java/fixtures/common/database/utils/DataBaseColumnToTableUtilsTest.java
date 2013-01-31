package fixtures.common.database.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cucumber.api.DataTable;
import cucumber.runtime.table.TableConverter;
import cucumber.runtime.xstream.LocalizedXStreams;
import fixtures.common.database.DataBaseColumnToTableUtils;
import gherkin.formatter.model.Comment;
import gherkin.formatter.model.DataTableRow;
import org.junit.Test;

public class DataBaseColumnToTableUtilsTest {
    public static final int HEADER_DATATABLE_INDEX = 0;

    @Test
    public void testCompareBaseToTable_nominal_case() throws Exception {
        // table
        List<DataTableRow> rows = new ArrayList<DataTableRow>();
        ArrayList<Comment> comments = new ArrayList<Comment>();
        rows.add(new DataTableRow(comments, Arrays.asList(FromXToDatatableEnum.A.getDatatableColumnName(), FromXToDatatableEnum.B.getDatatableColumnName(), FromXToDatatableEnum.D.getDatatableColumnName()), 0));
        rows.add(new DataTableRow(comments, Arrays.asList("125", "bob", "foobar"), 1));
        rows.add(new DataTableRow(comments, Arrays.asList("666", "john", "foobar"), 2));
        rows.add(new DataTableRow(comments, Arrays.asList("877", "rob", "foobar"), 3));
        //
        final List<Map<String, Object>> query = new ArrayList<Map<String, Object>>();
        query.add(createMap("125", "bob", "bob@email.com", "foobar"));
        query.add(createMap("666", "john", "johon@email.com", "foobar"));
        query.add(createMap("877", "rob", "rob@email.com", "foobar"));

        DataTable table = new DataTable(rows, new TableConverter(getXStream(), null));
        DataBaseColumnToTableUtils.compareBaseToTable(table, query, FromXToDatatableEnum.class);
    }

    private static LocalizedXStreams.LocalizedXStream getXStream() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        LocalizedXStreams.LocalizedXStream xStream = new LocalizedXStreams(classLoader).get(Locale.getDefault());
        return xStream;
    }

    @Test
    public void testCompareBaseToTable_randomly_ordered_columns_from_dataTable() throws Exception {
        // table
        List<DataTableRow> rows = new ArrayList<DataTableRow>();
        ArrayList<Comment> comments = new ArrayList<Comment>();
        rows.add(new DataTableRow(comments, Arrays.asList(FromXToDatatableEnum.A.getDatatableColumnName(), FromXToDatatableEnum.B.getDatatableColumnName(), FromXToDatatableEnum.D.getDatatableColumnName()), 0));
        rows.add(new DataTableRow(comments, Arrays.asList( "125","bob", "foobar"), 1));
        rows.add(new DataTableRow(comments, Arrays.asList("666","john", "foobar"), 2));
        rows.add(new DataTableRow(comments, Arrays.asList("877","rob", "foobar"), 3));
        //
        final List<Map<String, Object>> query = new ArrayList<Map<String, Object>>();
        query.add(createMap("125", "bob", "bob@email.com", "foobar"));
        query.add(createMap("666", "john", "johon@email.com", "foobar"));
        query.add(createMap("877", "rob", "rob@email.com", "foobar"));

        DataTable table = new DataTable(rows, new TableConverter(getXStream(), null));
        DataBaseColumnToTableUtils.compareBaseToTable(table, query, FromXToDatatableEnum.class);
    }

    @Test
    public void testCompareBaseToTable_check_active_from_dataTable() throws Exception {
        // table
        List<DataTableRow> rows = new ArrayList<DataTableRow>();
        ArrayList<Comment> comments = new ArrayList<Comment>();
        rows.add(new DataTableRow(comments, Arrays.asList(FromXToDatatableEnum.A.getDatatableColumnName(), FromXToDatatableEnum.B.getDatatableColumnName(), FromXToDatatableEnum.D.getDatatableColumnName()), 0));
        rows.add(new DataTableRow(comments, Arrays.asList("125", "bob", "foobar"), 1));
        rows.add(new DataTableRow(comments, Arrays.asList("666", "john", "foobar"), 2));
        rows.add(new DataTableRow(comments, Arrays.asList("877", "rob", "foobar"), 3));
        //
        final List<Map<String, Object>> query = new ArrayList<Map<String, Object>>();
        query.add(createMap("125", "bob", "bob@email.com", "foobar"));
        query.add(createMap("666", "john", "johon@email.com", "foobar"));
        query.add(createMap("877", "rob", "rob@email.com", "foobar"));

        DataTable table = new DataTable(rows, new TableConverter(getXStream(), null));
        DataBaseColumnToTableUtils.compareBaseToTable(table, query, FromXToDatatableEnum.class);
    }

    @Test
    public void testCompareBaseToTable_reordered_columns_by_id_from_base() throws Exception {
        // table
        List<DataTableRow> rows = new ArrayList<DataTableRow>();
        ArrayList<Comment> comments = new ArrayList<Comment>();
        rows.add(new DataTableRow(comments, Arrays.asList(FromXToDatatableEnum.A.getDatatableColumnName(), FromXToDatatableEnum.B.getDatatableColumnName(), FromXToDatatableEnum.D.getDatatableColumnName()), 0));
        rows.add(new DataTableRow(comments, Arrays.asList("125","bob", "foobar"), 1));
        rows.add(new DataTableRow(comments, Arrays.asList("666","john" , "foobar"), 2));
        rows.add(new DataTableRow(comments, Arrays.asList("877","rob" , "foobar"), 3));
        //
        final List<Map<String, Object>> query = new ArrayList<Map<String, Object>>();
        query.add(createMap("877", "rob", "rob@email.com", "foobar"));
        query.add(createMap("125", "bob", "bob@email.com", "foobar"));
        query.add(createMap("666", "john", "johon@email.com", "foobar"));

        DataTable table = new DataTable(rows, new TableConverter(getXStream(), null));
        DataBaseColumnToTableUtils.newBaseColumn(table, query, FromXToDatatableEnum.class).sortBy(FromXToDatatableEnum.A.getDatatableColumnName())
                .compare();
    }

    @Test
    public void testCompareBaseToTable_reordered_columns_by_name_from_base() throws Exception {
        // table
        List<DataTableRow> rows = new ArrayList<DataTableRow>();
        ArrayList<Comment> comments = new ArrayList<Comment>();
        DataTableRow dataTableHeaderRow = getDataTableRow(comments);
        rows.add(dataTableHeaderRow);
        rows.add(new DataTableRow(comments, Arrays.asList("rob", "666", "foobar"), 3));
        rows.add(new DataTableRow(comments, Arrays.asList("john", "877", "foobar"), 2));
        rows.add(new DataTableRow(comments, Arrays.asList("bob", "125", "foobar"), 1));
        //
        final List<Map<String, Object>> query = new ArrayList<Map<String, Object>>();
        query.add(createMap( "rob","666","foobar", "rob@email.com" ));
        query.add(createMap("bob", "125","foobar" , "bob@email.com"));
        query.add(createMap( "john","877", "foobar", "johon@email.com"));

        DataTable table = new DataTable(rows, new TableConverter(getXStream(), null));
        DataBaseColumnToTableUtils.newBaseColumn(table, query, FromXToDatatableEnum.class)
                .sortBy(FromXToDatatableEnum.A.getDatatableColumnName(), false).compare();
    }

    private DataTableRow getDataTableRow(final ArrayList<Comment> comments) {
        return new DataTableRow(comments, Arrays.asList(FromXToDatatableEnum.A.getDatatableColumnName(),
                FromXToDatatableEnum.B.getDatatableColumnName(), FromXToDatatableEnum.C.getDatatableColumnName()),
                    HEADER_DATATABLE_INDEX);
    }

    private Map<String, Object> createMap(String foo, String bar, String qix, String pioupiou) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(FromXToDatatableEnum.A.getBaseColumnName(), foo);
        map.put(FromXToDatatableEnum.B.getBaseColumnName(), bar);
        map.put(FromXToDatatableEnum.C.getBaseColumnName(), qix);
        map.put(FromXToDatatableEnum.D.getBaseColumnName(), pioupiou);
        return map;
    }
}
