package fixtures.common.elasticsearch;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import fixtures.common.rows.RowToObject;
import org.elasticsearch.common.xcontent.XContentBuilder;

public abstract class Document extends RowToObject<ElasticSearchWrapper, XContentBuilder> {
    public Document(Map<String, Integer> headers, final List<String> row, final ElasticSearchWrapper dataSource) {
        super(headers, row, dataSource);
    }

    @Override
    protected XContentBuilder mapRowToObject() {
        final Map<String, Object> context = getContext();

        for (String mandatoryHeader : getMandatoryHeaders()) {
            if (Strings.isNullOrEmpty(getValue(mandatoryHeader))) {
                checkArgument(context!=null && context.get(mandatoryHeader) != null, mandatoryHeader + " is mandatory");
            }
        }

        return getXContentBuilder();
    }

    protected abstract Collection<String> getMandatoryHeaders();

    protected abstract XContentBuilder getXContentBuilder();
}
