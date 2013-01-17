package fixtures.common.rows;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import com.google.common.collect.Lists;
import cucumber.api.DataTable;
import org.junit.Test;

public class RowsToObjectTest {

    // =============================================================================================
    // setArgs
    // =============================================================================================

    @Test
    public void testSetArgs_null_array() throws Exception {
        RowsToObject rowsToObject = new RowsToObject(DataTable.create(Lists.newArrayList()), null, null);
        Object[] objects = null;
        //
        rowsToObject.setArgs(objects);
        //
        assertThat(rowsToObject.args, is(nullValue()));
    }

    @Test
    public void testSetArgs_empty_array() throws Exception {
        RowsToObject rowsToObject = new RowsToObject(DataTable.create(Lists.newArrayList()), null, null);
        Object[] objects = new Object[0];
        //
        rowsToObject.setArgs(objects);
        //
        assertThat(rowsToObject.args, is(new Object[0]));
    }

    @Test
    public void testSetArgs_array_with_one_data() throws Exception {
        RowsToObject rowsToObject = new RowsToObject(DataTable.create(Lists.newArrayList()), null, null);
        Object[] objects = new Object[]{"one data"};
        //
        rowsToObject.setArgs(objects);
        //
        assertThat(rowsToObject.args, is(new Object[]{"one data"}));
    }

    @Test
    public void testSetArgs_array_with_many_data() throws Exception {
        RowsToObject rowsToObject = new RowsToObject(DataTable.create(Lists.newArrayList()), null, null);
        Object[] objects = new Object[]{"one data", "two data"};
        //
        rowsToObject.setArgs(objects);
        //
        assertThat(rowsToObject.args, is(new Object[]{"one data", "two data"}));
    }

    @Test
    public void testSetArgs_array_with_many_data_less_data_after() throws Exception {
        RowsToObject rowsToObject = new RowsToObject(DataTable.create(Lists.newArrayList()), null, null);
        Object[] objects = new Object[]{"one data", "two data"};
        //
        rowsToObject.setArgs(objects);
        assertThat(rowsToObject.args, is(new Object[]{"one data", "two data"}));
        //
        Object[] objects02 = new Object[]{"one data2"};
        rowsToObject.setArgs(objects02);
        assertThat(rowsToObject.args, is(new Object[]{"one data2"}));
    }

    @Test
    public void testSetArgs_array_with_many_data_more_data_after() throws Exception {
        RowsToObject rowsToObject = new RowsToObject(DataTable.create(Lists.newArrayList()), null, null);
        Object[] objects = new Object[]{"one data", "two data"};
        //
        rowsToObject.setArgs(objects);
        assertThat(rowsToObject.args, is(new Object[]{"one data", "two data"}));
        //
        Object[] objects02 = new Object[]{"one data2", "two data2", "three data2"};
        rowsToObject.setArgs(objects02);
        assertThat(rowsToObject.args, is(new Object[]{"one data2", "two data2", "three data2"}));
    }
}
