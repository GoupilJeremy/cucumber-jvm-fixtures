package fixtures.common.mail;

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

    public MimeMessage createMimeMessage(final InputStream contentStream) {
        throw new NotImplementedException("createMimeMessage(InputStream contentStream)");
    }

    public void send(final MimeMessagePreparator[] mimeMessagePreparators) {
        throw new NotImplementedException("send(MimeMessagePreparator mimeMessagePreparator)");
    }

    public void send(final SimpleMailMessage[] simpleMessages) {
        throw new NotImplementedException("send(SimpleMailMessage[] simpleMessages)");
    }

    public void send(final MimeMessage mimeMessage) {
        throw new NotImplementedException("send(MimeMessage mimeMessage)");
    }

    public void send(final MimeMessage[] mimeMessages) {
        throw new NotImplementedException("send(MimeMessage[] mimeMessages)");
    }

    public void send(final MimeMessagePreparator mimeMessagePreparator) {
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

    public void send(final SimpleMailMessage simpleMessage) {
        MailBean mailBean = new MailBean(simpleMessage);
        if (StringUtils.equalsIgnoreCase(mailBean.getTo(), "exception@aden.com")) {
            throw new MailAuthenticationException("Bad email from Mock :)");
        }
        mailBeans.add(mailBean);
    }

    public List<MailBean> getMailBeans() {
        return mailBeans;
    }

    public Collection<MailBean> sendMails(final String mailSubject) {
        final List<MailBean> mailBeanList = getMailBeans();
        return Collections2.filter(mailBeanList, new MailSubjectPredicate(mailSubject));
    }

    public DataTable toDataTable(Collection<MailBean> mailsSent, DataTable expected) {
        EmailTransformer emailTransformer = new EmailTransformer(expected);
        return emailTransformer.toDataTable(mailsSent);
    }

    private static class MailSubjectPredicate implements Predicate<MailBean> {
        private String mailSubject;

        public MailSubjectPredicate(final String mailSubject) {
            this.mailSubject = mailSubject;
        }

        @Override
        public boolean apply(final MailBean input) {
            return input.getSubject().contains(mailSubject);
        }
    }
}
