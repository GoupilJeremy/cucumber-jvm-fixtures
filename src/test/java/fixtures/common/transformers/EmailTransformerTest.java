package fixtures.common.transformers;

import com.google.common.collect.Lists;
import cucumber.api.DataTable;
import fixtures.common.datatable.DatatableUtils;
import fixtures.common.mail.MailBean;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static fixtures.common.transformers.EmailTransformer.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class EmailTransformerTest {
    @Test(expected = IllegalArgumentException.class)
    public void testFileTransformer_mails_null() throws Exception {
        List<String> headers = Lists.newArrayList(SUJET_HEADER);
        List<String> list01 = Lists.newArrayList("Hello");
        DataTable dataTable = DatatableUtils.getDatatable(headers, Lists.<List<String>>newArrayList(headers, list01));
        //
        Collection<MailBean> mailBeans = null;
        EmailTransformer emailTransformer = new EmailTransformer(dataTable);
        emailTransformer.toDataTable(mailBeans);
    }

    @Test
    public void testFileTransformer_mails_empty() throws Exception {
        List<String> headers = Lists.newArrayList(SUJET_HEADER);
        List<String> list01 = Lists.newArrayList("Hello");
        DataTable dataTable = DatatableUtils.getDatatable(headers, Lists.<List<String>>newArrayList(headers, list01));
        //
        Collection<MailBean> mailBeans = Lists.newArrayList();
        EmailTransformer emailTransformer = new EmailTransformer(dataTable);
        DataTable dataTableEmail = emailTransformer.toDataTable(mailBeans);
        //
        List<List<String>> expected = Lists.<List<String>>newArrayList(Lists.<String>newArrayList(SUJET_HEADER));
        assertThat(dataTableEmail.raw(), is(expected));
    }

    @Test
    public void testFileTransformer_mails_ok_only_few_header() throws Exception {
        List<String> headers = Lists.newArrayList(SUJET_HEADER, MESSAGE_HEADER);
        List<String> list01 = Lists.newArrayList("Hello", "how are you ?");
        DataTable dataTable = DatatableUtils.getDatatable(headers, Lists.<List<String>>newArrayList(headers, list01));
        //
        MailBean mailBean = mock(MailBean.class);
        // mock
        when(mailBean.getFrom()).thenReturn("from@mail.com");
        when(mailBean.getTo()).thenReturn("to@mail.com");
        when(mailBean.getBcc()).thenReturn("bcc@mail.com");
        when(mailBean.getSubject()).thenReturn("mon sujet");
        when(mailBean.getBody()).thenReturn("mon message");
        //
        Collection<MailBean> mailBeans = Lists.newArrayList(mailBean);
        EmailTransformer emailTransformer = new EmailTransformer(dataTable);
        DataTable dataTableEmail = emailTransformer.toDataTable(mailBeans);
        //
        List<List<String>> expected = Lists
                .<List<String>>newArrayList(Lists.<String>newArrayList(SUJET_HEADER, MESSAGE_HEADER),
                        Lists.<String>newArrayList("mon sujet", "mon message"));
        assertThat(dataTableEmail.raw(), is(expected));
    }

    @Test
    public void testFileTransformer_mails_ok_all_header() throws Exception {
        List<String> headers = Lists
                .newArrayList(SUJET_HEADER, MESSAGE_HEADER, A_HEADER, COPIE_CACHEE_HEADER, DE_HEADER,
                        PIECE_JOINTE_HEADER, REPONDRE_A_HEADER);
        List<String> list01 = Lists
                .newArrayList("Hello", "how are you ?", "to@to.com", "cc@cc.com", "from@from,com", "PJ", "reply");
        DataTable dataTable = DatatableUtils.getDatatable(headers, Lists.<List<String>>newArrayList(headers, list01));
        //
        MailBean mailBean = mock(MailBean.class);
        // mock
        when(mailBean.getFrom()).thenReturn("from@mail.com");
        when(mailBean.getTo()).thenReturn("to@mail.com");
        when(mailBean.getBcc()).thenReturn("bcc@mail.com");
        when(mailBean.getSubject()).thenReturn("mon sujet");
        when(mailBean.getBody()).thenReturn("mon message with" + Label.NON_BREAKING_SPACE + "non-breaking space");
        when(mailBean.getReplyTo()).thenReturn("replyTo@mail.com");
        //
        Collection<MailBean> mailBeans = Lists.newArrayList(mailBean);
        EmailTransformer emailTransformer = new EmailTransformer(dataTable);
        DataTable dataTableEmail = emailTransformer.toDataTable(mailBeans);
        //
        //
        List<List<String>> expected = Lists.<List<String>>newArrayList(
                Lists.<String>newArrayList(SUJET_HEADER, MESSAGE_HEADER, A_HEADER, COPIE_CACHEE_HEADER, DE_HEADER,
                        PIECE_JOINTE_HEADER, REPONDRE_A_HEADER),
                Lists.<String>newArrayList("mon sujet", "mon message with non-breaking space", "to@mail.com",
                        "bcc@mail.com", "from@mail.com", null, "replyTo@mail.com"));
        assertThat(dataTableEmail.raw(), is(expected));
        verify(mailBean).getAttachment();
    }

    @Test
    public void testFileTransformer_many_mails_few_header() throws Exception {
        List<String> headers = Lists.newArrayList(A_HEADER, SUJET_HEADER);
        List<String> list01 = Lists
                .newArrayList("to@to.com", "Hello", "how are you ?");
        DataTable dataTable = DatatableUtils.getDatatable(headers, Lists.<List<String>>newArrayList(headers, list01));
        //
        MailBean mailBean01 = mock(MailBean.class);
        MailBean mailBean02 = mock(MailBean.class);
        MailBean mailBean03 = mock(MailBean.class);
        // mock
        when(mailBean01.getTo()).thenReturn("to@mail.com");
        when(mailBean01.getSubject()).thenReturn("mon sujet");
        when(mailBean02.getTo()).thenReturn("to02@mail.com");
        when(mailBean02.getSubject()).thenReturn("mon sujet 02");
        when(mailBean03.getTo()).thenReturn(null);
        when(mailBean03.getSubject()).thenReturn("mon sujet 03");
        //
        Collection<MailBean> mailBeans = Lists.newArrayList(mailBean02, mailBean01, mailBean03);
        EmailTransformer emailTransformer = new EmailTransformer(dataTable);
        DataTable dataTableEmail = emailTransformer.toDataTable(mailBeans);
        //
        List<List<String>> expected = Lists
                .<List<String>>newArrayList(Lists.<String>newArrayList(A_HEADER, SUJET_HEADER),
                        Lists.<String>newArrayList(null, "mon sujet 03"),
                        Lists.<String>newArrayList("to02@mail.com", "mon sujet 02"),
                        Lists.<String>newArrayList("to@mail.com", "mon sujet"));
        assertThat(dataTableEmail.raw(), is(expected));
    }

    @Test(expected = IllegalStateException.class)
    public void testFileTransformer_mails_ok_bad_header() throws Exception {
        List<String> headers = Lists.newArrayList("bad header", "too bad");
        List<String> list01 = Lists.newArrayList("boom", "badaboum");
        DataTable dataTable = DatatableUtils.getDatatable(headers, Lists.<List<String>>newArrayList(headers, list01));
        //
        MailBean mailBean = mock(MailBean.class);
        // mock
        when(mailBean.getFrom()).thenReturn("from@mail.com");
        when(mailBean.getTo()).thenReturn("to@mail.com");
        when(mailBean.getBcc()).thenReturn("bcc@mail.com");
        when(mailBean.getSubject()).thenReturn("mon sujet");
        when(mailBean.getBody()).thenReturn("mon message");
        when(mailBean.getReplyTo()).thenReturn("replyTo@mail.com");
        //
        Collection<MailBean> mailBeans = Lists.newArrayList(mailBean);
        EmailTransformer emailTransformer = new EmailTransformer(dataTable);
        emailTransformer.toDataTable(mailBeans);
    }
}
