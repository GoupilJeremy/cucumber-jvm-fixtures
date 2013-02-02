package fixtures.common.steputils;

import fixtures.common.StepParameter;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
public class ScenarioTools extends ApplicationObjectSupport {
    public void resetParameters() {

        final Map<String, StepParameter> stepParameterMap = BeanFactoryUtils
                .beansOfTypeIncludingAncestors(getApplicationContext(), StepParameter.class);
        final Collection<StepParameter> stepParameters = stepParameterMap.values();
        for (StepParameter parameter : stepParameters) {
            parameter.reset();
        }
    }
}
