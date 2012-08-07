package fixtures.common.transformers;

import static fixtures.common.transformers.Label.NON_BREAKING_SPACE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Constructor;

import org.junit.Test;

public class LabelTest {

    @Test
    public void testConstructor() throws Exception {
        Constructor constructor = Label.class.getDeclaredConstructor();
        // on vérifie que le contructeur est volontairement inacessible
        assertThat(constructor.isAccessible(),is(false));

        // pas utile au final, mais la couverture du constructeur est prise en compte
        constructor.setAccessible(true);
        constructor.newInstance();
    }

	@Test
	public void test_clean() {
		String myString = "ma" + NON_BREAKING_SPACE + "chaine de" + NON_BREAKING_SPACE + "caractère";
		String myCleanedString = "ma chaine de caractère";
		//
		assertThat(Label.cleanLabel(myString), is(myCleanedString));
	}
    
    @Test
	public void test_clean_null_string() {
		String myString = null;
		String myCleanedString = null;
		//
		assertThat(Label.cleanLabel(myString), is(myCleanedString));
	}

}
