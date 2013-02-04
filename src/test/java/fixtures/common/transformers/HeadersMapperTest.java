package fixtures.common.transformers;

import com.google.common.collect.Lists;
import fixtures.common.database.FromXToDatatableEnum;
import fixtures.common.database.MapperContainer;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HeadersMapperTest {
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_enum_is_null() throws Exception {
        new HeadersMapper(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_enum_is_not_good_instance() throws Exception {
        new HeadersMapper(String.class);
    }

    @Test
    public void testGetBaseColumnNames() throws Exception {
        HeadersMapper wrapper = new HeadersMapper(FromXToDatatableEnum.class);
        List<String> baseColumnNames = wrapper.getReplacementColumnNames();
        //
        assertThat(baseColumnNames.size(), is(FromXToDatatableEnum.values().length));
        assertThat(baseColumnNames, JUnitMatchers
                .hasItems(FromXToDatatableEnum.A.getReplacementColumnName(), FromXToDatatableEnum.B.getReplacementColumnName(),
                        FromXToDatatableEnum.C.getReplacementColumnName(), FromXToDatatableEnum.D.getReplacementColumnName()));
    }

    @Test
    public void testGetDatatableColumnNames() throws Exception {
        HeadersMapper wrapper = new HeadersMapper(FromXToDatatableEnum.class);
        List<String> baseColumnNames = wrapper.getDatatableColumnNames();
        //
        assertThat(baseColumnNames.size(), is(FromXToDatatableEnum.values().length));
        assertThat(baseColumnNames, JUnitMatchers.hasItems(FromXToDatatableEnum.A.getDatatableColumnName(),
                FromXToDatatableEnum.B.getDatatableColumnName(), FromXToDatatableEnum.C.getDatatableColumnName(),
                FromXToDatatableEnum.D.getDatatableColumnName()));
    }

    @Test
    public void testGetBaseColumnToTable() throws Exception {
        HeadersMapper wrapper = new HeadersMapper(FromXToDatatableEnum.class);
        List<MapperContainer> baseColumnToTable = wrapper.getReplacementColumnToTable();

        List<MapperContainer> expected = Lists
                .<MapperContainer>newArrayList(FromXToDatatableEnum.A, FromXToDatatableEnum.B,
                        FromXToDatatableEnum.C, FromXToDatatableEnum.D);
        assertThat(baseColumnToTable, is(expected));
	    assertThat(baseColumnToTable.get(0).getColumnType(),is(FromXToDatatableEnum.A.getColumnType()));
    }
}
