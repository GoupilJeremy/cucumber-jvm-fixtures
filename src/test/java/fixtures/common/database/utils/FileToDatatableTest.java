package fixtures.common.database.utils;

import fixtures.common.datatable.DatatableUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.junit.Assert.assertThat;

public class FileToDatatableTest {

	    @Test
    public void testConstructor() throws Exception {
        Constructor constructor = FileToDatatable.class.getDeclaredConstructor();
        // on vérifie que le contructeur est volontairement inacessible
        assertThat(constructor.isAccessible(), CoreMatchers.is(false));

        // pas utile au final, mais la couverture du constructeur est prise en compte
        constructor.setAccessible(true);
        constructor.newInstance();
    }

}
