package fixtures.common.transformers;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import cucumber.api.DataTable;
import gherkin.formatter.model.Comment;
import gherkin.formatter.model.DataTableRow;
import org.apache.commons.lang.Validate;

import java.util.*;

public abstract class AbstractDataTableBuilder<Line> implements IDataTableBuilder {
    public static final int HEADERS_INDEX = 0;
    protected static List<String> headersAsCells;

    private List<Line> lines;
    private LineComparator<Line> comparator;

    protected AbstractDataTableBuilder(DataTable dataTableFromFeatureFileToCompare,List<Line> lines){
        this.lines = lines;
               final List<List<String>> raw = dataTableFromFeatureFileToCompare.raw();
               Validate.notEmpty(raw,
                       "la datatable doit contenir au moins une ligne contenant les headers,\n afin de connaître lors de la méthode 'transform', quelle propriété de l'objet mettre dans la cellule");
               this.headersAsCells = raw.get(HEADERS_INDEX);
    }

    public AbstractDataTableBuilder compareWith(LineComparator<Line> comparator){
        this.comparator = comparator;
        return this;
    }

    private List<Line> filterAndReorderLines(List<Line> lines){
        final Collection<List<String>> filteredTable =

                Collections2.transform(lines, new Function<Line, List<String>>() {
                    @Override
                    public List<String> apply(final Line line) {
                        if (line != null) {
                            return reorderLine(headersAsCells, line);
                        } else {
                            return Lists.newArrayList();
                        }
                    }
                });

        List<Line> listFiltered = Lists.newArrayList();
//        List<Line> listFiltered.addAll(filteredTable);
        return listFiltered;
    }

    public DataTable toDataTable() {
        List<DataTableRow> rows = new ArrayList<DataTableRow>();
        addHeaderInRows(rows);
        return new DataTable(buildRowForDataTable(lines, rows), null);
    }

    private void addHeaderInRows(final List<DataTableRow> rows) {
        rows.add(new DataTableRow(new ArrayList<Comment>(), headersAsCells, HEADERS_INDEX));
    }

    protected List<DataTableRow> buildRowForDataTable(final List<Line> lines, final List<DataTableRow> rows) {
        Preconditions.checkArgument(lines != null, "la liste d'objets ne peut être null");
        List<Line> sorted = Lists.newArrayList(lines);

        if(comparator!=null && comparator.getSortColumn()!=null){
            Collections.sort(sorted, comparator);
        }

        int lineNumber = 0;
        for (Line lineContainer : sorted) {
            List<String> cells = new ArrayList<String>();
            Map<String, String> lineAsMap = apply(lineContainer);
            for (String headerValue : headersAsCells) {
                if (!lineAsMap.containsKey(headerValue)) {
                    throw new IllegalStateException("le header '" + headerValue + "' n'est pas trouvé");
                }
                cells.add(lineAsMap.get(headerValue));
            }
            rows.add(new DataTableRow(new ArrayList<Comment>(), cells, lineNumber));
            lineNumber++;
        }
        return rows;
    }

    public AbstractDataTableBuilder sortBy(String column) {
        return  sortBy(column, true);
    }

    public AbstractDataTableBuilder sortBy(String column, boolean asc) {
        if(comparator==null){
            throw new IllegalStateException("comparator has not been set. call 'compareWith' method to set it. ");
        }
        int index = headersAsCells.indexOf(column);
        if (index < 0) {
            throw new IllegalArgumentException("Colonne [" + column + "] inexistante pour le tri");
        }

        comparator.sortBy(column,asc);
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
        Map<String, String> map = apply(line);
        for (String header : headers) {
            reorderedLine.add(map.get(header));
        }
        return reorderedLine;
    }

    protected abstract Map<String, String> apply(final Line line);
}
