package fixtures.common;

import org.springframework.mock.web.MockHttpSession;
import org.springframework.stereotype.Component;

@Component
public class Session implements StepParameter {
    private MockHttpSession session;

    public Session() {
        session= newSession();
    }

    @Override
    public void reset() {
        this.session = newSession();
    }


    private MockHttpSession newSession() {
        return new MockHttpSession();
    }

    public MockHttpSession getSession() {
        return session;
    }


}
