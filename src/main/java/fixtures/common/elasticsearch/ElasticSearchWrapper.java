package fixtures.common.elasticsearch;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Random;

import fixtures.common.RowToObjectDataSource;
import org.elasticsearch.action.admin.cluster.node.shutdown.NodesShutdownRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearchWrapper implements RowToObjectDataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchWrapper.class);

    private static final int BIG_ID_INTERVAL = 1000000;

    private static final String ALL_INDICES = "_all";

    private Client client;

    private String index;

    private String type;

    private static Random random = new Random();

    public ElasticSearchWrapper(){

    }

    public ElasticSearchWrapper(Client client, String index, String type) throws IOException {
        this.client = client;
        this.index = index;
        LOGGER.info(index);
        this.type = type;
    }

    public void deleteAllIndices() throws InterruptedException {

        client.admin().indices().delete(new DeleteIndexRequest(ALL_INDICES));
        client.admin().indices().flush(new FlushRequest(ALL_INDICES));
        Thread.sleep(5000);
    }

    public void shutdownAllNodes() throws InterruptedException {

        client.admin().cluster().nodesShutdown(new NodesShutdownRequest("_all"));

    }

    public void initIndex(final Writer mapping, final String templateName) {
        final IndicesAdminClient adminClient = client.admin().indices();
        if (!adminClient.prepareExists(index).execute().actionGet().exists()) {
            adminClient.prepareCreate(index).execute().actionGet();
            adminClient.preparePutTemplate(templateName).setSource(mapping.toString()).execute()
                    .actionGet();
        }
    }

    public BulkResponse persistAndIndex(final List<XContentBuilder> documents) {
        final BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        for (XContentBuilder document : documents) {
            final IndexRequestBuilder indexRequestBuilder = indexRow(document);
            bulkRequestBuilder.add(indexRequestBuilder);
        }

        BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
        client.admin().indices().refresh(new RefreshRequest(index)).actionGet();
        return bulkResponse;
    }

    private IndexRequestBuilder indexRow( XContentBuilder xContentBuilder) {
        return client.prepareIndex(index, type, random.nextInt(BIG_ID_INTERVAL) + "").setSource(xContentBuilder);
    }

    public void setClient(final Client client) {
        this.client = client;
    }
}
