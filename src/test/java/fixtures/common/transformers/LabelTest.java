package fixtures.common.transformers;

import org.junit.Test;

import static fixtures.common.transformers.Label.NON_BREAKING_SPACE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LabelTest {

	@Test
	public void test_clean() {
		String myString = "ma" + NON_BREAKING_SPACE + "chaine de" + NON_BREAKING_SPACE + "caractère";
		String myCleanedString = "ma chaine de caractère";
		//
		assertThat(Label.cleanLabel(myString), is(myCleanedString));
	}

}
