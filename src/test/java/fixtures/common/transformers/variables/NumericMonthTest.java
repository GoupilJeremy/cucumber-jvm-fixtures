package fixtures.common.transformers.variables;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.joda.time.DateTime;
import org.junit.Test;

public class NumericMonthTest {
    @Test
    public void testMonth_null_input() throws Exception {
        String input = null;
        NumericMonth numericMonth = new NumericMonth();
        String result = numericMonth.apply(input);
        //
        assertThat(result, is(""));
    }

    @Test
    public void testMonth_input_no_var() throws Exception {
        String input = "2012-02";
        NumericMonth numericMonth = new NumericMonth();
        String result = numericMonth.apply(input);
        //
        assertThat(result, is("2012-02"));
    }

    @Test
    public void testMonth_input_has_var() throws Exception {
        String input = "2012-${moisNumeric}";
        NumericMonth numericMonth = new NumericMonth();
        String result = numericMonth.apply(input);
        String expectedMonth = DateTime.now().toString("MM");
        //
        assertThat(result, is("2012-" + expectedMonth));
    }

    @Test
    public void testMonth_input_has_var_minus_one_month() throws Exception {
        String input = "2012-${moisNumeric -1}";
        NumericMonth numericMonth = new NumericMonth();
        String result = numericMonth.apply(input);
        String expectedMonth = DateTime.now().minusMonths(1).toString("MM");
        //
        assertThat(result, is("2012-" + expectedMonth));
    }

    @Test
    public void testMonth_input_has_var_plus_two_month() throws Exception {
        String input = "2012-${moisNumeric +2}";
        NumericMonth numericMonth = new NumericMonth();
        String result = numericMonth.apply(input);
        String expectedMonth = DateTime.now().plusMonths(2).toString("MM");
        //
        assertThat(result, is("2012-" + expectedMonth));
    }

     @Test
    public void testMonth_input_has_var_plus_two_month_many_time() throws Exception {
        String input = "2012-${moisNumeric +2 -1}";
        NumericMonth numericMonth = new NumericMonth();
        String result = numericMonth.apply(input);
        //
        assertThat(result, is(input));
    }

    @Test
    public void testMonth_input_has_var_plus_two_month_without_space() throws Exception {
        String input = "2012-${moisNumeric+2}";
        NumericMonth numericMonth = new NumericMonth();
        String result = numericMonth.apply(input);
        //
        assertThat(result, is(input));
    }

    @Test
    public void testMonth_input_has_var_plus_two_month_many_space() throws Exception {
        String input = "2012-${moisNumeric   +2}";
        NumericMonth numericMonth = new NumericMonth();
        String result = numericMonth.apply(input);
        //
        assertThat(result, is(input));
    }

    @Test
    public void testMonth_input_has_var_plus_two_month_many_operator() throws Exception {
        String input = "2012-${moisNumeric -+2}";
        NumericMonth numericMonth = new NumericMonth();
        String result = numericMonth.apply(input);
        //
        assertThat(result, is(input));
    }

    @Test
    public void testMonth_input_has_var_missing_operator() throws Exception {
        String input = "2012-${moisNumeric 2}";
        NumericMonth numericMonth = new NumericMonth();
        String result = numericMonth.apply(input);
        //
        assertThat(result, is(input));
    }

    @Test
    public void testMonth_input_has_var_bad_numeric() throws Exception {
        String input = "2012-${moisNumeric dfdsf}";
        NumericMonth numericMonth = new NumericMonth();
        String result = numericMonth.apply(input);
        //
        assertThat(result, is(input));
    }
}
