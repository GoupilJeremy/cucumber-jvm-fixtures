package fixtures.common.transformers;

import java.util.Comparator;

public abstract class LineComparator<Line> implements Comparator<Line>{

    private int order = 1;



    protected String column;

    public  LineComparator sortBy(String column,boolean asc){
        this.column = column;
        if (!asc) {
            order = -1;
        }
        return this;
    }

    @Override
    public int compare(final Line row1, final Line row2) {
        if(column==null){
            throw new IllegalStateException("sort column has not been set");
        }
        Comparable value1 = getValue(row1);
        Comparable value2 = getValue(row2);
        int result;
        // la colonne avec le header reste en premier

        if(value1==null && value2 == null){
            result = 0;
        }else if(value2 == null){
            result = 1;
        }else if (value1 == null){
            result = -1;
        }else if (value1.equals(column)) {
            result= -1;
        }else {
            result = (value1.compareTo(value2)) * order;
        }
        return result;
    }

    public String getColumn() {
        return column;
    }

    protected abstract Comparable getValue(Line row);



}
