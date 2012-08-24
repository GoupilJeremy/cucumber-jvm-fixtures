package fixtures.common;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

public class SessionTest {
    @Test
    public void test_reset() {
        Session session = new Session();
        MockHttpSession session1 = session.getSession();
        session1.setAttribute("test", "test");
        //
        session.reset();
        //
        MockHttpSession sessionReset = session.getSession();
        assertThat(sessionReset, not(sameInstance(session1)));
        assertThat(sessionReset.getAttribute("test"), is(nullValue()));
    }
}
