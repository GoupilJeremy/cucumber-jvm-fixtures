package fixtures.common.transformers.variables;

import static fixtures.common.transformers.variables.LocalDateCurrentMonthAndYearInContext.CURRENT_MONTH;
import static fixtures.common.transformers.variables.LocalDateCurrentMonthAndYearInContext.CURRENT_YEAR;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Map;

import com.google.common.collect.Maps;
import org.junit.Test;

public class LocalDateCurrentMonthAndYearInContextTest {
    @Test
    public void test_context_is_null() throws Exception {
        Map<String, String> context = null;
        String input = "yyyy-mm-02";
        LocalDateCurrentMonthAndYearInContext currentMonthAndYearInContext = new LocalDateCurrentMonthAndYearInContext(
                context);
        String returned = currentMonthAndYearInContext.apply(input);
        //
        assertThat(returned, is(input));
    }

    @Test
    public void test_context_has_not_currentMonth_and_currentYear() throws Exception {
        Map<String, String> context = Maps.newHashMap();
        String input = "yyyy-mm-02";
        LocalDateCurrentMonthAndYearInContext currentMonthAndYearInContext = new LocalDateCurrentMonthAndYearInContext(
                context);
        String returned = currentMonthAndYearInContext.apply(input);
        //
        assertThat(returned, is(input));
    }

    @Test
    public void test_context_has_currentMonth_and_not_currentYear() throws Exception {
        Map<String, String> context = Maps.newHashMap();
        context.put(CURRENT_MONTH, "02");
        String input = "yyyy-mm-02";
        LocalDateCurrentMonthAndYearInContext currentMonthAndYearInContext = new LocalDateCurrentMonthAndYearInContext(
                context);
        String returned = currentMonthAndYearInContext.apply(input);
        //
        assertThat(returned, is(input));
    }

    @Test
    public void test_context_has_not_currentMonth_and_has_currentYear() throws Exception {
        Map<String, String> context = Maps.newHashMap();
        context.put(CURRENT_YEAR, "2012");
        String input = "yyyy-mm-02";
        LocalDateCurrentMonthAndYearInContext currentMonthAndYearInContext = new LocalDateCurrentMonthAndYearInContext(
                context);
        String returned = currentMonthAndYearInContext.apply(input);
        //
        assertThat(returned, is(input));
    }

    @Test
    public void test_context_ok_input_null() throws Exception {
        Map<String, String> context = Maps.newHashMap();
        context.put(CURRENT_MONTH, "02");
        context.put(CURRENT_YEAR, "2012");
        String input = null;
        LocalDateCurrentMonthAndYearInContext currentMonthAndYearInContext = new LocalDateCurrentMonthAndYearInContext(
                context);
        String returned = currentMonthAndYearInContext.apply(input);
        //
        assertThat(returned, is(nullValue()));
    }

    @Test
    public void test_context_ok_input_not_start_with_date() throws Exception {
        Map<String, String> context = Maps.newHashMap();
        context.put(CURRENT_MONTH, "02");
        context.put(CURRENT_YEAR, "2012");
        String input = "text : yyyy-mm-02";
        LocalDateCurrentMonthAndYearInContext currentMonthAndYearInContext = new LocalDateCurrentMonthAndYearInContext(
                context);
        String returned = currentMonthAndYearInContext.apply(input);
        //
        assertThat(returned, is(input));
    }

    @Test
    public void test_context_ok_input_ok() throws Exception {
        Map<String, String> context = Maps.newHashMap();
        context.put(CURRENT_MONTH, "02");
        context.put(CURRENT_YEAR, "2012");
        String input = "yyyy-mm-02";
        LocalDateCurrentMonthAndYearInContext currentMonthAndYearInContext = new LocalDateCurrentMonthAndYearInContext(
                context);
        String returned = currentMonthAndYearInContext.apply(input);
        //
        String expected = "2012-02-02";
        assertThat(returned, is(expected));
    }

    @Test
    public void test_context_ok_input_ok_with_bad_day() throws Exception {
        Map<String, String> context = Maps.newHashMap();
        context.put(CURRENT_MONTH, "02");
        context.put(CURRENT_YEAR, "2012");
        String input = "yyyy-mm-lolo";
        LocalDateCurrentMonthAndYearInContext currentMonthAndYearInContext = new LocalDateCurrentMonthAndYearInContext(
                context);
        String returned = currentMonthAndYearInContext.apply(input);
        //
        String expected = "2012-02-lolo";
        assertThat(returned, is(expected));
    }

    @Test
    public void test_context_ok_input_ok_with_no_day() throws Exception {
        Map<String, String> context = Maps.newHashMap();
        context.put(CURRENT_MONTH, "02");
        context.put(CURRENT_YEAR, "2012");
        String input = "yyyy-mm-";
        LocalDateCurrentMonthAndYearInContext currentMonthAndYearInContext = new LocalDateCurrentMonthAndYearInContext(
                context);
        String returned = currentMonthAndYearInContext.apply(input);
        //
        String expected = "2012-02-mm";
        assertThat(returned, is(expected));
    }
}
