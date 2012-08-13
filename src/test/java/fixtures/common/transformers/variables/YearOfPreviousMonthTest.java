package fixtures.common.transformers.variables;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.joda.time.MutableDateTime;
import org.junit.Test;

public class YearOfPreviousMonthTest {
    @Test
    public void test_apply_nominalCase() {
        YearOfPreviousMonth yearOfPreviousMonth = new YearOfPreviousMonth();
        String var = "ma date : ${anneeDuMoisPrecedent}";
        MutableDateTime previousMonth = MutableDateTime.now().monthOfYear().add(-1);
        //
        String expected = "ma date : " + previousMonth.year().getAsText(Locale.FRENCH);
        assertThat(yearOfPreviousMonth.apply(var), is(expected));
    }

    @Test
    public void test_apply_dont_match() {
        YearOfPreviousMonth yearOfPreviousMonth = new YearOfPreviousMonth();
        String var = "ma date : ${anneeDuJourPrecedent}";
        //
        assertThat(yearOfPreviousMonth.apply(var), is(var));
    }

    @Test
    public void test_apply_string_null() {
        YearOfPreviousMonth yearOfPreviousMonth = new YearOfPreviousMonth();
        String var = null;
        //
        assertThat(yearOfPreviousMonth.apply(var), is(""));
    }
}
