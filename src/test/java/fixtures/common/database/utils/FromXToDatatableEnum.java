package fixtures.common.database.utils;

import java.sql.Types;

public enum FromXToDatatableEnum implements IBaseColumnToTable {

    A("a", "A", Types.NUMERIC),
    B("b", "B", Types.VARCHAR),
    C("c", "C", Types.VARCHAR),
    D("d", "D", Types.VARCHAR);

    private String datatableColumnName;

    private String databaseColumnName;

    private int databaseType;

    FromXToDatatableEnum(final String datatableColumnName, final String databaseColumnName, final int databaseType) {
        this.databaseColumnName = databaseColumnName;
        this.datatableColumnName = datatableColumnName;
        this.databaseType = databaseType;
    }

    @Override
    public String getBaseColumnName() {
        return databaseColumnName;
    }

    @Override
    public int getColumnType() {
        return databaseType;
    }

    @Override
    public String getDatatableColumnName() {
        return datatableColumnName;
    }
}
