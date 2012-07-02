package fixtures.common.transformers.variables;

import junit.framework.TestCase;
import org.joda.time.MutableDateTime;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PreviousMonthTest {
	
		@Test
	public void test_apply_nominalCase() {
		PreviousMonth previousMonth = new PreviousMonth();
		String var = "ma date : ${moisPrecedent}";
		MutableDateTime previousMonthDate = MutableDateTime.now().monthOfYear().add(-1);
		//
		String expected = "ma date : "+ previousMonthDate.monthOfYear().getAsText(Locale.FRENCH);
		assertThat(previousMonth.apply(var),is(expected));
	}

	@Test
	public void test_getPreviousMonth() {
		PreviousMonth previousMonth = new PreviousMonth();
		MutableDateTime previousMonthDate = MutableDateTime.now().monthOfYear().add(-1);
		//
		String expected = previousMonthDate.monthOfYear().getAsText(Locale.FRENCH);
		assertThat(previousMonth.getPreviousMonth(),is(expected));

	}

	@Test
	public void test_apply_dont_match() {
		PreviousMonth previousMonth = new PreviousMonth();
		String var = "ma date : ${moisPrecedentMinuscule}";
		//
		assertThat(previousMonth.apply(var),is(var));

	}

	@Test
	public void test_apply_string_null() {
		PreviousMonth previousMonth = new PreviousMonth();
		String var = null;
		//
		assertThat(previousMonth.apply(var),is(""));

	}
}
