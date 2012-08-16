package fixtures.common.mail;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessagePreparator;

public class MockJavaMailSenderTest {



    @Test(expected = NotImplementedException.class)
    public void testCreateMimeMessage() throws Exception {
        MockJavaMailSender mockJavaMailSender = new MockJavaMailSender();
        mockJavaMailSender.createMimeMessage();
    }

    @Test(expected = NotImplementedException.class)
    public void testCreateMimeMessage_inputStream() throws Exception {
        MockJavaMailSender mockJavaMailSender = new MockJavaMailSender();
        InputStream inputStream = mock(InputStream.class);
        mockJavaMailSender.createMimeMessage(inputStream);
    }

    @Test(expected = NotImplementedException.class)
    public void testSend_MimeMessagePreparator() throws Exception {
        MockJavaMailSender mockJavaMailSender = new MockJavaMailSender();
        MimeMessagePreparator[] mimeMessagePreparator = new MimeMessagePreparator[0];
        mockJavaMailSender.send(mimeMessagePreparator);
    }

    @Test(expected = NotImplementedException.class)
    public void testSend_SimpleMailMessage() throws Exception {
        MockJavaMailSender mockJavaMailSender = new MockJavaMailSender();
        SimpleMailMessage[] simpleMessages = new SimpleMailMessage[0];
        mockJavaMailSender.send(simpleMessages);
    }

    @Test(expected = NotImplementedException.class)
    public void testSend_MimeMessage() throws Exception {
        MockJavaMailSender mockJavaMailSender = new MockJavaMailSender();
        MimeMessage mimeMessage = mock(MimeMessage.class);
        mockJavaMailSender.send(mimeMessage);
    }

    @Test(expected = NotImplementedException.class)
    public void testSend_MimeMessage_array() throws Exception {
        MockJavaMailSender mockJavaMailSender = new MockJavaMailSender();
        MimeMessage[] mimeMessages = new MimeMessage[0];
        mockJavaMailSender.send(mimeMessages);
    }

	// =================================================================================================================
	// test send(final SimpleMailMessage simpleMessage)
	// =================================================================================================================


	@Test (expected = IllegalArgumentException.class)
	public void testSend_simpleMailMessage_null() throws Exception {
		MockJavaMailSender mockJavaMailSender = new MockJavaMailSender();
        SimpleMailMessage simpleMailMessage =  null;
        mockJavaMailSender.send(simpleMailMessage);
	}

	@Test
	public void testSend_simpleMailMessage_ok() throws Exception {
		MockJavaMailSender mockJavaMailSender = new MockJavaMailSender();
        SimpleMailMessage simpleMailMessage =  new SimpleMailMessage();
		String to = "email@email.com";
		simpleMailMessage.setTo(to);
        mockJavaMailSender.send(simpleMailMessage);
		//
		List<MailBean> mailBeans = mockJavaMailSender.getMailBeans();
		assertThat(mailBeans.size(), is(1));
		assertThat(mailBeans.get(0).getTo(), is(to));
	}

	@Test (expected = MailAuthenticationException.class)
	public void testSend_simpleMailMessage_email_throw_error() throws Exception {
		MockJavaMailSender mockJavaMailSender = new MockJavaMailSender();
        SimpleMailMessage simpleMailMessage =  new SimpleMailMessage();
		String to = MockJavaMailSender.EXCEPTION_EMAIL;
		simpleMailMessage.setTo(to);
        mockJavaMailSender.send(simpleMailMessage);
	}

	// =================================================================================================================
	// test clean
	// =================================================================================================================

	@Test
	public void testSend_clean() throws Exception {
		MockJavaMailSender mockJavaMailSender = new MockJavaMailSender();
        SimpleMailMessage simpleMailMessage =  new SimpleMailMessage();
		String to = "email@email.com";
		simpleMailMessage.setTo(to);
        mockJavaMailSender.send(simpleMailMessage);
		//
		assertThat(mockJavaMailSender.getMailBeans().size(), is(1));
		// test clean
		mockJavaMailSender.clean();
		assertThat(mockJavaMailSender.getMailBeans().isEmpty(), is(true));
	}

	// =================================================================================================================
	// test getMailBean
	// =================================================================================================================

	@Test
	public void testSend_getMailBean() throws Exception {
		MockJavaMailSender mockJavaMailSender = new MockJavaMailSender();
        SimpleMailMessage simpleMailMessage =  new SimpleMailMessage();
		String to = "email@email.com";
		simpleMailMessage.setTo(to);
        mockJavaMailSender.send(simpleMailMessage);
		//
		List<MailBean> mailBeans = mockJavaMailSender.getMailBeans();
		assertThat(mailBeans.size(), is(1));
		// on va vérifier que la liste renvoyé par getMail est une duplication
		mailBeans.add(new MailBean(simpleMailMessage));
		assertThat(mailBeans.size(), is(2));
		// la liste d'origine n'a pas changé
		assertThat(mockJavaMailSender.getMailBeans().size(), is(1));
	}

