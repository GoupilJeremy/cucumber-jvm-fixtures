package fixtures.common.transformers;

import com.google.common.collect.Lists;
import com.google.common.io.LineProcessor;
import cucumber.api.DataTable;
import cucumber.runtime.CucumberException;
import cucumber.runtime.table.TableConverter;
import cucumber.runtime.xstream.LocalizedXStreams;
import gherkin.formatter.model.Comment;
import gherkin.formatter.model.DataTableRow;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class FileTransformerTest {
    private DataTable dataTable;

    @Before
    public void setUp() throws Exception {
        List<String> dataTableToCompareHeader = Lists.newArrayList("type de bien", "nombre de pieces");
        List<String> data = Lists.newArrayList("appartement", "5");
        List<DataTableRow> rows = Lists
                .newArrayList(new DataTableRow(new ArrayList<Comment>(), dataTableToCompareHeader, 1),
                        new DataTableRow(new ArrayList<Comment>(), data, 2));
        TableConverter tableConverter = new TableConverter(
                new LocalizedXStreams(Thread.currentThread().getContextClassLoader()).get(Locale.getDefault()), null);
        dataTable = new DataTable(rows, tableConverter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileTransformer_file_null() throws Exception {

        File file = null;
        //
        FileTransformer fileTransformer = new FileTransformer(dataTable);
        fileTransformer.toDataTable(file);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileTransformer_file_doesnt_exist() throws Exception {

        File file = new File("unknow.jstl");
        //
        FileTransformer fileTransformer = new FileTransformer(dataTable);
        fileTransformer.toDataTable(file);
    }

    @Test
    public void testFileTransformer_file_exist() throws Exception {

        File file = new File(getClass().getClassLoader().getResource("files/file_to_transform.txt").getFile());
        //
        FileTransformer fileTransformer = new FileTransformer(dataTable);
        final DataTable dataTableFromFile = fileTransformer.toDataTable(file);
        List<List<String>> raw = dataTableFromFile.raw();
        //
        List<List<String>> expected = Lists.<List<String>>newArrayList(Lists.<String>newArrayList("ID", "FILE", "REF"),
                Lists.<String>newArrayList("12", "fs.sys", "1878D"),
                Lists.<String>newArrayList("478", "read.txt", "2574"));
        assertThat(raw, is(expected));
    }

    @Test
    public void testFileTransformer_file_exist_with_no_tab_separator() throws Exception {

        File file = new File(getClass().getClassLoader().getResource("files/file_to_transform_no_tab.txt").getFile());
        //
        FileTransformer fileTransformer = new FileTransformer(dataTable);
        final DataTable dataTableFromFile = fileTransformer.toDataTable(file);
        List<List<String>> raw = dataTableFromFile.raw();
        //
        List<List<String>> expected = Lists.<List<String>>newArrayList(Lists.<String>newArrayList("ID  FILE     REF"),
                Lists.<String>newArrayList("1   fs.sys   878D"), Lists.<String>newArrayList("478 read.txt 2574"));
        assertThat(raw, is(expected));
    }

    @Test(expected = CucumberException.class)
    public void testFileTransformer_throw_IOException() throws Exception {

        File file = new File(getClass().getClassLoader().getResource("files/file_to_transform.txt").getFile());
        LineProcessor<List<DataTableRow>> lineProcessor = mock(LineProcessor.class);
        //
        when(lineProcessor.processLine(anyString())).thenThrow(new IOException("fake exception"));
        //
        FileTransformer fileTransformer = new FileTransformer(dataTable);
        FileTransformer spyFileTransformer = spy(fileTransformer);
        //
        doReturn(lineProcessor).when(spyFileTransformer).newLineProcessor();
        //
        final DataTable dataTableFromFile = spyFileTransformer.toDataTable(file);
        dataTableFromFile.raw();
    }

    // sort

    @Test
    public void test_fileTransformer_column_is_null() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("files/file_to_transform_to_sort.txt").getFile());
        //
        String column = null;
        FileTransformer fileTransformer = new FileTransformer(dataTable, column);
        final DataTable dataTableFromFile = fileTransformer.toDataTable(file);
        List<List<String>> raw = dataTableFromFile.raw();
        //ID, FILE, REF], [478, read.txt, 2574], [12, fs.sys, 1878D], [321 .git    48717

        List<List<String>> expected = Lists.<List<String>>newArrayList( //
                Lists.<String>newArrayList("ID", "FILE", "REF"), //
                Lists.<String>newArrayList("478", "read.txt", "2574"),//
                Lists.<String>newArrayList("12", "fs.sys", "1878D"),//
                Lists.<String>newArrayList("321", ".git", "48717"));
        assertThat(raw, is(expected));
    }

    @Test
    public void test_fileTransformer_column_is_unknow() throws Exception {

        File file = new File(getClass().getClassLoader().getResource("files/file_to_transform_to_sort.txt").getFile());
        //
        String column = "unknown";
        FileTransformer fileTransformer = new FileTransformer(dataTable, column);
        final DataTable dataTableFromFile = fileTransformer.toDataTable(file);
        List<List<String>> raw = dataTableFromFile.raw();
        //ID, FILE, REF], [478, read.txt, 2574], [12, fs.sys, 1878D], [321 .git    48717

        List<List<String>> expected = Lists.<List<String>>newArrayList( //
                Lists.<String>newArrayList("ID", "FILE", "REF"), //
                Lists.<String>newArrayList("478", "read.txt", "2574"),//
                Lists.<String>newArrayList("12", "fs.sys", "1878D"),//
                Lists.<String>newArrayList("321", ".git", "48717"));
        assertThat(raw, is(expected));
    }

    @Test
    public void test_fileTransformer_column_is_ok() throws Exception {

        File file = new File(getClass().getClassLoader().getResource("files/file_to_transform_to_sort.txt").getFile());
        //
        String column = "ID";
        FileTransformer fileTransformer = new FileTransformer(dataTable, column);
        final DataTable dataTableFromFile = fileTransformer.toDataTable(file);
        List<List<String>> raw = dataTableFromFile.raw();
        //ID, FILE, REF], [478, read.txt, 2574], [12, fs.sys, 1878D], [321 .git    48717

        List<List<String>> expected = Lists.<List<String>>newArrayList( //
                Lists.<String>newArrayList("ID", "FILE", "REF"), //
                Lists.<String>newArrayList("12", "fs.sys", "1878D"),//
                Lists.<String>newArrayList("321", ".git", "48717"),//
                Lists.<String>newArrayList("478", "read.txt", "2574"));
        assertThat(raw, is(expected));
    }
}
