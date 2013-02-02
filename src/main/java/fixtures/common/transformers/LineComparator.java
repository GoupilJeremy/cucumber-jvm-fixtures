package fixtures.common.transformers;

import java.util.Comparator;

public abstract class LineComparator<Line> implements Comparator<Line>{

    private int order = 1;



    protected String sortColumn;

    public  LineComparator sortBy(String column,boolean asc){
        this.sortColumn = column;
        if (!asc) {
            order = -1;
        }
        return this;
    }

    @Override
    public int compare(final Line row1, final Line row2) {
        if(sortColumn ==null){
            throw new IllegalStateException("sort sortColumn has not been set");
        }
        Comparable value1 = getValue(row1, sortColumn);
        Comparable value2 = getValue(row2, sortColumn);
        int result;
        // la colonne avec le header reste en premier

        if(value1==null && value2 == null){
            result = 0;
        }else if(value2 == null){
            result = 1;
        }else if (value1 == null){
            result = -1;
        }else if (value1.equals(sortColumn)) {
            result= -1;
        }else {
            result = (value1.compareTo(value2)) * order;
        }
        return result;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    protected abstract Comparable getValue(Line row,String columnName);



}
