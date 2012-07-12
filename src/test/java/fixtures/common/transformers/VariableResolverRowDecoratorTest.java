package fixtures.common.transformers;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import gherkin.formatter.model.Comment;
import gherkin.formatter.model.DataTableRow;
import gherkin.formatter.model.Row;
import org.joda.time.DateTimeUtils;
import org.junit.Test;

public class VariableResolverRowDecoratorTest {
    private static final int FIXED_TIME = 100;

    @Test
    public void test_check_nb_transformer() throws Exception {
        //when
        List<Function<String, String>> functions = VariableResolverStringDecorator.getFunctions(Maps.<String, String>newHashMap());
        //then
        // SI on change la taille, AJOUTER LES TESTS ASSOCIES A LA FONCTION
        assertThat(functions.size(), is(8));
    }

    @Test
    public void test_check_getDiffType() throws Exception {
        //given
        List<String> strings = Arrays.asList("1", "2", "${moisPrecedent}");
        DataTableRow row = new DataTableRow(Lists.<Comment>newArrayList(), strings, 0);
        DateTimeUtils.setCurrentMillisFixed(FIXED_TIME);
        //when
        Row.DiffType diffType = new VariableResolverRowDecorator(row, Maps.<String, String>newHashMap()).getDiffType();
        //then
        assertThat(diffType, is(Row.DiffType.NONE));
    }

