package fixtures.common.database;

import cucumber.api.DataTable;
import cucumber.runtime.table.TableConverter;
import cucumber.runtime.xstream.LocalizedXStreams;
import gherkin.formatter.model.Comment;
import gherkin.formatter.model.DataTableRow;
import org.junit.Test;

import java.util.*;

public class DatatableFromDataBaseComparatorTest {



    public static final int HEADER_DATATABLE_INDEX = 0;

       @Test
       public void testCompareBaseToTable_nominal_case() throws Exception {
           // table
           List<DataTableRow> rows = new ArrayList<DataTableRow>();
           ArrayList<Comment> comments = new ArrayList<Comment>();
           rows.add(new DataTableRow(comments, Arrays.asList(FromXToDatatableEnum.A.getDatatableColumnName(),
                   FromXToDatatableEnum.B.getDatatableColumnName(), FromXToDatatableEnum.D.getDatatableColumnName()), 0));
           rows.add(new DataTableRow(comments, Arrays.asList("125", "bob", "foobar"), 1));
           rows.add(new DataTableRow(comments, Arrays.asList("666", "john", "foobar"), 2));
           rows.add(new DataTableRow(comments, Arrays.asList("877", "rob", "foobar"), 3));
           //
           final List<Map<String,  ? extends Comparable>> query = new ArrayList<Map<String,  ? extends Comparable>>();
           query.add(createMap("125", "bob", "bob@email.com", "foobar"));
           query.add(createMap("666", "john", "johon@email.com", "foobar"));
           query.add(createMap("877", "rob", "rob@email.com", "foobar"));

           DataTable table = new DataTable(rows, new TableConverter(getXStream(), null));
           table.diff(DatatableFromDataBaseComparator.from(table, query, FromXToDatatableEnum.class).sortBy(FromXToDatatableEnum.A.getDatatableColumnName()).toDataTable());
       }

       private static LocalizedXStreams.LocalizedXStream getXStream() {
           ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
           return new LocalizedXStreams(classLoader).get(Locale.getDefault());
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
           final List<Map<String,  ? extends Comparable>> query = new ArrayList<Map<String,  ? extends Comparable>>();
           query.add(createMap("125", "bob", "bob@email.com", "foobar"));
           query.add(createMap("666", "john", "johon@email.com", "foobar"));
           query.add(createMap("877", "rob", "rob@email.com", "foobar"));

           DataTable table = new DataTable(rows, new TableConverter(getXStream(), null));
           table.diff(DatatableFromDataBaseComparator.from(table, query, FromXToDatatableEnum.class).sortBy(FromXToDatatableEnum.A.getDatatableColumnName()).toDataTable());
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
           final List<Map<String,  ? extends Comparable>> query = new ArrayList<Map<String,  ? extends Comparable>>();
           query.add(createMap("125", "bob", "bob@email.com", "foobar"));
           query.add(createMap("666", "john", "johon@email.com", "foobar"));
           query.add(createMap("877", "rob", "rob@email.com", "foobar"));

           DataTable table = new DataTable(rows, new TableConverter(getXStream(), null));
           table.diff(DatatableFromDataBaseComparator.from(table, query, FromXToDatatableEnum.class).sortBy(FromXToDatatableEnum.A.getDatatableColumnName()).toDataTable());
       }

       @Test
       public void testCompareBaseToTable_reordered_columns_by_id_from_base() throws Exception {
           List<DataTableRow> rows = new ArrayList<DataTableRow>();
           ArrayList<Comment> comments = new ArrayList<Comment>();
           rows.add(new DataTableRow(comments, Arrays.asList(FromXToDatatableEnum.A.getDatatableColumnName(), FromXToDatatableEnum.B.getDatatableColumnName(), FromXToDatatableEnum.D.getDatatableColumnName()), 0));
           rows.add(new DataTableRow(comments, Arrays.asList("125","bob", "foobar"), 1));
           rows.add(new DataTableRow(comments, Arrays.asList("666","john" , "foobar"), 2));
           rows.add(new DataTableRow(comments, Arrays.asList("877","rob" , "foobar"), 3));
           //
           final List<Map<String,  ? extends Comparable>> query = new ArrayList<Map<String,  ? extends Comparable>>();
           query.add(createMap("877", "rob", "rob@email.com", "foobar"));
           query.add(createMap("125", "bob", "bob@email.com", "foobar"));
           query.add(createMap("666", "john", "johon@email.com", "foobar"));

           DataTable expected = new DataTable(rows, new TableConverter(getXStream(), null));
           expected.diff(DatatableFromDataBaseComparator.from(expected, query, FromXToDatatableEnum.class).sortBy(FromXToDatatableEnum.A.getDatatableColumnName()).toDataTable());
       }

       @Test
       public void testCompareBaseToTable_reordered_columns_by_name_from_base() throws Exception {
           // expected
           List<DataTableRow> rows = new ArrayList<DataTableRow>();
           ArrayList<Comment> comments = new ArrayList<Comment>();
           DataTableRow dataTableHeaderRow = getDataTableRow(comments);
           rows.add(dataTableHeaderRow);
           rows.add(new DataTableRow(comments, Arrays.asList("bob", "125", "foobar"), 1));
           rows.add(new DataTableRow(comments, Arrays.asList("john", "877", "foobar"), 2));
           rows.add(new DataTableRow(comments, Arrays.asList("rob", "666", "foobar"), 3));
           //
           final List<Map<String,  ? extends Comparable>> query = new ArrayList<Map<String,  ? extends Comparable>>();
           query.add(createMap( "rob","666","foobar", "rob@email.com" ));
           query.add(createMap("bob", "125","foobar" , "bob@email.com"));
           query.add(createMap( "john","877", "foobar", "johon@email.com"));

           DataTable expected = new DataTable(rows, new TableConverter(getXStream(), null));
           expected.diff(DatatableFromDataBaseComparator.from(expected, query, FromXToDatatableEnum.class)
                   .sortBy(FromXToDatatableEnum.A.getDatatableColumnName(), false).toDataTable());
       }

       private DataTableRow getDataTableRow(final ArrayList<Comment> comments) {
           return new DataTableRow(comments, Arrays.asList(FromXToDatatableEnum.A.getDatatableColumnName(),
                   FromXToDatatableEnum.B.getDatatableColumnName(), FromXToDatatableEnum.C.getDatatableColumnName()),
                       HEADER_DATATABLE_INDEX);
       }

       private Map<String,  ? extends Comparable> createMap(String foo, String bar, String qix, String pioupiou) {
           Map<String, String> map = new HashMap<String, String>();
           map.put(FromXToDatatableEnum.A.getBaseColumnName(), foo);
           map.put(FromXToDatatableEnum.B.getBaseColumnName(), bar);
           map.put(FromXToDatatableEnum.C.getBaseColumnName(), qix);
           map.put(FromXToDatatableEnum.D.getBaseColumnName(), pioupiou);
           return map;
       }
}
