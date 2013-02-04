package fixtures.common.database;

import java.sql.Types;

public enum FromXToDatatableEnum implements MapperContainer {

    A("a", "A", Types.NUMERIC),
    B("b", "B", Types.VARCHAR),
    C("c", "C", Types.VARCHAR),
    D("d", "D", Types.VARCHAR);

    private String datatableColumnName;

    private String databaseColumnName;

    private int databaseType;

    FromXToDatatableEnum(final String datatableColumnName, final String databaseColumnName, final int databaseType) {
        this.datatableColumnName = datatableColumnName;
        this.databaseColumnName = databaseColumnName;
        this.databaseType = databaseType;
    }

    @Override
    public String getReplacementColumnName() {
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
