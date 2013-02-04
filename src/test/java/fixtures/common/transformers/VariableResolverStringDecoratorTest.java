package fixtures.common.transformers;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import fixtures.common.transformers.variables.VariableResolverStringDecorator;
import org.hamcrest.core.Is;
import org.joda.time.DateTimeUtils;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class VariableResolverStringDecoratorTest {
    private static final int FIXED_TIME = 100;

    @Test
    public void testConstructor() throws Exception {
        Constructor constructor = VariableResolverStringDecorator.class.getDeclaredConstructor();
        // on vérifie que le contructeur est volontairement inacessible
        assertThat(constructor.isAccessible(), is(false));

        // pas utile au final, mais la couverture du constructeur est prise en compte
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void test_check_nb_transformer() throws Exception {
        //when
        List<Function<String, String>> functions = VariableResolverStringDecorator
                .getFunctions(Maps.<String, String>newHashMap());
        //then
        // SI on change la taille, AJOUTER LES TESTS ASSOCIES A LA FONCTION
        assertThat(functions.size(), Is.is(9));
    }

    @Test
    public void test_null_string() throws Exception {
        String input = null;
        Map<String, String> maps = Maps.newHashMap();
        String result = VariableResolverStringDecorator.resolveVariables(input, maps);
        //
        assertThat(result, is(""));
    }

    @Test
    public void test_empty_string() throws Exception {
        String input = "";
        Map<String, String> maps = Maps.newHashMap();
        String result = VariableResolverStringDecorator.resolveVariables(input, maps);
        //
        assertThat(result, is(""));
    }

    @Test
    public void test_bad_variable() throws Exception {
        String input = "test = ${bad}";
        Map<String, String> maps = Maps.newHashMap();
        String result = VariableResolverStringDecorator.resolveVariables(input, maps);
        //
        assertThat(result, is(input));
    }

    // =================================================================================================================

    @Test
    public void test_resolve_variables_many_vars() throws Exception {
        //given
        String input = "test = ${moisNumeric} - ${moisPrecedent} - ${majMoisPrecedent} - ${majMoisPrecedent} - ${moisPrecedentEtAnnee} - ${moisPrecedentMajuscule} - ${now (yyyy/dd) +4}";
        DateTimeUtils.setCurrentMillisFixed(FIXED_TIME);
        //when
        final HashMap<String, String> context = Maps.newHashMap();
        String result = VariableResolverStringDecorator.resolveVariables(input, context);
        //then
        String expected = "test = 01 - décembre - Décembre - Décembre - Décembre 1969 - DÉCEMBRE - 1970/05";
        assertThat(result, is(expected));
    }

    @Test
    public void test_resolve_variables_with_vars() throws Exception {
        //given
        String input = "LP-1";
        DateTimeUtils.setCurrentMillisFixed(FIXED_TIME);
        //when
        final HashMap<String, String> context = Maps.newHashMap();
        context.put("LP-1", "ma var");
        String result = VariableResolverStringDecorator.resolveVariables(input, context);
        //then
        String expected = "ma var";
        assertThat(result, is(expected));
    }

    @Test
    public void test_resolve_variables_local_data() throws Exception {
        //given
        String input = "yyyy-mm-25";
        DateTimeUtils.setCurrentMillisFixed(FIXED_TIME);
        //when
        final HashMap<String, String> context = Maps.newHashMap();
        context.put("currentMonth", "09");
        context.put("currentYear", "1977");
        String result = VariableResolverStringDecorator.resolveVariables(input, context);
        //then
        String expected = "1977-09-25";
        assertThat(result, is(expected));
    }
}
