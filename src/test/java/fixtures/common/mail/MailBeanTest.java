package fixtures.common.mail;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import com.google.common.base.Strings;
import fixtures.common.transformers.Label;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailBeanTest {

	// from

	@Test(expected = AddressException.class)
	public void test_MailBean_mimeMessage_from_empty() throws Exception {
		MimeMessage mimeMessage = createMimeMessage("", new String[]{"to@to.com"}, null, "", "", true, "");
		new MailBean(mimeMessage);
	}

	@Test
	public void test_MailBean_mimeMessage_from_invalid() throws Exception {
		MimeMessage mimeMessage = createMimeMessage("from", new String[]{"to@to.com"}, null, "", "", true, "");
		MailBean mailBean = new MailBean(mimeMessage);
		//
		assertThat(mailBean.getFrom(), is("from"));
		assertThat(mailBean.getTo(), is("to@to.com"));
		assertThat(mailBean.getBcc(), is(""));
		assertThat(mailBean.getSubject(), is(""));
		assertThat(mailBean.getBody(), is(""));
		assertThat(mailBean.getAttachment(), is(""));
		assertThat(mailBean.getReplyTo(), is("from"));
	}

	// to
	@Test
	public void test_MailBean_mimeMessage_to_empty() throws Exception {
		MimeMessage mimeMessage = createMimeMessage("from@from.com", new String[0], null, "", "", true, "");
		MailBean mailBean = new MailBean(mimeMessage);
		//
		assertThat(mailBean.getFrom(), is("from@from.com"));
		assertThat(mailBean.getTo(), is(""));
		assertThat(mailBean.getBcc(), is(""));
		assertThat(mailBean.getSubject(), is(""));
		assertThat(mailBean.getBody(), is(""));
		assertThat(mailBean.getAttachment(), is(""));
		assertThat(mailBean.getReplyTo(), is("from@from.com"));
	}

	@Test
	public void test_MailBean_mimeMessage_many_to() throws Exception {
		MimeMessage mimeMessage = createMimeMessage("from@from.com", new String[]{"to1@to.com", "to2@to.com"}, null, "", "", true, "");
		MailBean mailBean = new MailBean(mimeMessage);
		//
		assertThat(mailBean.getFrom(), is("from@from.com"));
		assertThat(mailBean.getTo(), is("to1@to.com;to2@to.com"));
		assertThat(mailBean.getBcc(), is(""));
		assertThat(mailBean.getSubject(), is(""));
		assertThat(mailBean.getBody(), is(""));
		assertThat(mailBean.getAttachment(), is(""));
		assertThat(mailBean.getReplyTo(), is("from@from.com"));
	}

	// bcc
	@Test(expected = AddressException.class)
	public void test_MailBean_mimeMessage_bcc_empty() throws Exception {
		MimeMessage mimeMessage = createMimeMessage("from@from.com", new String[]{"to@to.com"}, "", "", "", true, "");
		new MailBean(mimeMessage);
	}

	@Test
	public void test_MailBean_mimeMessage_from_to_empty_bcc_ok() throws Exception {
		MimeMessage mimeMessage = createMimeMessage("from@from.com", new String[0], "bcc@bcc.com", "", "", true, "");
		MailBean mailBean = new MailBean(mimeMessage);
		//
		assertThat(mailBean.getFrom(), is("from@from.com"));
		assertThat(mailBean.getTo(), is(""));
		assertThat(mailBean.getBcc(), is("bcc@bcc.com"));
		assertThat(mailBean.getSubject(), is(""));
		assertThat(mailBean.getBody(), is(""));
		assertThat(mailBean.getAttachment(), is(""));
		assertThat(mailBean.getReplyTo(), is("from@from.com"));
	}

	// subject/body

	@Test
	public void test_MailBean_mimeMessage_subject_body_multipart_ok() throws Exception {
		MimeMessage mimeMessage = createMimeMessage("from@from.com", new String[]{"to@to.com"}, null, "my subject", "my body for mail", true, "");
		MailBean mailBean = new MailBean(mimeMessage);
		//
		assertThat(mailBean.getFrom(), is("from@from.com"));
		assertThat(mailBean.getTo(), is("to@to.com"));
		assertThat(mailBean.getBcc(), is(""));
		assertThat(mailBean.getSubject(), is("my subject"));
		assertThat(mailBean.getBody(), is("my body for mail"));
		assertThat(mailBean.getAttachment(), is(""));
		assertThat(mailBean.getReplyTo(), is("from@from.com"));
		//
		assertThat(mailBean.toString(), is("MailBean{to='to@to.com', from='from@from.com'', subject='my subject'}"));
	}

	@Test
	public void test_MailBean_mimeMessage_subject_body_notmultipart() throws Exception {
		MimeMessage mimeMessage = createMimeMessage("from@from.com", new String[]{"to@to.com"}, null, "my subject", "my body for mail", false, "");
		MailBean mailBean = new MailBean(mimeMessage);
		//
		assertThat(mailBean.getFrom(), is("from@from.com"));
		assertThat(mailBean.getTo(), is("to@to.com"));
		assertThat(mailBean.getBcc(), is(""));
		assertThat(mailBean.getSubject(), is("my subject"));
		assertThat(mailBean.getBody(), is("my body for mail"));
		assertThat(mailBean.getAttachment(), is(""));
		assertThat(mailBean.getReplyTo(), is("from@from.com"));
	}

	@Test
	public void test_MailBean_mimeMessage_subject_body_multipart_with_two_content() throws Exception {
		MimeMessage mimeMessage = createMimeMessage("from@from.com", new String[]{"to@to.com"}, null, "my subject", "my body for mail", true, "my body in html");
		MailBean mailBean = new MailBean(mimeMessage);
		//
		assertThat(mailBean.getFrom(), is("from@from.com"));
		assertThat(mailBean.getTo(), is("to@to.com"));
		assertThat(mailBean.getBcc(), is(""));
		assertThat(mailBean.getSubject(), is("my subject"));
		assertThat(mailBean.getBody(), is("my body for mail"));
		assertThat(mailBean.getAttachment(), is(""));
		assertThat(mailBean.getReplyTo(), is("from@from.com"));
	}

	// attachment

	@Test
	public void test_MailBean_mimeMessage_with_attachment() throws Exception {
		String text = "my body for mail";
		MimeMessage mimeMessage = createMimeMessage("from@from.com", new String[]{"to@to.com"}, null, "my subject", text, true, "jhghjg");
		addAttachement(mimeMessage, "content1", "data with content", text);
		MailBean mailBean = new MailBean(mimeMessage);
		//
		assertThat(mailBean.getFrom(), is("from@from.com"));
		assertThat(mailBean.getTo(), is("to@to.com"));
		assertThat(mailBean.getBcc(), is(""));
		assertThat(mailBean.getSubject(), is("my subject"));
		assertThat(mailBean.getBody(), is(text));
		assertThat(mailBean.getAttachment(), is("data with content"));
		assertThat(mailBean.getReplyTo(), is("from@from.com"));
		//
	}

	@Test
	public void test_MailBean_mimeMessage_with_file_attachment() throws Exception {
		String text = "my body for mail";
		MimeMessage mimeMessage = createMimeMessage("from@from.com", new String[]{"to@to.com"}, null, "my subject", text, true, "");
		addAttachementFile(mimeMessage, "content1", "file with" + Label.NON_BREAKING_SPACE + "content", "content on line2", text);
		MailBean mailBean = new MailBean(mimeMessage);
		//
		assertThat(mailBean.getFrom(), is("from@from.com"));
		assertThat(mailBean.getTo(), is("to@to.com"));
		assertThat(mailBean.getBcc(), is(""));
		assertThat(mailBean.getSubject(), is("my subject"));
		assertThat(mailBean.getBody(), is(text));
		assertThat(mailBean.getAttachment(), is("file with content\ncontent on line2"));
		assertThat(mailBean.getReplyTo(), is("from@from.com"));
		//
	}

	// =======================================================================================
	// mailBean SimpleMailMessage
	// =======================================================================================

	@Test
	public void test_MailBean_SimpleMailMessage_nominalCase() throws Exception {

		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom("from@from.com");
		simpleMailMessage.setTo("to@to.com");
		simpleMailMessage.setBcc("bcc@bcc.com");
		simpleMailMessage.setSubject("my subject");
		simpleMailMessage.setText("my content");
		simpleMailMessage.setReplyTo("reply@to.com");
		//
		MailBean mailBean = new MailBean(simpleMailMessage);
		//
		assertThat(mailBean.getFrom(), is("from@from.com"));
		assertThat(mailBean.getTo(), is("to@to.com"));
		assertThat(mailBean.getBcc(), is("bcc@bcc.com"));
		assertThat(mailBean.getSubject(), is("my subject"));
		assertThat(mailBean.getBody(), is("my content"));
		assertThat(mailBean.getAttachment(), is(""));
		assertThat(mailBean.getReplyTo(), is("reply@to.com"));
	}

	@Test
	public void test_MailBean_SimpleMailMessage_nominalCase_many_values() throws Exception {

		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom("from@from.com");
		simpleMailMessage.setTo(new String[]{"to@to.com", "to1@to.com"});
		simpleMailMessage.setBcc(new String[]{"bcc@bcc.com", "bcc1@bcc.com"});
		simpleMailMessage.setSubject("my subject");
		simpleMailMessage.setText("my content");
		simpleMailMessage.setReplyTo("reply@to.com");
		//
		MailBean mailBean = new MailBean(simpleMailMessage);
		//
		assertThat(mailBean.getFrom(), is("from@from.com"));
		assertThat(mailBean.getTo(), is("to@to.com,to1@to.com"));
		assertThat(mailBean.getBcc(), is("bcc@bcc.com,bcc1@bcc.com"));
		assertThat(mailBean.getSubject(), is("my subject"));
		assertThat(mailBean.getBody(), is("my content"));
		assertThat(mailBean.getAttachment(), is(""));
		assertThat(mailBean.getReplyTo(), is("reply@to.com"));
	}

	// =======================================================================================
	// Utils
	// =======================================================================================

	private MimeMessage createMimeMessage(String from, String[] to, String bcc, String subject, String text, boolean multipart, String htmlText) throws MessagingException {
		MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties()));
		MimeMessageHelper message = new MimeMessageHelper(mimeMessage, multipart, "UTF-8");
		message.setFrom(from);
		message.setTo(to);
		if (bcc != null) {
			message.setBcc(bcc);
		}
		message.setSubject(subject);
		if (Strings.isNullOrEmpty(htmlText)) {
			message.setText(text);
		} else {
			message.setText(text, htmlText);
		}
		//
		return mimeMessage;
	}

	private void addAttachement(MimeMessage mimeMessage, String name, String content, String text) throws MessagingException {
		MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		message.setText(text);
		message.addAttachment(name, new ByteArrayResource(content.getBytes()));
	}

	private void addAttachementFile(MimeMessage mimeMessage, String name, String line1, String line2, String text) throws MessagingException, IOException {
		MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		message.setText(text);

		File file = File.createTempFile(name, "txt");
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(line1);
		fileWriter.write("\r\n");
		fileWriter.write(line2);
		fileWriter.close();

		message.addAttachment(name, file);
	}

}
