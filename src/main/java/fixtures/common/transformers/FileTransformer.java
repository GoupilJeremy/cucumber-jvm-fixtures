package fixtures.common.transformers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import cucumber.api.DataTable;
import cucumber.runtime.CucumberException;
import gherkin.formatter.model.Comment;
import gherkin.formatter.model.DataTableRow;
import org.apache.commons.lang.NotImplementedException;

public class FileTransformer extends AbstractDataTableBuilder<String> {
    private static final Charset CHARSET = Charset.forName("ISO-8859-15");

    private static final String SEPARATOR = "\t";

    private String column;

    private FileTransformer(final DataTable dataTableFromFeatureFileToCompare,
            final List<String> collection) {
        super(dataTableFromFeatureFileToCompare, collection);
    }


    protected Map<String, String> apply(final DataTableRow object) {
        HashMap<String,String> map = Maps.newHashMap();
        List<String> cells = object.getCells();

        for (int i = 0; i <cells.size(); i++) {
            String head = headersAsCells.get(i);
            String value = cells.get(i);
            map.put(head,value);
        }
        return map;
    }

    public static FileTransformer from(DataTable dataTable, String column, File file) {

        Preconditions.checkArgument(file != null, "le fichier ne peut etre null");
        Preconditions.checkArgument(file.exists(), "le fichier doit exister");
        final List<String> dataTableRows;
        try {
            dataTableRows = Files.readLines(file, CHARSET, newLineProcessor());
        } catch (IOException e) {
            throw new CucumberException("Test cucumber", e);
        }
        FileTransformer fileTransformer = new FileTransformer(dataTable, dataTableRows);
        fileTransformer.setColumn(column);
        return fileTransformer;

    }

    @Override
    protected List<DataTableRow> buildRowForDataTable(final List<String> dataTableRows,
            final List<DataTableRow> rows) {



        if (dataTableRows != null && !dataTableRows.isEmpty() && !Strings.isNullOrEmpty(column)) {
           return super.buildRowForDataTable(dataTableRows, rows);
        }

        return Lists.newArrayList();
    }

    @Override
    protected Comparator<String> getComparator() {
        return new DataTableRowComparator(column, headersAsCells);
    }

    @Override
    protected Map<String, String> apply(final String object) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected static LineProcessor<List<String>> newLineProcessor() {
        return new InnerLineProcessor();
    }

    protected void setColumn(final String column) {
        this.column = column;
    }

    // =============================================================================================
    // inner class
    // =============================================================================================

    private static class InnerLineProcessor implements LineProcessor<List<String>> {
        private  List<String> cells= new ArrayList<String>();


        @Override
        public boolean processLine(final String line) throws IOException {
            final Splitter splitter = Splitter.on(SEPARATOR);
            ArrayList<String> strings = Lists.newArrayList(splitter.split(Label.cleanLabel(line)));
            cells.addAll(strings);

            return true;
        }

        @Override
        public List<String> getResult() {
            return cells;
        }
    }

    private static class DataTableRowComparator implements Comparator<String>, Serializable {
        private static final long serialVersionUID = 1L;

        private String sortColumnName;

        private int index;

        public DataTableRowComparator(final String sortColumnName, final List<String> headersAsCells) {
            this.sortColumnName = sortColumnName;
            this.index = headersAsCells.indexOf(sortColumnName);
        }

        @Override
        public int compare(final String line01, final String line02) {
            if (index < 0) {
                return 0;
            }

            final Splitter splitter = Splitter.on(SEPARATOR);
            List<String> split = Lists.newArrayList(splitter.split(line01));
            String cell01 = split.get(index);
            List<String> split2 = Lists.newArrayList(splitter.split(line02));
            String cell02 = split2.get(index);
            // la colonne avec le header reste en premier
            if (cell01.equals(sortColumnName)) {
                return -1;
            }
            return cell01.compareTo(cell02);
        }
    }
}
