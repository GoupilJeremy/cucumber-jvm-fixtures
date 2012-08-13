package fixtures.common.elasticsearch;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
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

    private static final String MANDATORY_VALUE = "value needed";

    private static final String SECOND_COLUMN_VALUE = "toto";

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
        // given
        Map<String, Integer> headers = new HashMap<String, Integer>();
        headers.put(MANDATORY_HEADER, FIRST_COLUMN_INDEX);
        headers.put(SECOND_COLUMN_NAME, SECOND_COLUMN_INDEX);
        List<String> row = new ArrayList<String>();
        row.add(MANDATORY_VALUE);
        row.add(SECOND_COLUMN_VALUE);

        document = buildDocument(headers, row);
        Document spy = spy(document);

        //when
        final XContentBuilder json = spy.mapRowToObject();

        //then
        verify(spy).getXContentBuilder();
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

        document = buildDocument(headers, row);

        //when
        document.mapRowToObject();
    }

    @Test
    public void test_mapRowToObject_call_getXContentBuilder_with_context() throws IOException {
        // given
        Map<String, Integer> headers = new HashMap<String, Integer>();
        headers.put(SECOND_COLUMN_NAME, FIRST_COLUMN_INDEX);

        List<String> row = new ArrayList<String>();
        row.add(SECOND_COLUMN_VALUE);

        Map<String, Object> context = new HashMap<String, Object>();
        context.put(MANDATORY_HEADER, MANDATORY_VALUE);

        document = buildDocument(headers, row);
        document.setContext(context);
        Document spy = spy(document);

        //when
        final XContentBuilder json = spy.mapRowToObject();

        //then
        verify(spy).getXContentBuilder();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_mapRowToObject_call_getXContentBuilder_with_context_with_data_null() throws IOException {
        // given
        Map<String, Integer> headers = new HashMap<String, Integer>();
        headers.put(SECOND_COLUMN_NAME, FIRST_COLUMN_INDEX);

        List<String> row = new ArrayList<String>();
        row.add(SECOND_COLUMN_VALUE);

        Map<String, Object> context = new HashMap<String, Object>();
        context.put(MANDATORY_HEADER, null);

        document = buildDocument(headers, row);
        document.setContext(context);
        Document spy = spy(document);

        //when
        try {
            spy.mapRowToObject();
        } finally {
            //then
            verify(spy, never()).getXContentBuilder();
        }
    }

    private Document buildDocument(final Map<String, Integer> headers, final List<String> row) {
        return new Document(headers, row, elasticSearchWrapper) {
            @Override
            protected Collection<String> getMandatoryHeaders() {
                Collection<String> headers = buildMandatoryHeaders();
                return headers;
            }

            @Override
            protected XContentBuilder getXContentBuilder() {
                return xContentBuilder;
            }
        };
    }

    private Collection<String> buildMandatoryHeaders() {
        Collection<String> headers = Lists.newArrayList();
        headers.add(MANDATORY_HEADER);
        return headers;
    }
}
