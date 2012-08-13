package fixtures.common.transformers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import cucumber.table.DataTable;
import fixtures.common.mail.MailBean;
import fixtures.common.transformers.model.EmailPropertyEnum;
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
            final List<DataTableRow> rows) {
        Preconditions.checkArgument(mailBeans != null, "la liste de mail ne peut ëtre null");

        List<MailBean> sorted = Lists.newArrayList(mailBeans);
        Collections.sort(sorted, new MailBeanComparator());

        int line = 0;
        for (MailBean mailBean : sorted) {
            List<String> cells = new ArrayList<String>();
            for (String headerValue : headersAsCells) {
                cells.addAll(populate(mailBean, headerValue));
            }
            rows.add(new DataTableRow(new ArrayList<Comment>(), cells, line));
            line++;
        }
        return rows;
    }

    private List<String> populate(final MailBean mailBean, final String headerValue) {
        EmailPropertyEnum emailProperty = EmailPropertyEnum.getEmailPropertyFromLabel(headerValue);
        if (emailProperty==null) {
            throw new IllegalStateException("le header '" + headerValue + "' n'est pas géré par EmailTransformer");
        }
        return fillCells(mailBean, emailProperty);
    }

    private List<String> fillCells(final MailBean mailBean, final EmailPropertyEnum emailProperty) {
        List<String> cells = Lists.newArrayList();
        switch (emailProperty) {
            case SUJET_HEADER:
                cells.add(mailBean.getSubject());
                break;
            case MESSAGE_HEADER:
                cells.add(Label.cleanLabel(mailBean.getBody()));
                break;
            case REPONDRE_A_HEADER:
                cells.add(mailBean.getReplyTo());
                break;
            case DE_HEADER:
                cells.add(mailBean.getFrom());
                break;
            case A_HEADER:
                cells.add(mailBean.getTo());
                break;
            case COPIE_CACHEE_HEADER:
                cells.add(mailBean.getBcc());
                break;
            case PIECE_JOINTE_HEADER:
                cells.add(mailBean.getAttachment());
                break;
            default:
                break;
        }

        return cells;
    }

    private class MailBeanComparator implements Comparator<MailBean> {
        @Override
        public int compare(final MailBean mailBean01, final MailBean mailBean02) {
            String mailTo01 = Strings.nullToEmpty(mailBean01.getTo());
            String mailTo02 = Strings.nullToEmpty(mailBean02.getTo());
            return mailTo01.compareTo(mailTo02);
        }
    }


}
