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
    protected List<DataTableRow> buildRowsForDataTable(final List<String> dataTableRows) {

        if (dataTableRows != null && !dataTableRows.isEmpty() && !Strings.isNullOrEmpty(column)) {
           return super.buildRowsForDataTable(dataTableRows);
        }

        return Lists.newArrayList();
    }



    @Override
    protected Map<String, String> toMap(final String line) {
        HashMap<String,String> map = Maps.newHashMap();
        final Splitter splitter = Splitter.on(SEPARATOR);
        List<String> cells = Lists.newArrayList(splitter.split(Label.cleanLabel(line)));

        for (int i = 0; i <cells.size(); i++) {
            String head = headersAsCells.get(i);
            String value = cells.get(i);
            map.put(head,value);
        }
        return map;
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


}
