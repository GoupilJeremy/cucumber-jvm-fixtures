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

    private Map<String, Integer> headers;

    private List<String> row;

    @Mock
    private RowToObjectDataSource rowToObjectDataSource;

    @Test
    public void testGetValue_nominal_case() {
        //given
        Map<String, Integer> headers = new HashMap<String, Integer>();
        headers.put(FIRST_COLUMN_NAME, FIRST_COLUMN_INDEX);
        List<String> row = new ArrayList<String>();
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
        List<String> row = new ArrayList<String>();

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
        Map<String, Integer> headers = new HashMap<String, Integer>();
        headers.put(FIRST_COLUMN_NAME, FIRST_COLUMN_INDEX);
        headers.put(SECOND_COLUMN_NAME, SECOND_COLUMN_INDEX);
        List<String> row = new ArrayList<String>();
        row.add(StringUtils.EMPTY);
        row.add(FIRST_COLUMN_VALUE);
        RowToObject rowToObject = new RowToObject(headers, row, rowToObjectDataSource) {
            @Override
            protected Object mapRowToObject() {
                return null;
            }
        };

        //when
        final String value = rowToObject.getValueForColumn(FIRST_COLUMN_NAME, DEFAULT_COLUMN_VALUE);

        //then
        assertThat("value =" + value, value, is(DEFAULT_COLUMN_VALUE));
    }
}
