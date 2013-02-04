package fixtures.common.transformers;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import gherkin.formatter.model.DataTableRow;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public  class LineComparator implements Comparator<DataTableRow>{

    private int order = 1;

    private Collator collator;


    protected String sortColumn;

    private Map<String,Integer> headersMap =Maps.newHashMap();

    public LineComparator(List<String> headers, Locale locale) {
        for (int i = 0; i < headers.size(); i++) {
            headersMap.put(headers.get(i),i);
        }
        collator= Collator.getInstance(locale);
        collator.setStrength(Collator.PRIMARY);
    }

    public  LineComparator sortBy(String column,boolean asc){
        this.sortColumn = column;
        if (!asc) {
            order = -1;
        }
        return this;
    }

    @Override
    public int compare(final DataTableRow row1, final DataTableRow row2) {
        if(sortColumn ==null){
            throw new IllegalStateException("sort sortColumn has not been set");
        }
        List<String> cells1 = row1.getCells();
        Integer sortColumnIndex = headersMap.get(sortColumn);
        String value1 = cells1.get(sortColumnIndex);
        List<String> cells2 = row2.getCells();
        String value2 = cells2.get(sortColumnIndex);
        int result;
        // la colonne avec le header reste en premier
        value1 = Strings.nullToEmpty(value1);
        value2 = Strings.nullToEmpty(value2);
        if (value1.equals(sortColumn)) {
            result= -1;
        }else {
            result = (collator.compare(value1, value2)) * order;
        }
        return result;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public boolean isAsc(){
        return order == 1;
    }




}
