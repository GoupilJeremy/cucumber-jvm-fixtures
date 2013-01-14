package fixtures.common.transformers;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import cucumber.api.DataTable;
import fixtures.common.mail.MailBean;
import gherkin.formatter.model.Comment;
import gherkin.formatter.model.DataTableRow;
import org.elasticsearch.common.collect.Maps;

import java.util.*;

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
	        Map<String,String> mailToRow = mailToRow(mailBean);
            for (String headerValue : headersAsCells) {
	            if (!mailToRow.containsKey(headerValue)) {
                    throw new IllegalStateException("le header '" + headerValue + "' n'est pas géré par EmailTransformer");
                }
                cells.add(mailToRow.get(headerValue));
            }
            rows.add(new DataTableRow(new ArrayList<Comment>(), cells, line));
            line++;
        }
        return rows;
    }

	private Map<String, String> mailToRow(MailBean mailBean) {
		Map<String, String> mapMailBean = Maps.newHashMap();
		mapMailBean.put(A_HEADER, mailBean.getTo());
		mapMailBean.put(DE_HEADER, mailBean.getFrom());
		mapMailBean.put(COPIE_CACHEE_HEADER, mailBean.getBcc());
		mapMailBean.put(REPONDRE_A_HEADER, mailBean.getReplyTo());
		mapMailBean.put(SUJET_HEADER, mailBean.getSubject());
		mapMailBean.put(MESSAGE_HEADER, Label.cleanLabel(mailBean.getBody()));
		mapMailBean.put(PIECE_JOINTE_HEADER, mailBean.getAttachment());
		return mapMailBean;
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
