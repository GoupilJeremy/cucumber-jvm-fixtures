package fixtures.common.database;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TableFromDatabaseWrapperTest {
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_enum_is_null() throws Exception {
        new TableFromDatabaseWrapper(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_enum_is_not_good_instance() throws Exception {
        new TableFromDatabaseWrapper(String.class);
    }

    @Test
    public void testGetBaseColumnNames() throws Exception {
        TableFromDatabaseWrapper wrapper = new TableFromDatabaseWrapper(FromXToDatatableEnum.class);
        List<String> baseColumnNames = wrapper.getBaseColumnNames();
        //
        assertThat(baseColumnNames.size(), is(FromXToDatatableEnum.values().length));
        assertThat(baseColumnNames, JUnitMatchers
                .hasItems(FromXToDatatableEnum.A.getBaseColumnName(), FromXToDatatableEnum.B.getBaseColumnName(),
                        FromXToDatatableEnum.C.getBaseColumnName(), FromXToDatatableEnum.D.getBaseColumnName()));
    }

    @Test
    public void testGetDatatableColumnNames() throws Exception {
        TableFromDatabaseWrapper wrapper = new TableFromDatabaseWrapper(FromXToDatatableEnum.class);
        List<String> baseColumnNames = wrapper.getDatatableColumnNames();
        //
        assertThat(baseColumnNames.size(), is(FromXToDatatableEnum.values().length));
        assertThat(baseColumnNames, JUnitMatchers.hasItems(FromXToDatatableEnum.A.getDatatableColumnName(),
                FromXToDatatableEnum.B.getDatatableColumnName(), FromXToDatatableEnum.C.getDatatableColumnName(),
                FromXToDatatableEnum.D.getDatatableColumnName()));
    }

    @Test
    public void testGetBaseColumnToTable() throws Exception {
        TableFromDatabaseWrapper wrapper = new TableFromDatabaseWrapper(FromXToDatatableEnum.class);
        List<IBaseColumnToTable> baseColumnToTable = wrapper.getBaseColumnToTable();

        List<IBaseColumnToTable> expected = Lists
                .<IBaseColumnToTable>newArrayList(FromXToDatatableEnum.A, FromXToDatatableEnum.B,
                        FromXToDatatableEnum.C, FromXToDatatableEnum.D);
        assertThat(baseColumnToTable, is(expected));
	    assertThat(baseColumnToTable.get(0).getColumnType(),is(FromXToDatatableEnum.A.getColumnType()));
    }
}
