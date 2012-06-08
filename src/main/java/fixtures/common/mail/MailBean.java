package fixtures.common.mail;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.springframework.mail.SimpleMailMessage;

public class MailBean {
    private static final String CARRIAGE_RETURN = "\n";

    private String to;

    private String from;

    private String replyTo;

    private String bcc;

    private String subject;

    private String body;

    private String attachment;

    public MailBean(final MimeMessage mimeMessage) throws MessagingException, IOException {
        to = extractTo(mimeMessage);
        from = extractFrom(mimeMessage);
        bcc = extractBcc(mimeMessage);
        replyTo = extractReplyTo(mimeMessage);
        subject = mimeMessage.getSubject();
        body = extractBody(mimeMessage);
        attachment = extractAttachment(mimeMessage);
    }

    public MailBean(final SimpleMailMessage simpleMessage) {
        to = formatArray(simpleMessage.getTo());
        from = simpleMessage.getFrom();
        replyTo = simpleMessage.getReplyTo();
        bcc = formatArray(simpleMessage.getBcc());
        subject = simpleMessage.getSubject();
        body = simpleMessage.getText();
        attachment = StringUtils.EMPTY;
    }

    // =============================================================================================
    // Utils
    // =============================================================================================
    private String formatArray(final String[] array) {
        String label = StringUtils.EMPTY;
        if (!ArrayUtils.isEmpty(array)) {
            label = StringUtils.join(Arrays.asList(array), ",");
        }
        return label;
    }

    private String extractAttachment(final MimeMessage mimeMessage) throws IOException, MessagingException {
        Object content = mimeMessage.getContent();
        String attachment = StringUtils.EMPTY;
        if (content instanceof MimeMultipart) {
            MimeMultipart mimeMultipart = (MimeMultipart) content;
            //MimeMultipart bodyPart = (MimeMultipart) mimeMultipart.getBodyPart(0).getContent();
            // on a une pi�ce jointe
            if (mimeMultipart.getCount() > 1) {
                if (mimeMultipart.getBodyPart(1).getContent() instanceof FileInputStream) {
                    FileInputStream fileInputStream = (FileInputStream) mimeMultipart.getBodyPart(1).getContent();
                    List listLines = IOUtils.readLines(fileInputStream, CharEncoding.ISO_8859_1);
                    // on remplace les espaces non s�cables par un espace normal
                    attachment = StringUtils.replace(StringUtils.join(listLines, "\n"), "�", " ");
                    // ferme le flux
                    IOUtils.closeQuietly(fileInputStream);
                } else if (mimeMultipart.getBodyPart(1).getContent() instanceof ByteArrayInputStream) {
                    ByteArrayInputStream fileInputStream = (ByteArrayInputStream) mimeMultipart.getBodyPart(1)
                            .getContent();
                    List listLines = IOUtils.readLines(fileInputStream, CharEncoding.ISO_8859_1);
                    // on remplace les espaces non s�cables par un espace normal
                    attachment = StringUtils.replace(StringUtils.join(listLines, "\n"), "�", " ");
                    // ferme le flux
                    IOUtils.closeQuietly(fileInputStream);
                }
            }
        }
        return attachment;
    }

    private String extractBody(final MimeMessage mimeMessage) throws IOException, MessagingException {
        Object content = mimeMessage.getContent();
        String body = StringUtils.EMPTY;
        if (content instanceof MimeMultipart) {
            MimeMultipart mimeMultipart = (MimeMultipart) content;
            MimeMultipart bodyPart = (MimeMultipart) mimeMultipart.getBodyPart(0).getContent();
            // on a un body
            if (bodyPart.getCount() > 0) {
                MimeBodyPart mimeBodyPart = (MimeBodyPart) bodyPart.getBodyPart(0);
                if (mimeBodyPart.getContent() instanceof MimeMultipart) {
                    MimeMultipart multipart = (MimeMultipart) mimeBodyPart.getContent();
                    if (multipart.getCount() > 0) {
                        mimeBodyPart = (MimeBodyPart) multipart.getBodyPart(0);
                    }
                }
                InputStream inputStream = mimeBodyPart.getInputStream();
                //
                List listLines = IOUtils.readLines(inputStream, CharEncoding.ISO_8859_1);
                // on remplace les espaces non s�cables par un espace normal
                body = StringUtils.replace(StringUtils.join(listLines, CARRIAGE_RETURN), "�", " ");
            }
        } else if (content instanceof String) {
            body = (String) content;
        }
        return body;
    }

    private String extractFrom(final MimeMessage mimeMessage) throws MessagingException {
        Address[] addressFrom = mimeMessage.getFrom();
        List<String> listAddress = createListAddress(addressFrom);
        return StringUtils.join(listAddress, ";");
    }

    private String extractReplyTo(final MimeMessage mimeMessage) throws MessagingException {
        Address[] addressReplyTo = mimeMessage.getReplyTo();
        List<String> listAddress = createListAddress(addressReplyTo);
        return StringUtils.join(listAddress, ";");
    }

    private String extractBcc(final MimeMessage mimeMessage) throws MessagingException {
        Address[] addressBcc = mimeMessage.getRecipients(Message.RecipientType.BCC);
        List<String> listAddress = createListAddress(addressBcc);
        return StringUtils.join(listAddress, ";");
    }

    private String extractTo(final MimeMessage mimeMessage) throws MessagingException {
        Address[] addressBcc = mimeMessage.getRecipients(Message.RecipientType.TO);
        List<String> listAddress = createListAddress(addressBcc);
        return StringUtils.join(listAddress, ";");
    }

    private List<String> createListAddress(final Address[] addresses) {
        List<String> listAddress = new ArrayList<String>();
        if (!ArrayUtils.isEmpty(addresses)) {
            for (Address address : addresses) {
                listAddress.add(address.toString());
            }
            Collections.sort(listAddress);
        }
        return listAddress;
    }

    @Override
    public String toString() {
        return "MailBean{" + "to='" + to + '\'' + ", from='" + from + '\'' + '\'' + ", subject='" + subject + '\'' +
                '}';
    }
    // =============================================================================================
    // getter/setter
    // =============================================================================================

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getBcc() {
        return bcc;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getAttachment() {
        return attachment;
    }

    public String getReplyTo() {
        return replyTo;
    }
}
