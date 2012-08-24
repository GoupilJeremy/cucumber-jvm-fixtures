package fixtures.common.transformers.variables;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

public class VariableExceptionTest {
    @Test
    public void testThrow_step_null() throws Exception {
        try {
            String cause = null;
            throw new VariableException(cause);
        } catch (VariableException e) {
            assertThat(e.getMessage(), Is.is("Cette variable ne peut être calculé : "));
        }
    }

    @Test
    public void testThrow_step_empty() throws Exception {
        try {
            String cause = null;
            throw new VariableException(cause);
        } catch (VariableException e) {
            assertThat(e.getMessage(), Is.is("Cette variable ne peut être calculé : "));
        }
    }

    @Test
    public void testThrow_step_ok() throws Exception {
        try {
            String cause = "bad var";
            throw new VariableException(cause);
        } catch (VariableException e) {
            assertThat(e.getMessage(), Is.is("Cette variable ne peut être calculé : bad var"));
        }
    }
}
