package fixtures.common.transformers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import cucumber.runtime.CucumberException;
import cucumber.table.DataTable;
import gherkin.formatter.model.Comment;
import gherkin.formatter.model.DataTableRow;

public class FileTransformer extends AbstractDataTableTransformer<File> {
    private static final Charset CHARSET = Charset.forName("ISO-8859-15");

    private static final String SEPARATOR = "\t";

    public FileTransformer(DataTable dataTable) {
        super(dataTable);
    }

    @Override
    protected List<DataTableRow> buildRowForDataTable(final File file, final List<DataTableRow> rows) {
        Preconditions.checkArgument(file != null, "le fichier ne peut ëtre null");
        Preconditions.checkArgument(file.exists(), "le fichier doit exister");
        final List<DataTableRow> dataTableRows;
        try {
            dataTableRows = Files.readLines(file, CHARSET, newLineProcessor());
        } catch (IOException e) {
            throw new CucumberException("Test cucumber", e);
        }

        return dataTableRows;
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
            final Iterable<String> cells = splitter.split(line);
            DataTableRow dataTableRow = new DataTableRow(new ArrayList<Comment>(), Lists.newArrayList(cells), counter);
            dataTableRows.add(dataTableRow);
            counter++;
            return true;
        }

        @Override
        public List<DataTableRow> getResult() {
            return dataTableRows;
        }
    };
}
