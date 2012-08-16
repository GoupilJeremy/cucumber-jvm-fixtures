package fixtures.common.transformers.variables;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.joda.time.DateTime;
import org.junit.Test;

public class NowTest {
    @Test
    public void testMonth_null_input() throws Exception {
        String input = null;
        Now now = new Now();
        String result = now.apply(input);
        //
        assertThat(result, is(""));
    }

    @Test
    public void testMonth_input_no_var() throws Exception {
        String input = "2012-02";
        Now now = new Now();
        String result = now.apply(input);
        //
        assertThat(result, is("2012-02"));
    }

    @Test
    public void testMonth_input_has_var() throws Exception {
        String input = "ma date ${now} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().toString(Now.DEFAULT_PATTERN);
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }

    @Test
    public void testMonth_input_has_var_minus_one_month() throws Exception {
        String input = "ma date ${now -1} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().minusDays(1).toString(Now.DEFAULT_PATTERN);
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }

    @Test
    public void testMonth_input_has_var_plus_two_month_with_pattern() throws Exception {
        String input = "ma date ${now +2 (yyyy-MM/dd)} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().plusDays(2).toString("yyyy-MM/dd");
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }


    @Test
    public void testMonth_input_has_var_minus_four_month_with_pattern_different_order() throws Exception {
        String input = "ma date ${now (yyyy-MM/dd) -4 } affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().minusDays(4).toString("yyyy-MM/dd");
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }

        @Test
    public void testMonth_input_has_no_var_just_pattern() throws Exception {
        String input = "ma date ${now (yyyy/MM/dd-hh:mm)} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().toString("yyyy/MM/dd-hh:mm");
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }

	@Test
    public void testMonth_input_has_no_var_many_date_pattern() throws Exception {
        String input = "ma date ${now (yyyy/MM/dd-hh:mm) (yyyy)} affichée";
        Now now = new Now();
        String result = now.apply(input);
        //
        assertThat(result, is(input));
    }

         @Test
    public void testMonth_input_has_no_var_badd_pattern() throws Exception {
        String input = "ma date ${now (lololo)} affichée";
        Now now = new Now();
        String result = now.apply(input);
        //
        assertThat(result, is(input));
    }

    @Test
    public void testMonth_input_has_var_plus_two_month() throws Exception {
        String input = "ma date ${now +2} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().plusDays(2).toString(Now.DEFAULT_PATTERN);
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }

    @Test
    public void testMonth_input_has_var_plus_two_month_many_time() throws Exception {
        String input = "ma date ${now +2 -1} affichée";
        Now now = new Now();
        String result = now.apply(input);
        //
        assertThat(result, is(input));
    }

    @Test
    public void testMonth_input_has_var_plus_two_month_without_space() throws Exception {
        String input = "ma date ${now+2} affichée";
        Now now = new Now();
        String result = now.apply(input);
        //
        assertThat(result, is(input));
    }

    @Test
    public void testMonth_input_has_var_plus_two_month_many_space() throws Exception {
        String input = "ma date ${now   +2} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().plusDays(2).toString(Now.DEFAULT_PATTERN);
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }

    @Test
    public void testMonth_input_has_var_plus_two_month_many_operator() throws Exception {
        String input = "ma date ${now -+2} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().toString(Now.DEFAULT_PATTERN);
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }

    @Test
    public void testMonth_input_has_var_missing_operator() throws Exception {
        String input = "ma date ${now 2} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().toString(Now.DEFAULT_PATTERN);
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }

    @Test
    public void testMonth_input_has_var_bad_numeric() throws Exception {
        String input = "ma date ${now dfdsf} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().toString(Now.DEFAULT_PATTERN);
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }
}
