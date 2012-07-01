package fixtures.common.transformers.variables;

import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;
import org.joda.time.MutableDateTime;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PreviousMonthAndYearTest {
	
		@Test
	public void test_apply_nominalCase() {
		PreviousMonthAndYear previousMonthAndYear = new PreviousMonthAndYear();
		String var = "ma date : ${moisPrecedentEtAnnee}";
		MutableDateTime previousMonth = MutableDateTime.now().monthOfYear().add(-1);
		//
		String expected = "ma date : "+ StringUtils.capitalize(previousMonth.monthOfYear().getAsText(Locale.FRENCH)) + " " + previousMonth.year().getAsText(Locale.FRENCH) ;
		assertThat(previousMonthAndYear.apply(var),is(expected));

	}

	@Test
	public void test_apply_dont_match() {
		PreviousMonthAndYear previousMonthAndYear = new PreviousMonthAndYear();
		String var = "ma date : ${anneeDuJourPrecedent}";
		//
		assertThat(previousMonthAndYear.apply(var),is(var));

	}

	@Test
	public void test_apply_string_null() {
		PreviousMonthAndYear previousMonthAndYear = new PreviousMonthAndYear();
		String var = null;
		//
		assertThat(previousMonthAndYear.apply(var),is(""));

	}
	
}
