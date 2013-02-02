package fixtures.common.transformers.variables;

import org.joda.time.MutableDateTime;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PreviousMonthUpperTest {
    @Test
    public void test_apply_nominalCase() {
        PreviousMonthUpper previousMonthUpper = new PreviousMonthUpper();
        String var = "ma date : ${moisPrecedentMajuscule}";
        MutableDateTime previousMonth = MutableDateTime.now().monthOfYear().add(-1);
        //
        String expected = "ma date : " + previousMonth.monthOfYear().getAsText(Locale.FRENCH).toUpperCase();
        assertThat(previousMonthUpper.apply(var), is(expected));
    }

    @Test
    public void test_getPreviousMonth() {
        PreviousMonthUpper previousMonthUpper = new PreviousMonthUpper();
        MutableDateTime previousMonth = MutableDateTime.now().monthOfYear().add(-1);
        //
        String expected = previousMonth.monthOfYear().getAsText(Locale.FRENCH);
        assertThat(previousMonthUpper.getPreviousMonth(), is(expected));
    }

    @Test
    public void test_apply_dont_match() {
        PreviousMonthUpper previousMonthUpper = new PreviousMonthUpper();
        String var = "ma date : ${moisPrecedentMinuscule}";
        //
        assertThat(previousMonthUpper.apply(var), is(var));
    }

    @Test
    public void test_apply_string_null() {
        PreviousMonthUpper previousMonthUpper = new PreviousMonthUpper();
        String var = null;
        //
        assertThat(previousMonthUpper.apply(var), is(""));
    }
}
