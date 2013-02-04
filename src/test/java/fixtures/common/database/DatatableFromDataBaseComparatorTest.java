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
           query.add(createMapWithBaseColumn("125", "bob", "bob@email.com", "foobar"));
           query.add(createMapWithBaseColumn("666", "john", "johon@email.com", "foobar"));
           query.add(createMapWithBaseColumn("877", "rob", "rob@email.com", "foobar"));

           DataTable table = new DataTable(rows, new TableConverter(getXStream(), null));
           table.diff(//
                   DatatableFromDataBaseComparator.from(table, query)//
                   .replaceHeadersWith(FromXToDatatableEnum.class)
                   .sortBy(FromXToDatatableEnum.A.getDatatableColumnName())//
                   .toDataTable());
       }

       private static LocalizedXStreams.LocalizedXStream getXStream() {
           ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
           return new LocalizedXStreams(classLoader).get(Locale.getDefault());
       }

       @Test
       public void testCompareBaseToTable_randomly_ordered_columns_from_dataTable() throws Exception {
           List<DataTableRow> expectedRows = new ArrayList<DataTableRow>();
           ArrayList<Comment> comments = new ArrayList<Comment>();
           expectedRows.add(new DataTableRow(comments, Arrays.asList(FromXToDatatableEnum.A.getDatatableColumnName(), FromXToDatatableEnum.B.getDatatableColumnName(), FromXToDatatableEnum.D.getDatatableColumnName()), 0));
           expectedRows.add(new DataTableRow(comments, Arrays.asList("125", "bob", "foobar"), 1));
           expectedRows.add(new DataTableRow(comments, Arrays.asList("666", "john", "foobar"), 2));
           expectedRows.add(new DataTableRow(comments, Arrays.asList("877", "rob", "foobar"), 3));
           //
           final List<Map<String,  ? extends Comparable>> query = new ArrayList<Map<String,  ? extends Comparable>>();
           query.add(createMapWithBaseColumn("125", "bob", "bob@email.com", "foobar"));
           query.add(createMapWithBaseColumn("666", "john", "johon@email.com", "foobar"));
           query.add(createMapWithBaseColumn("877", "rob", "rob@email.com", "foobar"));

           DataTable expected = new DataTable(expectedRows, new TableConverter(getXStream(), null));
           expected.diff(DatatableFromDataBaseComparator.from(expected, query).replaceHeadersWith(FromXToDatatableEnum.class).sortBy(FromXToDatatableEnum.A.getDatatableColumnName()).toDataTable());
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
           query.add(createMapWithBaseColumn("125", "bob", "bob@email.com", "foobar"));
           query.add(createMapWithBaseColumn("666", "john", "johon@email.com", "foobar"));
           query.add(createMapWithBaseColumn("877", "rob", "rob@email.com", "foobar"));

           DataTable table = new DataTable(rows, new TableConverter(getXStream(), null));
           table.diff(DatatableFromDataBaseComparator.from(table, query).replaceHeadersWith(FromXToDatatableEnum.class).sortBy(FromXToDatatableEnum.A.getDatatableColumnName()).toDataTable());
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
           query.add(createMapWithTableColumn("877", "rob", "rob@email.com", "foobar"));
           query.add(createMapWithTableColumn("125", "bob", "bob@email.com", "foobar"));
           query.add(createMapWithTableColumn("666", "john", "johon@email.com", "foobar"));

           DataTable expected = new DataTable(rows, new TableConverter(getXStream(), null));
           expected.diff(DatatableFromDataBaseComparator.from(expected, query).sortBy(FromXToDatatableEnum.A.getDatatableColumnName()).toDataTable());
       }

       @Test
       public void testCompareBaseToTable_reordered_columns_by_name_from_base() throws Exception {
           // expected
           List<DataTableRow> rows = new ArrayList<DataTableRow>();
           ArrayList<Comment> comments = new ArrayList<Comment>();
           DataTableRow dataTableHeaderRow = getDataTableRowWithDatabaseColumnNames(comments);
           rows.add(dataTableHeaderRow);
           rows.add(new DataTableRow(comments, Arrays.asList("aaa", "125", "foobar"), 1));
           rows.add(new DataTableRow(comments, Arrays.asList("bbb", "877", "foobar"), 2));
           rows.add(new DataTableRow(comments, Arrays.asList("ccc", "666", "foobar"), 3));
           //
           final List<Map<String,  ? extends Comparable>> query = new ArrayList<Map<String,? extends Comparable>>();
           query.add(createMapWithBaseColumn("ccc", "666", "foobar", "rob@email.com"));
           query.add(createMapWithBaseColumn("aaa", "125", "foobar", "bob@email.com"));
           query.add(createMapWithBaseColumn("bbb", "877", "foobar", "johon@email.com"));

           DataTable expected = new DataTable(rows, new TableConverter(getXStream(), null));
           expected.diff(DatatableFromDataBaseComparator.from(expected, query).replaceHeadersWith(FromXToDatatableEnum.class)
                   .sortBy(FromXToDatatableEnum.A.getDatatableColumnName(), true).toDataTable());
       }

    @Test
    public void testCompareBaseToTable_reordered_columns_by_name_desc_from_base() throws Exception {
        // expected
        List<DataTableRow> rows = new ArrayList<DataTableRow>();
        ArrayList<Comment> comments = new ArrayList<Comment>();
        DataTableRow dataTableHeaderRow = getDataTableRowWithDatabaseColumnNames(comments);
        rows.add(dataTableHeaderRow);
        rows.add(new DataTableRow(comments, Arrays.asList("ccc", "666", "foobar"), 3));
        rows.add(new DataTableRow(comments, Arrays.asList("bbb", "877", "foobar"), 2));
        rows.add(new DataTableRow(comments, Arrays.asList("aaa", "125", "foobar"), 1));
        //
        final List<Map<String,  ? extends Comparable>> query = new ArrayList<Map<String,? extends Comparable>>();
        query.add(createMapWithBaseColumn("ccc", "666", "foobar", "rob@email.com"));
        query.add(createMapWithBaseColumn("aaa", "125", "foobar", "bob@email.com"));
        query.add(createMapWithBaseColumn("bbb", "877", "foobar", "johon@email.com"));

        DataTable expected = new DataTable(rows, new TableConverter(getXStream(), null));
        expected.diff(DatatableFromDataBaseComparator.from(expected, query).replaceHeadersWith(FromXToDatatableEnum.class)
                .sortBy(FromXToDatatableEnum.A.getDatatableColumnName(), false).toDataTable());
    }




    private DataTableRow getDataTableRowWithDatabaseColumnNames(final ArrayList<Comment> comments) {
        return new DataTableRow(comments, Arrays.asList(FromXToDatatableEnum.A.getDatatableColumnName(),
                FromXToDatatableEnum.B.getDatatableColumnName(), FromXToDatatableEnum.C.getDatatableColumnName()),
                HEADER_DATATABLE_INDEX);
    }

       private Map<String,  ? extends Comparable> createMapWithBaseColumn(String foo, String bar, String qix, String pioupiou) {
           Map<String, String> map = new HashMap<String, String>();
           map.put(FromXToDatatableEnum.A.getReplacementColumnName(), foo);
           map.put(FromXToDatatableEnum.B.getReplacementColumnName(), bar);
           map.put(FromXToDatatableEnum.C.getReplacementColumnName(), qix);
           map.put(FromXToDatatableEnum.D.getReplacementColumnName(), pioupiou);
           return map;
       }

    private Map<String,  ? extends Comparable> createMapWithTableColumn(String foo, String bar, String qix, String pioupiou) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(FromXToDatatableEnum.A.getDatatableColumnName(), foo);
        map.put(FromXToDatatableEnum.B.getDatatableColumnName(), bar);
        map.put(FromXToDatatableEnum.C.getDatatableColumnName(), qix);
        map.put(FromXToDatatableEnum.D.getDatatableColumnName(), pioupiou);
        return map;
    }


}
