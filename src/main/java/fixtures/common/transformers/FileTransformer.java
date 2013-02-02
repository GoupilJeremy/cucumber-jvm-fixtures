package fixtures.common.transformers;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import cucumber.api.DataTable;
import cucumber.runtime.CucumberException;
import gherkin.formatter.model.DataTableRow;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        fileTransformer.compareWith(new DataTableRowComparator(column, headersAsCells));
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
    protected Map<String, String> apply(final String object) {
        return null;
    }

    protected static LineProcessor<List<String>> newLineProcessor() {
        return new InnerLineProcessor();
    }

    protected void setColumn(final String column) {
        this.column = column;
    }


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

    private static class DataTableRowComparator extends LineComparator<String> implements Serializable {
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

            String cell01 = getValue(line01);
            String cell02 = getValue(line02);
            // la colonne avec le header reste en premier
            if (cell01.equals(sortColumnName)) {
                return -1;
            }
            return cell01.compareTo(cell02);
        }

        @Override
        protected String getValue(String row) {
            final Splitter splitter = Splitter.on(SEPARATOR);
            List<String> split = Lists.newArrayList(splitter.split(row));
            return split.get(index);
        }
    }
}
