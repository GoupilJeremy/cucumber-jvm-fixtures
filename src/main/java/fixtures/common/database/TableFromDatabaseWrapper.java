package fixtures.common.database;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

class TableFromDatabaseWrapper {
    private EnumSet enumSet;

    private final static Function<IBaseColumnToTable, String> GET_BASE_COLUMN_NAME = new Function<IBaseColumnToTable, String>() {
        @Override
        public String apply(final IBaseColumnToTable input) {
            if (input == null) {
                return "";
            }
            return input.getBaseColumnName();
        }
    };

    private final static Function<IBaseColumnToTable, String> GET_DATATABLE_COLUMN_NAME = new Function<IBaseColumnToTable, String>() {
        @Override
        public String apply(final IBaseColumnToTable input) {
            if (input == null) {
                return "";
            }
            return input.getDatatableColumnName();
        }
    };

    public TableFromDatabaseWrapper(Class myEnum) {
        Preconditions.checkArgument(myEnum != null, "la classe ne peut être null");
        Preconditions.checkArgument(myEnum.isEnum(), "la classe n'est une Enum");
        enumSet = EnumSet.allOf(myEnum);
    }

    public List<IBaseColumnToTable> getBaseColumnToTable() {
        return new ArrayList<IBaseColumnToTable>(enumSet);
    }

    public List<String> getBaseColumnNames() {
        return getColumns(GET_BASE_COLUMN_NAME);
    }

    public List<String> getDatatableColumnNames() {
        return getColumns(GET_DATATABLE_COLUMN_NAME);
    }

    private List<String> getColumns(Function<IBaseColumnToTable, String> function) {
        List<String> results = Lists.newArrayList();
        results.addAll(Collections2.transform(enumSet, function));
        return results;
    }
}
