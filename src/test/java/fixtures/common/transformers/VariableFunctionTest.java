package fixtures.common.transformers;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import com.google.common.collect.Maps;
import fixtures.common.transformers.variables.VariableFunction;
import org.junit.Test;

public class VariableFunctionTest {
    @Test
    public void test_context_is_null() {
        Map<String, String> context = null;
        String input = "text";
        VariableFunction variableFunction = new VariableFunction(context);
        //
        assertThat(variableFunction.apply(input), is(input));
    }

    @Test
    public void test_context_is_ok_one_var_found() {
        Map<String, String> context = Maps.newHashMap();
        context.put("PT01", "programme 01");
        String input = "PT01";
        String expected = "programme 01";
        VariableFunction variableFunction = new VariableFunction(context);
        //
        assertThat(variableFunction.apply(input), is(expected));
    }

    @Test
    public void test_context_is_ok_no_var_found() {
        Map<String, String> context = Maps.newHashMap();
        context.put("PT01", "programme 01");
        String input = "PT02";
        String expected = "PT02";
        VariableFunction variableFunction = new VariableFunction(context);
        //
        assertThat(variableFunction.apply(input), is(expected));
    }

    @Test
    public void test_context_is_ok_input_null() {
        Map<String, String> context = Maps.newHashMap();
        context.put("PT01", "programme 01");
        String input = null;
        String expected = "";
        VariableFunction variableFunction = new VariableFunction(context);
        //
        assertThat(variableFunction.apply(input), is(expected));
    }
}
