package fixtures.common.database.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.util.List;

import com.google.common.collect.Lists;
import cucumber.table.DataTable;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class FileToDatatableTest {
    @Test
    public void testConstructor() throws Exception {
        Constructor constructor = FileToDatatable.class.getDeclaredConstructor();
        // on vérifie que le contructeur est volontairement inacessible
        assertThat(constructor.isAccessible(), CoreMatchers.is(false));

        // pas utile au final, mais la couverture du constructeur est prise en compte
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    // =============================================================================================
    // test convert
    // =============================================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testConvert_no_api_data_ok() throws Exception {
        String api = null;
        String data = "ok";
        FileToDatatable.convert(api, data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvert_api_ok_no_data() throws Exception {
        String api = "ok";
        String data = null;
        FileToDatatable.convert(api, data);
    }

    @Test(expected = FileNotFoundException.class)
    public void testConvert_file_not_found() throws Exception {
        String api = "not";
        String data = "found";
        FileToDatatable.convert(api, data);
    }

    @Test
    public void testConvert_nominalCase() throws Exception {
        String api = "files";
        String data = "insertion_donnees";
        // les données sont extraites du fichier : cucumber-jvm-fixtures/src/test/resources/files/insert/insertion_donnees.txt
        DataTable convert = FileToDatatable.convert(api, data);
        //
        List<List<String>> expected = Lists
                .<List<String>>newArrayList(Lists.<String>newArrayList("id", "nom", "email", "numéro"),
                        Lists.<String>newArrayList("1587", "bob", "bob@bob.com", "157"),
                        Lists.<String>newArrayList("9595", "connor", "connor@kent.com", "2"),
                        Lists.<String>newArrayList("1234", "invisible", "susan@storm.com", "1"));
        assertThat(convert.raw(), is(expected));
    }

    @Test
    public void testConvert_nominalCase_data_with_case_and_accents() throws Exception {
        String api = "files";
        String data = "Insertion données";
        // les données sont extraites du fichier : cucumber-jvm-fixtures/src/test/resources/files/insert/insertion_donnees.txt
        DataTable convert = FileToDatatable.convert(api, data);
        //
        List<List<String>> expected = Lists
                .<List<String>>newArrayList(Lists.<String>newArrayList("id", "nom", "email", "numéro"),
                        Lists.<String>newArrayList("1587", "bob", "bob@bob.com", "157"),
                        Lists.<String>newArrayList("9595", "connor", "connor@kent.com", "2"),
                        Lists.<String>newArrayList("1234", "invisible", "susan@storm.com", "1"));
        assertThat(convert.raw(), is(expected));
    }

    @Test
    public void testConvert_nominalCase_data_empty_lines_in_files() throws Exception {
        String api = "files";
        String data = "données avec des lignes vides";
        DataTable convert = FileToDatatable.convert(api, data);
        //
        List<List<String>> expected = Lists
                .<List<String>>newArrayList(Lists.<String>newArrayList("id", "nom", "email", "numéro"),
                        Lists.<String>newArrayList("1587", "bob", "bob@bob.com", "157"),
                        Lists.<String>newArrayList("9595", "tim", "tim@drake.com", "3"));
        assertThat(convert.raw(), is(expected));
    }

    @Test
    public void testConvert_nominalCase_data_bad_filled() throws Exception {
        String api = "files";
        String data = "donnees_mal_remplies";
        DataTable convert = FileToDatatable.convert(api, data);
        // même si il manque des colonnes on remplit tout de même la datatable
        List<List<String>> expected = Lists
                .<List<String>>newArrayList(Lists.<String>newArrayList("id", "nom", "email", "numéro"),
                        Lists.<String>newArrayList("1587", "bob", "bob@bob.com", "157"),
                        Lists.<String>newArrayList("9595", "broken", "broken@arrow.com"));
        assertThat(convert.raw(), is(expected));
    }
}
