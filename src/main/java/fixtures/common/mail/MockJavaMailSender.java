package fixtures.common.mail;

import javax.annotation.Nullable;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import cucumber.table.DataTable;
import fixtures.common.transformers.EmailTransformer;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

public class MockJavaMailSender implements JavaMailSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(MockJavaMailSender.class);

    private List<MailBean> mailBeans = new ArrayList<MailBean>();

    public void clean() {
        mailBeans.clear();
    }

    // =============================================================================================
    // extended method
    // =============================================================================================
    public MimeMessage createMimeMessage() {
        throw new NotImplementedException("createMimeMessage()");
    }

    public MimeMessage createMimeMessage(final InputStream contentStream) throws MailException {
        throw new NotImplementedException("createMimeMessage(InputStream contentStream)");
    }

    public void send(final MimeMessage mimeMessage) throws MailException {
        throw new NotImplementedException("send(MimeMessage mimeMessage)");
    }

    public void send(final MimeMessage[] mimeMessages) throws MailException {
        throw new NotImplementedException("send(MimeMessage[] mimeMessages)");
    }

    public void send(final MimeMessagePreparator mimeMessagePreparator) throws MailException {
        MailBean mailBean;
        try {
            MimeMessage mimeMessage = new MimeMessage(Session.getInstance(new Properties()));
            mimeMessagePreparator.prepare(mimeMessage);
            //
            mailBean = new MailBean(mimeMessage);
            // on ajoute pas si on est dans le cas d'erreur : mail =  "exception@aden.com"
            if (!StringUtils.equalsIgnoreCase(mailBean.getTo(), "exception@aden.com")) {
                mailBeans.add(mailBean);
            }
        } catch (Exception e) {
            LOGGER.error("Impossible de récupérer les infos du message preparator", e);
            throw new MailAuthenticationException("Message preparator failed ", e);
        }
        if (StringUtils.equalsIgnoreCase(mailBean.getTo(), "exception@aden.com")) {
            throw new MailAuthenticationException("Bad email from Mock :)");
        }
    }

    public void send(final MimeMessagePreparator[] mimeMessagePreparators) throws MailException {
        throw new NotImplementedException("send(MimeMessagePreparator mimeMessagePreparator)");
    }

    public void send(final SimpleMailMessage simpleMessage) throws MailException {
        MailBean mailBean = new MailBean(simpleMessage);
        if (StringUtils.equalsIgnoreCase(mailBean.getTo(), "exception@aden.com")) {
            throw new MailAuthenticationException("Bad email from Mock :)");
        }
        mailBeans.add(mailBean);
    }

    public void send(final SimpleMailMessage[] simpleMessages) throws MailException {
        throw new NotImplementedException("send(SimpleMailMessage[] simpleMessages)");
    }

    public List<MailBean> getMailBeans() {
        return mailBeans;
    }

    public Collection<MailBean> sendMails(final String mailSubject) {
        final List<MailBean> mailBeans = getMailBeans();

        return Collections2.filter(mailBeans, new Predicate<MailBean>() {
            @Override
            public boolean apply(@Nullable final MailBean input) {
                return input.getSubject().contains(mailSubject);
            }
        });
    }

    public DataTable toDataTable(Collection<MailBean> mailsSent, DataTable expected) {
        EmailTransformer emailTransformer = new EmailTransformer(expected);
        return emailTransformer.toDataTable(mailsSent);
    }
}
