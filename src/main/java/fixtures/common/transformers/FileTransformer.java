package fixtures.common.transformers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import cucumber.api.DataTable;
import cucumber.runtime.CucumberException;
import gherkin.formatter.model.Comment;
import gherkin.formatter.model.DataTableRow;
import org.apache.commons.lang.NotImplementedException;

public class FileTransformer extends AbstractDataTableTransformer<DataTableRow> {
    private static final Charset CHARSET = Charset.forName("ISO-8859-15");

    private static final String SEPARATOR = "\t";

    private String column;

    public FileTransformer(DataTable dataTable) {
        super(dataTable);
    }

    @Override
    protected Map<String, String> apply(final DataTableRow object) {
        throw new NotImplementedException("this method isn't needed for this implementation");
    }

    public FileTransformer(DataTable dataTable, String column) {
        super(dataTable);
        this.column = Strings.nullToEmpty(column);
    }


    public DataTable toDataTable(File file) {
        Preconditions.checkArgument(file != null, "le fichier ne peut ëtre null");
        Preconditions.checkArgument(file.exists(), "le fichier doit exister");
        final List<DataTableRow> dataTableRows;
                try {
                    dataTableRows = Files.readLines(file, CHARSET, newLineProcessor());
                } catch (IOException e) {
                    throw new CucumberException("Test cucumber", e);
                }
        return toDataTable(dataTableRows);
    }

    @Override
    protected List<DataTableRow> buildRowForDataTable(final Collection<DataTableRow> dataTableRows, final List<DataTableRow> rows) {

        ArrayList<DataTableRow> rowsList = Lists.newArrayList(dataTableRows);

        if (dataTableRows != null && !dataTableRows.isEmpty() && !Strings.isNullOrEmpty(column)) {
            DataTableRowComparator dataTableRowComparator = new DataTableRowComparator(column, rowsList.get(0));
            Collections.sort(rowsList, dataTableRowComparator);
        }

        return rowsList;
    }

    protected LineProcessor<List<DataTableRow>> newLineProcessor() {
        return new InnerLineProcessor();
    }

    // =============================================================================================
    // inner class
    // =============================================================================================

    private static class InnerLineProcessor implements LineProcessor<List<DataTableRow>> {
        private List<DataTableRow> dataTableRows = new ArrayList<DataTableRow>();

        private int counter = 0;

        @Override
        public boolean processLine(final String line) throws IOException {
            final Splitter splitter = Splitter.on(SEPARATOR);
            final Iterable<String> cells = splitter.split(Label.cleanLabel(line));
            DataTableRow dataTableRow = new DataTableRow(new ArrayList<Comment>(), Lists.newArrayList(cells), counter);
            dataTableRows.add(dataTableRow);
            counter++;
            return true;
        }

        @Override
        public List<DataTableRow> getResult() {
            return dataTableRows;
        }
    }

    private static class DataTableRowComparator implements Comparator<DataTableRow>, Serializable {
        private static final long serialVersionUID = 1L;

        private String column;

        private int index;

        public DataTableRowComparator(final String column, final DataTableRow firstRow) {
            this.column = column;
            this.index = firstRow.getCells().indexOf(column);
        }

        @Override
        public int compare(final DataTableRow dataTableRow01, final DataTableRow dataTableRow02) {
            if (index < 0) {
                return 0;
            }
            String cell01 = dataTableRow01.getCells().get(index);
            String cell02 = dataTableRow02.getCells().get(index);
            // la colonne avec le header reste en premier
            if (cell01.equals(column)) {
                return -1;
            }
            return cell01.compareTo(cell02);
        }
    }
}
