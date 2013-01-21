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
    public void testMonth_input_has_many_var() throws Exception {
        String input = "ma date ${now} et ${now +5} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedDate = DateTime.now().toString(Now.DEFAULT_PATTERN);
        String expectedDate2 = DateTime.now().plusDays(5).toString(Now.DEFAULT_PATTERN);
        //
        assertThat(result, is("ma date " + expectedDate + " et "+expectedDate2+ " affichée"));
    }

	@Test
    public void testMonth_input_has_many_date_with_a_bad_one() throws Exception {
        String input = "ma date ${now -2m}, ${now +5 +6} et ${now (yy/MM-EEEE) +5} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedDate = DateTime.now().plusMonths(-2).toString(Now.DEFAULT_PATTERN);
        String expectedDate2 = DateTime.now().plusDays(5).toString("yy/MM-EEEE");
        //
        assertThat(result, is("ma date " + expectedDate + ", $bad{now +5 +6} et "+expectedDate2+ " affichée"));
    }

	// day

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
    public void testMonth_input_has_no_var_just_pattern_02() throws Exception {
        String input = "ma date ${now (yyyy/MM/dd hh:mm)} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().toString("yyyy/MM/dd hh:mm");
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }

    @Test
    public void testMonth_input_has_no_var_just_pattern_many() throws Exception {
        String input = "ma date ${now (yyyy/MM/dd hh:mm)(yyyy/MM/dd)} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().toString("yyyy/MM/dd hh:mm");
        //
        assertThat(result, is("ma date $bad{now (yyyy/MM/dd hh:mm)(yyyy/MM/dd)} affichée"));
    }

    @Test
    public void testMonth_input_has_no_var_just_pattern_many_02() throws Exception {
        String input = "ma date ${now (yyyy/MM/dd hh:mm)(yyyy/MM/dd} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().toString("yyyy/MM/dd hh:mm");
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }

    @Test
    public void testMonth_input_has_no_var_many_date_pattern() throws Exception {
        String input = "ma date ${now (yyyy/MM/dd-hh:mm) (yyyy)} affichée";
        String expected = "ma date $bad{now (yyyy/MM/dd-hh:mm) (yyyy)} affichée";
        Now now = new Now();
        String result = now.apply(input);
        //
        assertThat(result, is(expected));
    }

    @Test
    public void testMonth_input_has_no_var_badd_pattern() throws Exception {
        String input = "ma date ${now (lololo)} affichée";
        String expected = "ma date $bad{now (lololo)} affichée";
        Now now = new Now();
        String result = now.apply(input);
        //
        assertThat(result, is(expected));
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
        String expected = "ma date $bad{now +2 -1} affichée";
        Now now = new Now();
        String result = now.apply(input);
        //
        assertThat(result, is(expected));
    }

    @Test
    public void testMonth_input_has_var_plus_two_month_without_space() throws Exception {
        String input = "ma date ${now+2} affichée";
        String expected = "ma date $bad{now+2} affichée";
        Now now = new Now();
        String result = now.apply(input);
        //
        assertThat(result, is(expected));
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

	// month

	@Test
    public void testMonth_input_add_month() throws Exception {
        String input = "ma date ${now +2m} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().plusMonths(2).toString(Now.DEFAULT_PATTERN);
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }


	@Test
    public void testMonth_input_minus_month() throws Exception {
        String input = "ma date ${now -3m} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().minusMonths(3).toString(Now.DEFAULT_PATTERN);
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }



	@Test
    public void testMonth_input_add_month_and_pattern() throws Exception {
        String input = "ma date ${now (yyyy/MM/dd-hh:mm) +3m} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().plusMonths(3).toString("yyyy/MM/dd-hh:mm");
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }


	@Test
    public void testMonth_input_add_month_day_and_pattern() throws Exception {
        String input = "ma date ${now (yyyy/MM/dd-hh:mm) +25 +3m} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().plusDays(25).plusMonths(3).toString("yyyy/MM/dd-hh:mm");
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }

	@Test
    public void testMonth_input_add_month_day_and_pattern_different_order() throws Exception {
        String input = "ma date ${now +5m -26 (yyyy/MM/dd-mm)} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().plusDays(-26).plusMonths(5).toString("yyyy/MM/dd-mm");
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }

	@Test
    public void testMonth_input_add_month_in_upper_case() throws Exception {
        String input = "ma date ${now +5M (yyyy/MM/dd-mm)} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().plusMonths(5).toString("yyyy/MM/dd-mm");
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }

	@Test
    public void testMonth_input_has_two_month_pattern() throws Exception {
        String input = "ma date $bad{now +2m -1m} affichée";
        String expected = "ma date $bad{now +2m -1m} affichée";
        Now now = new Now();
        String result = now.apply(input);
        //
        assertThat(result, is(expected));
    }

	// year

	@Test
    public void testMonth_input_add_year() throws Exception {
        String input = "ma date ${now +2y} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().plusYears(2).toString(Now.DEFAULT_PATTERN);
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }


	@Test
    public void testMonth_input_minus_year() throws Exception {
        String input = "ma date ${now -3y} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().minusYears(3).toString(Now.DEFAULT_PATTERN);
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }



	@Test
    public void testMonth_input_add_year_and_pattern() throws Exception {
        String input = "ma date ${now (yyyy/MM/dd-hh:mm) +3y} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().plusYears(3).toString("yyyy/MM/dd-hh:mm");
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }


	@Test
    public void testMonth_input_add_year_day_and_pattern() throws Exception {
        String input = "ma date ${now (yyyy/MM/dd-hh:mm) +25 +3y} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().plusDays(25).plusYears(3).toString("yyyy/MM/dd-hh:mm");
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }

	@Test
    public void testMonth_input_add_year_day_and_pattern_different_order() throws Exception {
        String input = "ma date ${now +5y -26 (yyyy/MM/dd-mm)} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().plusDays(-26).plusYears(5).toString("yyyy/MM/dd-mm");
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }

	@Test
    public void testMonth_input_add_year_in_upper_case() throws Exception {
        String input = "ma date ${now +5Y (yyyy/MM/dd-mm)} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().plusYears(5).toString("yyyy/MM/dd-mm");
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }

	@Test
    public void testMonth_input_add_year_month_day_in_upper_case() throws Exception {
        String input = "ma date ${now +5Y +254 -5m (yyyy/MM/dd-mm)} affichée";
        Now now = new Now();
        String result = now.apply(input);
        String expectedMonth = DateTime.now().plusDays(254).plusMonths(-5).plusYears(5).toString("yyyy/MM/dd-mm");
        //
        assertThat(result, is("ma date " + expectedMonth + " affichée"));
    }

	@Test
    public void testMonth_input_has_two_year_pattern() throws Exception {
        String input = "ma date $bad{now +2y -1y} affichée";
        String expected = "ma date $bad{now +2y -1y} affichée";
        Now now = new Now();
        String result = now.apply(input);
        //
        assertThat(result, is(expected));
    }
}
