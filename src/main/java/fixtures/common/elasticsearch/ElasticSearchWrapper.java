package fixtures.common.elasticsearch;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Random;

import fixtures.common.RowToObjectDataSource;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.node.NodeBuilder;

public class ElasticSearchWrapper implements RowToObjectDataSource {
    private static final int BIG_ID_INTERVAL = 1000000;

    private static final boolean HOSTING_NO_DATA = true;

    private Client client;


    private String index;

    private String type;

    private static Random random = new Random();

    public ElasticSearchWrapper(String index, String type, Writer mapping,
            final String templateName) throws IOException {
        this.client = NodeBuilder.nodeBuilder().local(true).client(HOSTING_NO_DATA).data(false).node().client();
        this.index = index;
        this.type = type;
        if (client.admin().indices().prepareExists(index).execute().actionGet().exists()) {
            client.admin().indices().prepareDelete(index).execute().actionGet();
        }
        client.admin().indices().prepareCreate(index).execute().actionGet();
        client.admin().indices().preparePutTemplate(index).setTemplate(templateName).setSource(mapping.toString())
                .execute().actionGet();
    }

    public BulkResponse persistAndIndex(final List<XContentBuilder> documents) {
        final BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        for (XContentBuilder document : documents) {
            final IndexRequestBuilder indexRequestBuilder = indexRow(client, document);
            bulkRequestBuilder.add(indexRequestBuilder);
        }
        BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
        client.admin().indices().refresh(new RefreshRequest(index)).actionGet();
        return bulkResponse;
    }

    private IndexRequestBuilder indexRow(final Client client, XContentBuilder xContentBuilder) {

        return client.prepareIndex(index, type, random.nextInt(BIG_ID_INTERVAL) + "").setSource(xContentBuilder);
    }
}
