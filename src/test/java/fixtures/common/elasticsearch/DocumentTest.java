package fixtures.common.elasticsearch;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import fixtures.common.rows.RowToObjectTest;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DocumentTest extends RowToObjectTest {
    private static final String MANDATORY_HEADER = "needed";

    @Mock
    private Document documentMock;

    @Mock
    private ElasticSearchWrapper elasticSearchWrapper;

    private Document document;

    private XContentBuilder xContentBuilder;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        xContentBuilder = jsonBuilder();
    }

    @Test
    public void test_mapRowToObject_call_getXContentBuilder() throws IOException {

        //given
        given(documentMock.getXContentBuilder()).willReturn(xContentBuilder);

        //when
        final XContentBuilder json = documentMock.mapRowToObject();

        //then
        verify(documentMock).getXContentBuilder();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_mapRowToObject_check_mandatory_headers() throws IOException {

        // given
        Map<String, Integer> headers = new HashMap<String, Integer>();
        headers.put(FIRST_COLUMN_NAME, FIRST_COLUMN_INDEX);
        headers.put(SECOND_COLUMN_NAME, SECOND_COLUMN_INDEX);
        List<String> row = new ArrayList<String>();
        row.add(StringUtils.EMPTY);
        row.add(FIRST_COLUMN_VALUE);

        document = new Document(headers,row,elasticSearchWrapper) {
            @Override
            protected Collection<String> getMandatoryHeaders() {
                Collection<String> headers = Lists.newArrayList();
                headers.add(MANDATORY_HEADER);
                return headers;
            }

            @Override
            protected XContentBuilder getXContentBuilder() {
                return xContentBuilder;
            }
        };

        //when
        document.mapRowToObject();
    }
}
