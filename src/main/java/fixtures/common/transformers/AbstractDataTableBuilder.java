package fixtures.common.transformers;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cucumber.api.DataTable;
import gherkin.formatter.model.Comment;
import gherkin.formatter.model.DataTableRow;
import org.apache.commons.lang.Validate;
import org.joda.time.LocalDate;

public abstract class AbstractDataTableBuilder<Line> implements IDataTableBuilder {
    public static final int HEADERS_INDEX = 0;
    protected static List<String> headersAsCells;
    protected HeadersMapper headersMapper;
    private List<Line> lines;
    private LineComparator comparator;
    private Locale locale=Locale.FRENCH;
    private String sortColumn;
    private boolean ascSort;

    protected AbstractDataTableBuilder(DataTable dataTableFromFeatureFileToCompare,List<Line> lines){
        this.lines = lines;
               final List<List<String>> raw = dataTableFromFeatureFileToCompare.raw();
               Validate.notEmpty(raw,
                       "la datatable doit contenir au moins une ligne contenant les headers,\n afin de connaître lors de la méthode 'transform', quelle propriété de l'objet mettre dans la cellule");
               this.headersAsCells = raw.get(HEADERS_INDEX);


    }

    public AbstractDataTableBuilder compareWithLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    public DataTable toDataTable() {
        List<DataTableRow> gherkinRows = buildRowsForDataTable(lines);
        return new DataTable(gherkinRows, null);
    }

    private DataTableRow getHeadersAsDatatableRow() {
        return new DataTableRow(new ArrayList<Comment>(), headersAsCells, HEADERS_INDEX);
    }

    protected List<DataTableRow> buildRowsForDataTable(final List<Line> lines) {
        Map<String,Integer> headersMap = Maps.newHashMap();
        for (int i = 0; i < headersAsCells.size(); i++) {
            headersMap.put(headersAsCells.get(i),i);
        }
        comparator = new LineComparator(headersMap, locale);

        Preconditions.checkArgument(lines != null, "la liste d'objets ne peut être null");
        List<Line> sorted = Lists.newArrayList(lines);

       if(headersMapper==null){
           headersMapper = new HeadersMapper(headersAsCells);
       }

        int lineNumber = 0;
        List<DataTableRow> rows = Lists.newArrayList();
        for (Line lineContainer : sorted) {
            List<String> cells = reorderLine(headersAsCells,lineContainer);
            DataTableRow dtRow = new DataTableRow(new ArrayList<Comment>(), cells, lineNumber);
            rows.add(dtRow);
            lineNumber++;
        }

        int index = headersAsCells.indexOf(sortColumn);
        if (index < 0) {
            throw new IllegalArgumentException("Colonne [" + sortColumn + "] inexistante pour le tri");
        }


        comparator.sortBy(sortColumn,ascSort);
        if(comparator!=null && comparator.getSortColumn()!=null){
            Collections.sort(rows, comparator);
        }

        rows.add(0, getHeadersAsDatatableRow());
        return rows;
    }

    private Map<String,String> replaceValues(Map<String,String> headersAndValues) {



        Map<String,String> modifiedMap = Maps.newHashMap();
        for (Map.Entry<String,String> entry : headersAndValues.entrySet()) {
            String header = entry.getKey();
            String value = entry.getValue();
            int type = Integer.parseInt(headersMapper.headersNamesAndTypes.get(header));
            String modifiedValue = value;

            modifiedMap.put(header,modifiedValue);
        }

        return modifiedMap;
    }

    public AbstractDataTableBuilder sortBy(String column) {
        return  sortBy(column, true);
    }

    public AbstractDataTableBuilder sortBy(String column, boolean asc) {
        this.sortColumn = column;
        ascSort = asc;
        return this;

    }

    public AbstractDataTableBuilder mapHeadersWith(Class<? extends MapperContainer> mapperContainer){
        headersMapper = new HeadersMapper(mapperContainer);
        return this;
    }

    /**
     * filter and reorder cells according to headers list.
     *
     * @param headersAsCells
     * @param line
     * @return
     */
    protected List<String> reorderLine(final List<String> headersAsCells, final Line line) {
        List<String> reorderedLine = Lists.newArrayList();
        Map<String, Object> map = toMap(line);
        if(map==null){
            throw new IllegalStateException("failure: line hasn't been converted into map; map is null");
        }
        List<String> notFoundHeaders = Lists.newArrayList();
        final Map<String, String> headersNamesAndReplacement = headersMapper.getHeadersNamesAndReplacement();
        final Map<String, String> headersNamesAndTypes = headersMapper.getHeadersNamesAndTypes();
        for (String header : headersAsCells) {
            final String replacedHeader = headersNamesAndReplacement.get(header);
            if (!map.containsKey(replacedHeader)) {
                notFoundHeaders.add(header);
            }else {
                final String type = headersNamesAndTypes.get(header);
                final Object valueFromDataSource = map.get(replacedHeader);

                reorderedLine.add(replaceValue(type, valueFromDataSource));
            }
        }
        if(!notFoundHeaders.isEmpty()){
            String message = Joiner.on(',').join(notFoundHeaders);
            throw new IllegalArgumentException("les headers '" + message + "' ne sont pas trouvés");
        }
        return reorderedLine;
    }

    protected String replaceValue(final String type, final Object valueFromDataSource) {
        return valueFromDataSource.toString();
    }

    protected abstract Map<String, Object> toMap(final Line line);
}
