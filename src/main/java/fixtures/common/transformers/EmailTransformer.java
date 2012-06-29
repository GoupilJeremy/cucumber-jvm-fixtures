package fixtures.common.transformers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;
import cucumber.table.DataTable;
import fixtures.common.mail.MailBean;
import gherkin.formatter.model.Comment;
import gherkin.formatter.model.DataTableRow;

public class EmailTransformer extends AbstractDataTableTransformer<Collection<MailBean>> {
    public static final String SUJET_HEADER = "sujet";

    public static final String MESSAGE_HEADER = "message";

    public static final String REPONDRE_A_HEADER = "répondre à";

    public static final String DE_HEADER = "de";

    public static final String A_HEADER = "à";

    public static final String COPIE_CACHEE_HEADER = "copie cachée";

    public static final String PIECE_JOINTE_HEADER = "piece jointe";

    public EmailTransformer(DataTable dataTable) {
        super(dataTable);
    }

    @Override
    protected List<DataTableRow> buildRowForDataTable(final Collection<MailBean> mailBeans,
            final List<DataTableRow> rows)  {

        List<MailBean> sorted = Lists.newArrayList(mailBeans);
        Collections.sort(sorted, new MailBeanComparator());

        int line = 0;
        for (MailBean mailBean : sorted) {
            List<String> cells = new ArrayList<String>();
            for (String headerValue : headersAsCells) {
                if (headerValue.equalsIgnoreCase(SUJET_HEADER)) {
                    cells.add(mailBean.getSubject());
                } else if (headerValue.equalsIgnoreCase(MESSAGE_HEADER)) {
                    cells.add(Label.cleanLabel(mailBean.getBody()));
                } else if (headerValue.equalsIgnoreCase(REPONDRE_A_HEADER)) {
                    cells.add(mailBean.getReplyTo());
                } else if (headerValue.equalsIgnoreCase(DE_HEADER)) {
                    cells.add(mailBean.getFrom());
                } else if (headerValue.equalsIgnoreCase(A_HEADER)) {
                    cells.add(mailBean.getTo());
                } else if (headerValue.equalsIgnoreCase(COPIE_CACHEE_HEADER)) {
                    cells.add(mailBean.getBcc());
                } else if (headerValue.equalsIgnoreCase(PIECE_JOINTE_HEADER)) {
                    cells.add(mailBean.getAttachment());
                } else {
                    throw new IllegalStateException(
                            "le header '" + headerValue + "' n'est pas géré par EmailTransformer");
                }
            }
            rows.add(new DataTableRow(new ArrayList<Comment>(), cells, line));

            line++;
        }
        return rows;
    }

    private class MailBeanComparator implements Comparator<MailBean> {
        @Override
        public int compare(final MailBean mailBean01, final MailBean mailBean02) {
            return mailBean01.getTo().compareTo(mailBean02.getTo());
        }
    }
}
