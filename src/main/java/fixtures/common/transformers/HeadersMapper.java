package fixtures.common.transformers;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

class HeadersMapper {
    private EnumSet enumSet;



    Map<String,String> headersNamesAndReplacement = Maps.newHashMap();



    Map<String,String> headersNamesAndTypes = Maps.newHashMap();
    private final static Function<MapperContainer, String> GET_REPLACEMENT_COLUMN_NAME = new Function<MapperContainer, String>() {
        @Override
        public String apply(final MapperContainer input) {
            if (input == null) {
                return "";
            }
            return input.getReplacementColumnName();
        }
    };

    private final static Function<MapperContainer, String> GET_DATATABLE_COLUMN_NAME = new Function<MapperContainer, String>() {
        @Override
        public String apply(final MapperContainer input) {
            if (input == null) {
                return "";
            }
            return input.getDatatableColumnName();
        }
    };

    private final static Function<MapperContainer, String> GET_COLUMN_TYPE = new Function<MapperContainer, String>() {
        @Override
        public String apply(final MapperContainer input) {
            if (input == null) {
                return "0";
            }
            Integer integer = new Integer(input.getColumnType());
            return integer.toString();
        }
    };

    public HeadersMapper(Class myEnum) {
        Preconditions.checkArgument(myEnum != null, "la classe ne peut être null");
        Preconditions.checkArgument(myEnum.isEnum(), "la classe n'est pas une Enum");
        enumSet = EnumSet.allOf(myEnum);
        List<String> datatableColumnNames = getDatatableColumnNames();
        List<String> replacementColumnNames = getReplacementColumnNames();
        List<String> columnTypes = getDatatableColumnTypes();
        for (int i = 0; i < datatableColumnNames.size(); i++) {
            headersNamesAndReplacement.put(datatableColumnNames.get(i), replacementColumnNames.get(i));
            headersNamesAndTypes.put(datatableColumnNames.get(i), columnTypes.get(i));
        }
    }

     public HeadersMapper(List<String> headers) {
        Preconditions.checkArgument(headers != null, "headers are null");

//        List<String> columnTypes = getDatatableColumnTypes();
        for (int i = 0; i < headers.size(); i++) {
            headersNamesAndReplacement.put(headers.get(i), headers.get(i));
           headersNamesAndTypes.put(headers.get(i), "-1");
        }
    }

    public List<MapperContainer> getReplacementColumnToTable() {
        return new ArrayList<MapperContainer>(enumSet);
    }

    public List<String> getReplacementColumnNames() {
        return getColumns(GET_REPLACEMENT_COLUMN_NAME);
    }

    public List<String> getDatatableColumnNames() {
        return getColumns(GET_DATATABLE_COLUMN_NAME);
    }

    public List<String> getDatatableColumnTypes() {
        return getColumns(GET_COLUMN_TYPE);
    }

    private List<String> getColumns(Function<MapperContainer, String> function) {
        List<String> results = Lists.newArrayList();
        results.addAll(Collections2.transform(enumSet, function));
        return results;
    }

    public List<String> replaceColumns(List<String> headers){
        ArrayList<String> replacedHeaders = Lists.newArrayList();
        for (String header : headers) {
            String replacedHeader = headersNamesAndReplacement.get(header);
            if(replacedHeader!=null){
            replacedHeaders.add(replacedHeader);
            }else{
                replacedHeaders.add(header);
            }
        }
        return replacedHeaders;
    }

    public Map<String, String> getHeadersNamesAndReplacement() {
        return headersNamesAndReplacement;
    }

    public Map<String, String> getHeadersNamesAndTypes() {
        return headersNamesAndTypes;
    }
}
