package fixtures.common.transformers;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import cucumber.api.DataTable;
import fixtures.common.database.MapperContainer;
import gherkin.formatter.model.Comment;
import gherkin.formatter.model.DataTableRow;
import org.apache.commons.lang.Validate;

import java.util.*;

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
        comparator = new LineComparator(headersAsCells, locale);
        Preconditions.checkArgument(lines != null, "la liste d'objets ne peut être null");
        List<Line> sorted = Lists.newArrayList(lines);

        List<String> replacedHeaders = headersAsCells;
        if(headersMapper!=null){
            replacedHeaders = headersMapper.replaceColumns(headersAsCells);
        }

        int lineNumber = 0;
        List<DataTableRow> rows = Lists.newArrayList();
        for (Line lineContainer : sorted) {
            List<String> cells = reorderLine(replacedHeaders,lineContainer);
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

//    private List<String> replaceValues(List<String> headersAsCells) {


//            for (Map.Entry<String,  ? extends Comparable> cell : transformedLine.entrySet()) {
//                //je remplace la clé en base par la clé du tableau
//                Object content = cell.getValue();
//                MapperContainer columnEnum = getEnum(cell.getKey(), wrapper.getReplacementColumnToTable());
//                String finalContent = (content == null) ? StringUtils.EMPTY : content.toString();
//                if (columnEnum.getColumnType() == Types.BOOLEAN) {
//                    finalContent = finalContent.equals(TRUE_DATABASE_VALUE) ? TRUE_VALUE : FALSE_VALUE;
//                } else if (columnEnum.getColumnType() == Types.DATE && content != null) {
//                    Date date = (Date) content;
//                    finalContent = new LocalDate(date.getTime()).toString(DATE_PATTERN);
//                }
//                result.put(columnEnum.getDatatableColumnName(), finalContent);
//            }
//        return null;
//    }

    public AbstractDataTableBuilder sortBy(String column) {
        return  sortBy(column, true);
    }

    public AbstractDataTableBuilder sortBy(String column, boolean asc) {
        this.sortColumn = column;
        ascSort = asc;
        return this;

    }



    public AbstractDataTableBuilder replaceHeadersWith(Class<? extends MapperContainer> mapperContainer){
        headersMapper = new HeadersMapper(mapperContainer);
        return this;
    }

    /**
     * filter and reorder cells according to headers list.
     * @param headers
     * @param line
     * @return
     */
    protected List<String> reorderLine(final List<String> headers, final Line line) {
        List<String> reorderedLine = Lists.newArrayList();
        Map<String, String> map = toMap(line);
        if(map==null){
            throw new IllegalStateException("failure: line hasn't been converted into map; map is null");
        }
        List<String> notFoundHeaders = Lists.newArrayList();
        for (String header : headers) {
            if (!map.containsKey(header)) {
                notFoundHeaders.add(header);
            }else {
                reorderedLine.add(map.get(header));
            }
        }
        if(!notFoundHeaders.isEmpty()){
            String message = Joiner.on(',').join(notFoundHeaders);
            throw new IllegalArgumentException("les headers '" + message + "' ne sont pas trouvés");
        }
        return reorderedLine;
    }

    protected abstract Map<String, String> toMap(final Line line);
}
