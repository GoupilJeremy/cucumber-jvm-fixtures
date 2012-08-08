package fixtures.common.mail;

import static org.mockito.Mockito.mock;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Test;
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
}