	// =================================================================================================================
	// test getMailBean
	// =================================================================================================================

	@Test
	public void testSend_sendMail_mailSubject_null() throws Exception {
		MockJavaMailSender mockJavaMailSender = new MockJavaMailSender();
		SimpleMailMessage simpleMailMessage01 =  createSimpleMailMessage("to01@to.com","from01@from.com","My subject");
		SimpleMailMessage simpleMailMessage02 =  createSimpleMailMessage("to02@to.com","from02@from.com","My subject 02");
        mockJavaMailSender.send(simpleMailMessage01);
        mockJavaMailSender.send(simpleMailMessage02);
		//
		String mySubject = null;
		Collection<MailBean> mailBeans = mockJavaMailSender.sendMails(mySubject);
		assertThat(mailBeans.isEmpty(), is(true));
	}

	@Test
	public void testSend_sendMail_mailSubject_no_subject_found() throws Exception {
		MockJavaMailSender mockJavaMailSender = new MockJavaMailSender();
		SimpleMailMessage simpleMailMessage01 =  createSimpleMailMessage("to01@to.com","from01@from.com","My subject");
		SimpleMailMessage simpleMailMessage02 =  createSimpleMailMessage("to02@to.com","from02@from.com","My subject 02");
        mockJavaMailSender.send(simpleMailMessage01);
        mockJavaMailSender.send(simpleMailMessage02);
		//
		String mySubject = "no subject";
		Collection<MailBean> mailBeans = mockJavaMailSender.sendMails(mySubject);
		assertThat(mailBeans.isEmpty(), is(true));
	}

	@Test
	public void testSend_sendMail_mailSubject_subject_found_everywhere() throws Exception {
		MockJavaMailSender mockJavaMailSender = new MockJavaMailSender();
		SimpleMailMessage simpleMailMessage01 =  createSimpleMailMessage("to01@to.com","from01@from.com","My subject");
		SimpleMailMessage simpleMailMessage02 =  createSimpleMailMessage("to02@to.com","from02@from.com","My subject 02");
        mockJavaMailSender.send(simpleMailMessage01);
        mockJavaMailSender.send(simpleMailMessage02);
		//
		String mySubject = "subject";
		Collection<MailBean> mailBeans = mockJavaMailSender.sendMails(mySubject);
		assertThat(mailBeans.size(), is(2));
	}

	@Test
	public void testSend_sendMail_mailSubject_subject_found_only_one_time() throws Exception {
		MockJavaMailSender mockJavaMailSender = new MockJavaMailSender();
		SimpleMailMessage simpleMailMessage01 =  createSimpleMailMessage("to01@to.com","from01@from.com","My subject");
		SimpleMailMessage simpleMailMessage02 =  createSimpleMailMessage("to02@to.com","from02@from.com","My subject 02");
        mockJavaMailSender.send(simpleMailMessage01);
        mockJavaMailSender.send(simpleMailMessage02);
		//
		String mySubject = "My subject 02";
		Collection<MailBean> mailBeans = mockJavaMailSender.sendMails(mySubject);
		assertThat(mailBeans.size(), is(1));
	}

	@Test
	public void testSend_sendMail_mailSubject_subject_found_but_different_case() throws Exception {
		MockJavaMailSender mockJavaMailSender = new MockJavaMailSender();
		SimpleMailMessage simpleMailMessage01 =  createSimpleMailMessage("to01@to.com","from01@from.com","My subject");
		SimpleMailMessage simpleMailMessage02 =  createSimpleMailMessage("to02@to.com","from02@from.com","My subject 02");
        mockJavaMailSender.send(simpleMailMessage01);
        mockJavaMailSender.send(simpleMailMessage02);
		//
		String mySubject = "My Subject 02";
		Collection<MailBean> mailBeans = mockJavaMailSender.sendMails(mySubject);
		assertThat(mailBeans.isEmpty(), is(true));
	}

	// =================================================================================================================
	// utils
	// =================================================================================================================


	private SimpleMailMessage createSimpleMailMessage(final String to, final String from, final String subject) {
		SimpleMailMessage simpleMailMessage =  new SimpleMailMessage();
		simpleMailMessage.setTo(to);
		simpleMailMessage.setFrom(from);
		simpleMailMessage.setSubject(subject);
		return simpleMailMessage;
	}
}
