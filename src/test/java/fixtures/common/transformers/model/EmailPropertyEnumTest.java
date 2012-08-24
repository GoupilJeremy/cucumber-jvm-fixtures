package fixtures.common.transformers.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class EmailPropertyEnumTest {
    @Test
    public void testGetEnumPropertyFromLabel_label_is_null() throws Exception {
        String label =null;
        EmailPropertyEnum emailPropertyFromLabel = EmailPropertyEnum.getEmailPropertyFromLabel(label);
        //
        assertThat(emailPropertyFromLabel,is(nullValue()));
    }


     @Test
    public void testGetEnumPropertyFromLabel_label_is_known() throws Exception {
        String label =EmailPropertyEnum.DE_HEADER.getLabel();
        EmailPropertyEnum emailPropertyFromLabel = EmailPropertyEnum.getEmailPropertyFromLabel(label);
        //
        assertThat(emailPropertyFromLabel,is(EmailPropertyEnum.DE_HEADER));
    }

    @Test
    public void testGetEnumPropertyFromLabel_label_bad_accent_is_known() throws Exception {
        String label ="a"; // normalement "à"
        EmailPropertyEnum emailPropertyFromLabel = EmailPropertyEnum.getEmailPropertyFromLabel(label);
        //
        assertThat(emailPropertyFromLabel,is(nullValue()));
    }
}
