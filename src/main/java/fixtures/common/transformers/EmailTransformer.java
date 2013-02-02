package fixtures.common.transformers;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import cucumber.api.DataTable;
import fixtures.common.mail.MailBean;
import org.elasticsearch.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class EmailTransformer extends AbstractDataTableBuilder<MailBean> implements Function<MailBean, Map<String, String>> {
    public static final String SUJET_HEADER = "sujet";

    public static final String MESSAGE_HEADER = "message";

    public static final String REPONDRE_A_HEADER = "répondre à";

    public static final String DE_HEADER = "de";

    public static final String A_HEADER = "à";

    public static final String COPIE_CACHEE_HEADER = "copie cachée";

    public static final String PIECE_JOINTE_HEADER = "piece jointe";

    private EmailTransformer(final DataTable dataTableFromFeatureFileToCompare,
            final List<MailBean> collection) {
        super(dataTableFromFeatureFileToCompare, collection);
    }



    public static EmailTransformer from(final DataTable dataTableFromFeatureFileToCompare,
                final List<MailBean> collection){
        EmailTransformer emailTransformer = new EmailTransformer(dataTableFromFeatureFileToCompare, collection);
        emailTransformer.compareWith(new MailBeanComparator());
        emailTransformer.sortBy(A_HEADER);
        return emailTransformer;
    }


    @Override
    public Map<String, String> apply(final MailBean mailBean) {
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

    private static class MailBeanComparator extends LineComparator<MailBean> {
        @Override
        protected String getValue(MailBean row,String columnName) {
            return Strings.nullToEmpty(row.getTo());
        }
    }
}
