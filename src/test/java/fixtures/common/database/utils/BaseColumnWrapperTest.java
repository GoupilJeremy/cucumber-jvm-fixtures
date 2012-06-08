package fixtures.common.database.utils;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

public class BaseColumnWrapperTest {

    @Test
    public void testGetBaseColumnNames() throws Exception {
        BaseColumnWrapper wrapper = new BaseColumnWrapper(FromXToDatatableEnum.class);
        List<String> baseColumnNames = wrapper.getBaseColumnNames();
        //
        Assert.assertThat(baseColumnNames.size(), is(FromXToDatatableEnum.values().length));
        org.junit.Assert.assertThat(baseColumnNames, JUnitMatchers
                .hasItems(FromXToDatatableEnum.A.getBaseColumnName(), FromXToDatatableEnum.B.getBaseColumnName(),
                        FromXToDatatableEnum.C.getBaseColumnName(), FromXToDatatableEnum.D.getBaseColumnName()));
    }

    @Test
    public void testGetDatatableColumnNames() throws Exception {
        BaseColumnWrapper wrapper = new BaseColumnWrapper(FromXToDatatableEnum.class);
        List<String> baseColumnNames = wrapper.getDatatableColumnNames();
        //
        Assert.assertThat(baseColumnNames.size(), is(FromXToDatatableEnum.values().length));
        Assert.assertThat(baseColumnNames, JUnitMatchers
                .hasItems(FromXToDatatableEnum.A.getDatatableColumnName(),
                        FromXToDatatableEnum.B.getDatatableColumnName(),
                        FromXToDatatableEnum.C.getDatatableColumnName(),
                        FromXToDatatableEnum.D.getDatatableColumnName()));
    }
}
