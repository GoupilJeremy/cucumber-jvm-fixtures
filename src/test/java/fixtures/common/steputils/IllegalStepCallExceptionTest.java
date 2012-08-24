package fixtures.common.steputils;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

public class IllegalStepCallExceptionTest {
    @Test
    public void testThrow_step_null() throws Exception {
        try {
            String step = null;
            throw new IllegalStepCallException(step);
        } catch (IllegalStepCallException e) {
            assertThat(e.getMessage(), Is.is("Ce step est lié au step : \"\" qu'il faut appeler avant!"));
        }
    }

    @Test
    public void testThrow_step_empty() throws Exception {
        try {
            String step = null;
            throw new IllegalStepCallException(step);
        } catch (IllegalStepCallException e) {
            assertThat(e.getMessage(), Is.is("Ce step est lié au step : \"\" qu'il faut appeler avant!"));
        }
    }

    @Test
    public void testThrow_step_ok() throws Exception {
        try {
            String step = "mon step à moi";
            throw new IllegalStepCallException(step);
        } catch (IllegalStepCallException e) {
            assertThat(e.getMessage(), Is.is("Ce step est lié au step : \"mon step à moi\" qu'il faut appeler avant!"));
        }
    }
}
