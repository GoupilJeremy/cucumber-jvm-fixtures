package fixtures.common.rows;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fixtures.common.RowToObjectDataSource;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.mockito.Mock;

public class RowToObjectTest {
    protected static final String FIRST_COLUMN_NAME = "dummy column name";

    protected static final String FIRST_COLUMN_VALUE = "dummy column value";

    protected static final int FIRST_COLUMN_INDEX = 0;

    protected static final Integer SECOND_COLUMN_INDEX = 1;

    protected static final String DEFAULT_COLUMN_VALUE = "toto";

    protected static final Integer BAD_COLUMN_INDEX = 1;

    protected static final String SECOND_COLUMN_NAME = "second column name";

    private static final String NOT_IN_LIST = "no_in_list";

    private static final String NOT_IN_LIST_2 = "no_in_list_2";

    private Map<String, Integer> headers = new HashMap<String, Integer>();

    private List<String> row = new ArrayList<String>();

    @Mock
    private RowToObjectDataSource rowToObjectDataSource;

    @Test
    public void testGetValue_nominal_case() {
        //given
        headers.put(FIRST_COLUMN_NAME, FIRST_COLUMN_INDEX);

        row.add(FIRST_COLUMN_VALUE);
        RowToObject rowToObject = new RowToObject(headers, row, rowToObjectDataSource) {
            @Override
            protected Object mapRowToObject() {
                return null;
            }
        };

        //when
        final String value = rowToObject.getValue(FIRST_COLUMN_NAME);

        //then
        assertThat("value =" + value, value, is(FIRST_COLUMN_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValue_with_bad_column_index() {
        //given
        Map<String, Integer> headers = new HashMap<String, Integer>();
        headers.put(FIRST_COLUMN_NAME, BAD_COLUMN_INDEX);
        List<String> row = new ArrayList<String>();
        row.add(FIRST_COLUMN_VALUE);

        new RowToObject(headers, row, rowToObjectDataSource) {
            @Override
            protected Object mapRowToObject() {
                return null;
            }
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValue_with_empty_row() {
        //given
        Map<String, Integer> headers = new HashMap<String, Integer>();
        headers.put(FIRST_COLUMN_NAME, FIRST_COLUMN_INDEX);
        List<String> row = new ArrayList<String>();

        new RowToObject(headers, row, rowToObjectDataSource) {
            @Override
            protected Object mapRowToObject() {
                return null;
            }
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValue_with_empty_headers() {
        //given
        Map<String, Integer> headers = new HashMap<String, Integer>();
        List<String> row = new ArrayList<String>();

        row.add(FIRST_COLUMN_VALUE);

        new RowToObject(headers, row, rowToObjectDataSource) {
            @Override
            protected Object mapRowToObject() {
                return null;
            }
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetValue_with_null_headers() {
        //given

        row.add(FIRST_COLUMN_VALUE);

        new RowToObject(null, row, rowToObjectDataSource) {
            @Override
            protected Object mapRowToObject() {
                return null;
            }
        };
    }

    @Test
    public void test_getValue_with_Default_value() {
        //given
        RowToObject rowToObject = getRowToObject();

        //when
        final String value = rowToObject.getValueForColumn(FIRST_COLUMN_NAME, DEFAULT_COLUMN_VALUE);

        //then
        assertThat("value =" + value, value, is(DEFAULT_COLUMN_VALUE));
    }

    // =================================================================================================================

    @Test
    public void testGetValue_ellipse_nominal_case_first() {
        //given
        headers.put(FIRST_COLUMN_NAME, FIRST_COLUMN_INDEX);

        row.add(FIRST_COLUMN_VALUE);
        RowToObject rowToObject = new RowToObject(headers, row, rowToObjectDataSource) {
            @Override
            protected Object mapRowToObject() {
                return null;
            }
        };

        //when
        final String value = rowToObject.getValue(FIRST_COLUMN_NAME, NOT_IN_LIST);

        //then
        assertThat("value =" + value, value, is(FIRST_COLUMN_VALUE));
    }

    @Test
    public void testGetValue_ellipse_nominal_case_second() {
        //given
        headers.put(FIRST_COLUMN_NAME, FIRST_COLUMN_INDEX);

        row.add(FIRST_COLUMN_VALUE);
        RowToObject rowToObject = new RowToObject(headers, row, rowToObjectDataSource) {
            @Override
            protected Object mapRowToObject() {
                return null;
            }
        };

        //when
        final String value = rowToObject.getValue(NOT_IN_LIST, FIRST_COLUMN_NAME);

        //then
        assertThat("value =" + value, value, is(FIRST_COLUMN_VALUE));
    }


    @Test
    public void testGetValue_ellipse_nominal_case_no_one_found() {
        //given
        headers.put(FIRST_COLUMN_NAME, FIRST_COLUMN_INDEX);

        row.add(FIRST_COLUMN_VALUE);
        RowToObject rowToObject = new RowToObject(headers, row, rowToObjectDataSource) {
            @Override
            protected Object mapRowToObject() {
                return null;
            }
        };

        //when
        final String value = rowToObject.getValue(NOT_IN_LIST, NOT_IN_LIST_2);

        //then
        assertThat("value =" + value, value, is(""));
    }

    // =================================================================================================================
    @Test
    public void test_getArgs_copy_when_null() {
        //given
        RowToObject rowToObject = getRowToObject();
        rowToObject.setArgs(null);

        // when  then
        assertThat(rowToObject.getArgs(), is(new Object[0]));
    }

    private RowToObject getRowToObject() {
        headers.put(FIRST_COLUMN_NAME, FIRST_COLUMN_INDEX);
        headers.put(SECOND_COLUMN_NAME, SECOND_COLUMN_INDEX);
        List<String> row = new ArrayList<String>();
        row.add(StringUtils.EMPTY);
        row.add(FIRST_COLUMN_VALUE);
        return new RowToObject(headers, row, rowToObjectDataSource) {
            @Override
            protected Object mapRowToObject() {
                return null;
            }
        };
    }

    // =============================================================================================
    // setArgs
    // =============================================================================================

    @Test
    public void testSetArgs_null_array() throws Exception {
        RowToObject rowToObject = getRowToObject();
        Object[] objects = null;
        //
        rowToObject.setArgs(objects);
        //
        assertThat(rowToObject.getArgs(), is(new Object[0]));
    }

    @Test
    public void testSetArgs_empty_array() throws Exception {
        RowToObject rowToObject = getRowToObject();
        Object[] objects = new Object[0];
        //
        rowToObject.setArgs(objects);
        //
        assertThat(rowToObject.getArgs(), is(new Object[0]));
    }

    @Test
    public void testSetArgs_array_with_one_data() throws Exception {
        RowToObject rowToObject = getRowToObject();
        Object[] objects = new Object[]{"one data"};
        //
        rowToObject.setArgs(objects);
        //
        assertThat(rowToObject.getArgs(), is(new Object[]{"one data"}));
    }

    @Test
    public void testSetArgs_array_with_many_data() throws Exception {
        RowToObject rowToObject = getRowToObject();
        Object[] objects = new Object[]{"one data", "two data"};
        //
        rowToObject.setArgs(objects);
        //
        assertThat(rowToObject.getArgs(), is(new Object[]{"one data", "two data"}));
    }

    @Test
    public void testSetArgs_array_with_many_data_less_data_after() throws Exception {
        RowToObject rowToObject = getRowToObject();
        Object[] objects = new Object[]{"one data", "two data"};
        //
        rowToObject.setArgs(objects);
        assertThat(rowToObject.getArgs(), is(new Object[]{"one data", "two data"}));
        //
        Object[] objects02 = new Object[]{"one data2"};
        rowToObject.setArgs(objects02);
        assertThat(rowToObject.getArgs(), is(new Object[]{"one data2"}));
    }

    @Test
    public void testSetArgs_array_with_many_data_more_data_after() throws Exception {
        RowToObject rowToObject = getRowToObject();
        Object[] objects = new Object[]{"one data", "two data"};
        //
        rowToObject.setArgs(objects);
        assertThat(rowToObject.getArgs(), is(new Object[]{"one data", "two data"}));
        //
        Object[] objects02 = new Object[]{"one data2", "two data2", "three data2"};
        rowToObject.setArgs(objects02);
        assertThat(rowToObject.getArgs(), is(new Object[]{"one data2", "two data2", "three data2"}));
    }
}
