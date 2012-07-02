package fixtures.common;

import junit.framework.TestCase;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

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
		assertThat(sessionReset,not(sameInstance(session1)));
		assertThat(sessionReset.getAttribute("test"), is(nullValue()));
	}

}