    /**
     * Le NPE est levé: on ne change pas le comportement de la datatable.
     *
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void test_resolve_variables_with_null_list() throws Exception {
        //given
        DataTableRow row = new DataTableRow(Lists.<Comment>newArrayList(), null, 0);
    }


    // =================================================================================================================

    @Test
    public void test_resolve_variables_with_mois_precedent() throws Exception {
        //given
        DataTableRow row = new DataTableRow(Lists.<Comment>newArrayList(), Arrays.asList("1", "2", "${moisPrecedent}"),
                0);
        DateTimeUtils.setCurrentMillisFixed(FIXED_TIME);

        //when
        final List<String> cells = new VariableResolverRowDecorator(row, Maps.<String, String>newHashMap()).getCells();
        //then
        List<String> expected = Arrays.asList("1", "2", "décembre");
        assertThat(cells, is(expected));
    }

    @Test
    public void test_resolve_variables_with_no_variables() throws Exception {
        //given
        DataTableRow row = new DataTableRow(Lists.<Comment>newArrayList(), Arrays.asList("1", "2", "3"), 0);
        DateTimeUtils.setCurrentMillisFixed(FIXED_TIME);
        //when
        final List<String> cells = new VariableResolverRowDecorator(row, Maps.<String, String>newHashMap()).getCells();
        //then
        List<String> expected = Arrays.asList("1", "2", "3");
        assertThat(cells, is(expected));
    }

    @Test
    public void test_resolve_variables_with_annee_du_mois_precedent() throws Exception {
        //given
        DataTableRow row = new DataTableRow(Lists.<Comment>newArrayList(),
                Arrays.asList("1", "2", "${anneeDuMoisPrecedent}"), 0);
        DateTimeUtils.setCurrentMillisFixed(FIXED_TIME);
        //when
        final List<String> cells = new VariableResolverRowDecorator(row, Maps.<String, String>newHashMap()).getCells();
        //then
        List<String> expected = Arrays.asList("1", "2", "1969");
        assertThat(cells, is(expected));
    }

    @Test
    public void test_resolve_variables_with_empty_list() throws Exception {
        //given
        DataTableRow row = new DataTableRow(Lists.<Comment>newArrayList(), Lists.<String>newArrayList(), 0);
        DateTimeUtils.setCurrentMillisFixed(FIXED_TIME);
        //when
        final List<String> cells = new VariableResolverRowDecorator(row, Maps.<String, String>newHashMap()).getCells();
        //then
        List<String> expected = Lists.newArrayList();
        assertThat(cells, is(expected));
    }

    // =================================================================================================================

    @Test
    public void test_resolve_variables_with_annee_du_mois_precedent_and_mois_precedent() throws Exception {
        //given
        DataTableRow row = new DataTableRow(Lists.<Comment>newArrayList(),
                Arrays.asList("1", "${moisPrecedent}", "${anneeDuMoisPrecedent}"), 0);
        DateTimeUtils.setCurrentMillisFixed(FIXED_TIME);
        //when
        final List<String> cells = new VariableResolverRowDecorator(row, Maps.<String, String>newHashMap()).getCells();
        //then
        List<String> expected = Arrays.asList("1", "décembre", "1969");
        assertThat(cells, is(expected));
    }

    // =================================================================================================================

    @Test
    public void test_resolve_variables_with_mois_precedent_et_annee() throws Exception {
        //given
        DataTableRow row = new DataTableRow(Lists.<Comment>newArrayList(),
                Arrays.asList("1", "${moisPrecedent}", "${moisPrecedentEtAnnee}"), 0);
        DateTimeUtils.setCurrentMillisFixed(FIXED_TIME);
        //when
        final List<String> cells = new VariableResolverRowDecorator(row, Maps.<String, String>newHashMap()).getCells();
        //then
        List<String> expected = Arrays.asList("1", "décembre", "Décembre 1969");
        assertThat(cells, is(expected));
    }

    @Test
    public void test_resolve_variables_with_id_in_map() throws Exception {
        //given
        DataTableRow row = new DataTableRow(Lists.<Comment>newArrayList(),
                Arrays.asList("1", "LP-1", "${moisPrecedentEtAnnee}"), 0);
        DateTimeUtils.setCurrentMillisFixed(FIXED_TIME);
        //when
        final HashMap<String, String> context = Maps.newHashMap();
        context.put("LP-1", "1323-23");
        final List<String> cells = new VariableResolverRowDecorator(row, context).getCells();
        //then
        List<String> expected = Arrays.asList("1", "1323-23", "Décembre 1969");
        assertThat(cells, is(expected));
    }

    // =================================================================================================================

    @Test
    public void test_resolve_variables_PreviousMonthUpper() throws Exception {
        //given
        DataTableRow row = new DataTableRow(Lists.<Comment>newArrayList(),
                Arrays.asList("1", "LP-1", "${moisPrecedentMajuscule}"), 0);
        DateTimeUtils.setCurrentMillisFixed(FIXED_TIME);
        //when
        final HashMap<String, String> context = Maps.newHashMap();
        context.put("LP-1", "1323-23");
        final List<String> cells = new VariableResolverRowDecorator(row, context).getCells();
        //then
        List<String> expected = Arrays.asList("1", "1323-23", "DÉCEMBRE");
        assertThat(cells, is(expected));
    }

    // =================================================================================================================

    @Test
    public void test_resolve_variables_LocalDateCurrentMonthAndYearInContext() throws Exception {
        //given
        DataTableRow row = new DataTableRow(Lists.<Comment>newArrayList(), Arrays.asList("1", "LP-1", "yyyy-mm-25"), 0);
        DateTimeUtils.setCurrentMillisFixed(FIXED_TIME);
        //when
        final HashMap<String, String> context = Maps.newHashMap();
        context.put("currentMonth", "09");
        context.put("currentYear", "1977");
        final List<String> cells = new VariableResolverRowDecorator(row, context).getCells();
        //then
        List<String> expected = Arrays.asList("1", "LP-1", "1977-09-25");
        assertThat(cells, is(expected));
    }

    // =================================================================================================================

    @Test
    public void test_resolve_variables_NumericMonth() throws Exception {
        //given
        DataTableRow row = new DataTableRow(Lists.<Comment>newArrayList(), Arrays.asList("1", "LP-1", "12-${moisNumeric}"), 0);
        DateTimeUtils.setCurrentMillisFixed(FIXED_TIME);
        //when
        final HashMap<String, String> context = Maps.newHashMap();
        context.put("currentMonth", "09");
        context.put("currentYear", "1977");
        final List<String> cells = new VariableResolverRowDecorator(row, context).getCells();
        //then
        List<String> expected = Arrays.asList("1", "LP-1", "12-01");
        assertThat(cells, is(expected));
    }

    // =================================================================================================================

    @Test
    public void test_get_cells() throws Exception {
        //given

        DataTableRow row = new DataTableRow(Lists.<Comment>newArrayList(),
                Arrays.asList("le mois ${moisPrecedent}", "le mois ${anneeDuMoisPrecedent}", "ok pas de variables $1"),
                0);
        // on fixe la date avant le test
        DateTimeUtils.setCurrentMillisFixed(FIXED_TIME);
        //when

        final List<String> cellsDecorated = new VariableResolverRowDecorator(row, Maps.<String, String>newHashMap())
                .getCells();

        //then
        assertThat(cellsDecorated, is(Arrays.asList("le mois décembre", "le mois 1969", "ok pas de variables $1")));
    }
}
