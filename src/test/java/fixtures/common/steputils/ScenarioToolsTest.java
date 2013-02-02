package fixtures.common.steputils;

import fixtures.common.StepParameter;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ScenarioToolsTest {
    @Test
    public void testApplicationContext_has_stepParameter() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "classpath:config/context-with-step-parameters.xml");

        // Avant reset
        InnerStepParameter innerStepParameter01 = applicationContext
                .getBean("innerStepParameter01", InnerStepParameter.class);
        InnerStepParameter innerStepParameter02 = applicationContext
                .getBean("innerStepParameter02", InnerStepParameter.class);
        assertThat(innerStepParameter01.isReset(), is(false));
        assertThat(innerStepParameter01.isReset(), is(false));

        // On reset
        ScenarioTools scenarioTools = applicationContext.getBean("scenarioTools", ScenarioTools.class);
        scenarioTools.resetParameters();

        // Apres reset
        innerStepParameter01 = applicationContext.getBean("innerStepParameter01", InnerStepParameter.class);
        innerStepParameter02 = applicationContext.getBean("innerStepParameter02", InnerStepParameter.class);
        assertThat(innerStepParameter01.isReset(), is(true));
        assertThat(innerStepParameter01.isReset(), is(true));
    }

    @Test
    public void testApplicationContext_has_no_stepParameter() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "classpath:config/context-with-no-step-parameters.xml");
        // On reset
        ScenarioTools scenarioTools = applicationContext.getBean("scenarioTools", ScenarioTools.class);
        scenarioTools.resetParameters();
        // Pas d'erreur :)
    }

    public static class InnerStepParameter implements StepParameter {
        private boolean isReset = false;

        @Override
        public void reset() {
            isReset = true;
        }

        public boolean isReset() {
            return isReset;
        }
    }
}
