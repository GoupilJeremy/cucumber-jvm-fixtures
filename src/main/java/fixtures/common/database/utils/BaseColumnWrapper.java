package fixtures.common.database.utils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class BaseColumnWrapper {
    private EnumSet enumSet;

    public BaseColumnWrapper(Class myEnum) {
        enumSet = EnumSet.allOf(myEnum);
    }

    public List<IBaseColumnToTable> getBaseColumnToTable() {
        return new ArrayList<IBaseColumnToTable>(enumSet);
    }

    public List<String> getBaseColumnNames() {
        return getColumns(new Function<IBaseColumnToTable, String>() {
            @Override
            public String apply(final IBaseColumnToTable input) {
                return input.getBaseColumnName();
            }
        });
    }

    public List<String> getDatatableColumnNames() {
        return getColumns(new Function<IBaseColumnToTable, String>() {
            @Override
            public String apply(final IBaseColumnToTable input) {
                return input.getDatatableColumnName();
            }
        });
    }

    private List<String> getColumns(Function<IBaseColumnToTable, String> function) {
        List<String> results = Lists.newArrayList();
        results.addAll(Collections2.transform(enumSet, function));
        return results;
    }
}
