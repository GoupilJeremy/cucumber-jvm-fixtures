package fixtures.common.transformers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import cucumber.api.DataTable;
import fixtures.common.mail.MailBean;
import gherkin.formatter.model.Comment;
import gherkin.formatter.model.DataTableRow;
import org.apache.commons.lang.Validate;

public abstract class AbstractDataTableTransformer<T> implements IDataTableTransformer<Collection<T>> {
    protected List<String> headersAsCells;

    private AbstractDataTableTransformer() {
    }

    public AbstractDataTableTransformer(DataTable dataTableFromFeatureFileToCompare) {
        this();
        final List<List<String>> raw = dataTableFromFeatureFileToCompare.raw();
        Validate.notEmpty(raw,
                "la datatable doit contenir au moins une ligne contenant les headers,\n afin de connaître lors de la méthode 'transform', quelle propriété de l'objet mettre dans la cellule");
        this.headersAsCells = raw.get(0);
    }

    public DataTable toDataTable(Collection<T> transform) {
        List<DataTableRow> rows = new ArrayList<DataTableRow>();
        addHeaderInRows(rows);
        return new DataTable(buildRowForDataTable(transform, rows), null);
    }

    private void addHeaderInRows(final List<DataTableRow> rows) {
        rows.add(new DataTableRow(new ArrayList<Comment>(), headersAsCells, 0));
    }

    protected List<DataTableRow> buildRowForDataTable(final Collection<T> objects, final List<DataTableRow> rows) {
        Preconditions.checkArgument(objects != null, "la collection d'objets ne peut être null");

        int line = 0;
        for (T object : objects) {
            List<String> cells = new ArrayList<String>();
            Map<String, String> mailToRow = apply(object);
            for (String headerValue : headersAsCells) {
                if (!mailToRow.containsKey(headerValue)) {
                    throw new IllegalStateException("le header '" + headerValue + "' n'est pas trouvé");
                }
                cells.add(mailToRow.get(headerValue));
            }
            rows.add(new DataTableRow(new ArrayList<Comment>(), cells, line));
            line++;
        }
        return rows;
    }

    protected abstract Map<String, String> apply(final T object);
}
