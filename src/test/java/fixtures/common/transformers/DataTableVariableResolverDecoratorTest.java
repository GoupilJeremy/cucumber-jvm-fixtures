package fixtures.common.transformers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cucumber.table.DataTable;
import fixtures.common.datatable.DatatableUtils;
import junit.framework.TestCase;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static fixtures.common.transformers.EmailTransformer.MESSAGE_HEADER;
import static fixtures.common.transformers.EmailTransformer.SUJET_HEADER;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DataTableVariableResolverDecoratorTest {

	@Test  (expected = IllegalArgumentException.class)
	public void testConstructor_noContext_datatable_is_null() throws Exception {
		DataTable datatable = null;
		new DataTableVariableResolverDecorator(datatable);
	}

	@Test
	public void test_noContext_nominalCase() throws Exception {
		 List<String> headers = Lists.newArrayList("ID", "DATE");
        List<String> list01 = Lists.newArrayList("id24", "2012/${moisNumeric}");
        DataTable dataTable = DatatableUtils.getDatatable(headers, Lists.<List<String>>newArrayList(headers, list01));
		//
		DataTableVariableResolverDecorator resolverDecorator = new DataTableVariableResolverDecorator(dataTable);
		DataTable dataTableDecorated = resolverDecorator.getDataTableDecorated();
		//
		String expectedMonth = DateTime.now().toString("MM");
		List<List<String>> expected = Lists
                .<List<String>>newArrayList(Lists.<String>newArrayList("ID", "DATE"),
                        Lists.<String>newArrayList("id24", "2012/"+expectedMonth));
        assertThat(dataTableDecorated.raw(), is(expected));
	}

	@Test
	public void test_withContext_nominalCase() throws Exception {
		 List<String> headers = Lists.newArrayList("ID", "DATE");
        List<String> list01 = Lists.newArrayList("id21", "2012/${moisNumeric}");
        DataTable dataTable = DatatableUtils.getDatatable(headers, Lists.<List<String>>newArrayList(headers, list01));
		//
		Map<String, String> context = Maps.newHashMap();
		context.put("id23","10005");
		context.put("id21","587");
		DataTableVariableResolverDecorator resolverDecorator = new DataTableVariableResolverDecorator(dataTable,context);
		DataTable dataTableDecorated = resolverDecorator.getDataTableDecorated();
		//
		String expectedMonth = DateTime.now().toString("MM");
		List<List<String>> expected = Lists
                .<List<String>>newArrayList(Lists.<String>newArrayList("ID", "DATE"),
                        Lists.<String>newArrayList("587", "2012/"+expectedMonth));
        assertThat(dataTableDecorated.raw(), is(expected));
	}


	@Test
	public void test_withContext_nominalCase_noVariableInDatatable() throws Exception {
		 List<String> headers = Lists.newArrayList("ID", "DATE");
        List<String> list01 = Lists.newArrayList("254", "2012/08");
        DataTable dataTable = DatatableUtils.getDatatable(headers, Lists.<List<String>>newArrayList(headers, list01));
		//
		Map<String, String> context = Maps.newHashMap();
		context.put("id23","10005");
		context.put("id21","587");
		DataTableVariableResolverDecorator resolverDecorator = new DataTableVariableResolverDecorator(dataTable,context);
		DataTable dataTableDecorated = resolverDecorator.getDataTableDecorated();
		//
		List<List<String>> expected = Lists
                .<List<String>>newArrayList(Lists.<String>newArrayList("ID", "DATE"),
                        Lists.<String>newArrayList("254", "2012/08"));
        assertThat(dataTableDecorated.raw(), is(expected));
	}

		@Test
	public void testConstructor_datatable_ok_map_is_null() throws Exception {
		List<String> headers = Lists.newArrayList("ID", "DATE");
        List<String> list01 = Lists.newArrayList("id24", "2012/${moisNumeric}");
        DataTable dataTable = DatatableUtils.getDatatable(headers, Lists.<List<String>>newArrayList(headers, list01));
		Map<String, String> context = null;
		//
		DataTableVariableResolverDecorator resolverDecorator = new DataTableVariableResolverDecorator(dataTable,context);
		DataTable dataTableDecorated = resolverDecorator.getDataTableDecorated();
		//
		String expectedMonth = DateTime.now().toString("MM");
		List<List<String>> expected = Lists
                .<List<String>>newArrayList(Lists.<String>newArrayList("ID", "DATE"),
                        Lists.<String>newArrayList("id24", "2012/"+expectedMonth));
        assertThat(dataTableDecorated.raw(), is(expected));
	}

}
