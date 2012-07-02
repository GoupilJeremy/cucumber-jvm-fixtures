package fixtures.common.transformers;

import java.util.ArrayList;
import java.util.List;

import cucumber.table.DataTable;
import gherkin.formatter.model.Comment;
import gherkin.formatter.model.DataTableRow;
import org.apache.commons.lang.Validate;


public abstract class AbstractDataTableTransformer<T> implements IDataTableTransformer<T> {
    protected List<String> headersAsCells;

    private AbstractDataTableTransformer() {
    }

    public AbstractDataTableTransformer(DataTable dataTableFromFeatureFileToCompare) {
        final List<List<String>> raw = dataTableFromFeatureFileToCompare.raw();
        Validate.notEmpty(raw,
                "la datatable doit contenir au moins une ligne contenant les headers,\n afin de connaître lors de la méthode 'transform', quelle propriété de l'objet mettre dans la cellule");
        this.headersAsCells = raw.get(0);
    }

    public DataTable toDataTable(T transform) {
        List<DataTableRow> rows = new ArrayList<DataTableRow>();
        addHeaderInRows(rows);
        return new DataTable(buildRowForDataTable(transform, rows), null);
    }

    private void addHeaderInRows(final List<DataTableRow> rows) {
        rows.add(new DataTableRow(new ArrayList<Comment>(), headersAsCells, 0));
    }

    protected abstract List<DataTableRow> buildRowForDataTable(final T transform,
            List<DataTableRow> rows);
}
