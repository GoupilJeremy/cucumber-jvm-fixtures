package fixtures.common.steputils;

import java.util.Collection;
import java.util.Map;

import cucumber.annotation.After;
import fixtures.common.StepParameter;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

@Component
public class ScenarioTools extends ApplicationObjectSupport {
    @After
    public void resetParameters() {

        final Map<String, StepParameter> stepParameterMap = BeanFactoryUtils
                .beansOfTypeIncludingAncestors(getApplicationContext(), StepParameter.class);
        final Collection<StepParameter> stepParameters = stepParameterMap.values();
        for (StepParameter parameter : stepParameters) {
            parameter.reset();
        }
    }
}
