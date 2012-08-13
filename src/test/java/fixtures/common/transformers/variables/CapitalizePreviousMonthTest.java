package fixtures.common.transformers.variables;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.joda.time.MutableDateTime;
import org.junit.Test;

public class CapitalizePreviousMonthTest {
    @Test
    public void test_apply_nominalCase() {
        CapitalizePreviousMonth capitalizePreviousMonth = new CapitalizePreviousMonth();
        String var = "ma date : ${majMoisPrecedent}";
        MutableDateTime previousMonth = MutableDateTime.now().monthOfYear().add(-1);
        //
        String expected = "ma date : " + StringUtils.capitalize(previousMonth.monthOfYear().getAsText(Locale.FRENCH));
        assertThat(capitalizePreviousMonth.apply(var), is(expected));
    }

    @Test
    public void test_apply_dont_match() {
        CapitalizePreviousMonth capitalizePreviousMonth = new CapitalizePreviousMonth();
        String var = "ma date : ${anneeDuJourPrecedent}";
        //
        assertThat(capitalizePreviousMonth.apply(var), is(var));
    }

    @Test
    public void test_apply_string_null() {
        CapitalizePreviousMonth capitalizePreviousMonth = new CapitalizePreviousMonth();
        String var = null;
        //
        assertThat(capitalizePreviousMonth.apply(var), is(""));
    }
}
