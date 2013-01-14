package fixtures.common.rows;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cucumber.api.DataTable;
import cucumber.runtime.CucumberException;
import fixtures.common.RowToObjectDataSource;
import fixtures.common.datatable.DatatableUtils;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.util.*;

public class RowsToObjectTest {

	private static DataTable DEFAULT_DATATABLE = DatatableUtils.getDatatable(Lists.newArrayList("default"), Arrays.asList(Arrays.asList("default")));

	// =============================================================================================
	// executeInRows
	// =============================================================================================

	@Test(expected = IllegalArgumentException.class)
	public void test_executeInRows_noData() throws Exception {
		RowToObjectDataSource rowToObjectDataSource = new MyRowToObjectDataSource();
		Class<? extends RowToObject> clazz = RowToObject.class;

		RowsToObject rowsToObject = new RowsToObject(DataTable.create(Lists.newArrayList()), rowToObjectDataSource, clazz);
		rowsToObject.executeInRows();
	}

	@Test
	public void test_executeInRows_just_header() throws Exception {
		RowToObjectDataSource rowToObjectDataSource = new MyRowToObjectDataSource();
		Class<InnerRowToObject> clazz = InnerRowToObject.class;

		List<String> header = Lists.newArrayList("id", "name", "value");
		List<List<String>> values = new ArrayList<List<String>>();
		values.add(header);

		DataTable datatable = DatatableUtils.getDatatable(header, values);
		RowsToObject rowsToObject = new RowsToObject(datatable, rowToObjectDataSource, clazz);
		List results = rowsToObject.executeInRows();
		//
		assertThat(results.isEmpty(), is(true));
	}

	@Test
	public void test_executeInRows_class_ok_constructor_found_and_instanciable() throws Exception {
		RowToObjectDataSource rowToObjectDataSource = new MyRowToObjectDataSource();
		Class<InnerRowToObject> clazz = InnerRowToObject.class;
		//
		List<String> header = Lists.newArrayList("id", "name", "value");
		List<List<String>> values = new ArrayList<List<String>>();
		values.add(header);
		values.add(Lists.newArrayList("154", "bob", "here"));

		DataTable datatable = DatatableUtils.getDatatable(header, values);
		RowsToObject rowsToObject = new RowsToObject(datatable, rowToObjectDataSource, clazz);
		// set context
		Map<Object, Object> context = Maps.newHashMap();
		context.put("ctx", "val");
		context.put("ctx2", 1587);
		rowsToObject.setContext(context);
		//
		Object[] argsNew = new Object[2];
		argsNew[0] = "args1";
		argsNew[1] = 12;
		rowsToObject.setArgs(argsNew);
		//
		List results = rowsToObject.executeInRows();
		//
		assertThat((List<String>) results, hasItems("my result:args = [args1, 12] - context = {ctx2=1587, ctx=val}"));
	}

	@Test(expected = CucumberException.class)
	public void test_executeInRows_class_ok_constructor_not_found() throws Exception {
		RowToObjectDataSource rowToObjectDataSource = new RowToObjectDataSource() {
		};
		// class here need MyRowToObjectDataSource and not RowToObjectDataSource in constructor
		Class<InnerRowToObject> clazz = InnerRowToObject.class;
		//
		List<String> header = Lists.newArrayList("id", "name", "value");
		List<List<String>> values = new ArrayList<List<String>>();
		values.add(header);
		values.add(Lists.newArrayList("154", "bob", "here"));

		DataTable datatable = DatatableUtils.getDatatable(header, values);
		RowsToObject rowsToObject = new RowsToObject(datatable, rowToObjectDataSource, clazz);
		rowsToObject.executeInRows();
	}

	@Test(expected = CucumberException.class)
	public void test_executeInRows_class_ok_constructor_found_and_not_instanciable() throws Exception {
		RowToObjectDataSource rowToObjectDataSource = new MyRowToObjectDataSource();
		Class<InnerRowToObjectNotInstanciable> clazz = InnerRowToObjectNotInstanciable.class;
		//
		List<String> header = Lists.newArrayList("id", "name", "value");
		List<List<String>> values = new ArrayList<List<String>>();
		values.add(header);
		values.add(Lists.newArrayList("154", "bob", "here"));

		DataTable datatable = DatatableUtils.getDatatable(header, values);
		RowsToObject rowsToObject = new RowsToObject(datatable, rowToObjectDataSource, clazz);
		rowsToObject.executeInRows();
	}

	// =============================================================================================
	// setArgs
	// =============================================================================================

	@Test
	public void testSetArgs_null_array() throws Exception {
		RowsToObject rowsToObject = new RowsToObject(DEFAULT_DATATABLE, null, null);
		Object[] objects = null;
		//
		rowsToObject.setArgs(objects);
		//
		assertThat(rowsToObject.args, is(nullValue()));
	}

	@Test
	public void testSetArgs_empty_array() throws Exception {
		RowsToObject rowsToObject = new RowsToObject(DEFAULT_DATATABLE, null, null);
		Object[] objects = new Object[0];
		//
		rowsToObject.setArgs(objects);
		//
		assertThat(rowsToObject.args, is(new Object[0]));
	}

	@Test
	public void testSetArgs_array_with_one_data() throws Exception {
		RowsToObject rowsToObject = new RowsToObject(DEFAULT_DATATABLE, null, null);
		Object[] objects = new Object[]{"one data"};
		//
		rowsToObject.setArgs(objects);
		//
		assertThat(rowsToObject.args, is(new Object[]{"one data"}));
	}

	@Test
	public void testSetArgs_array_with_many_data() throws Exception {
		RowsToObject rowsToObject = new RowsToObject(DEFAULT_DATATABLE, null, null);
		Object[] objects = new Object[]{"one data", "two data"};
		//
		rowsToObject.setArgs(objects);
		//
		assertThat(rowsToObject.args, is(new Object[]{"one data", "two data"}));
	}

	@Test
	public void testSetArgs_array_with_many_data_less_data_after() throws Exception {
		RowsToObject rowsToObject = new RowsToObject(DEFAULT_DATATABLE, null, null);
		Object[] objects = new Object[]{"one data", "two data"};
		//
		rowsToObject.setArgs(objects);
		assertThat(rowsToObject.args, is(new Object[]{"one data", "two data"}));
		//
		Object[] objects02 = new Object[]{"one data2"};
		rowsToObject.setArgs(objects02);
		assertThat(rowsToObject.args, is(new Object[]{"one data2"}));
	}

	@Test
	public void testSetArgs_array_with_many_data_more_data_after() throws Exception {
		RowsToObject rowsToObject = new RowsToObject(DEFAULT_DATATABLE, null, null);
		Object[] objects = new Object[]{"one data", "two data"};
		//
		rowsToObject.setArgs(objects);
		assertThat(rowsToObject.args, is(new Object[]{"one data", "two data"}));
		//
		Object[] objects02 = new Object[]{"one data2", "two data2", "three data2"};
		rowsToObject.setArgs(objects02);
		assertThat(rowsToObject.args, is(new Object[]{"one data2", "two data2", "three data2"}));
	}

	// =============================================================================================
	// Utils
	// =============================================================================================


	private static class MyRowToObjectDataSource implements RowToObjectDataSource {
	}

	private static class InnerRowToObject extends RowToObject {

		public InnerRowToObject(Map<String, Integer> headers, List<String> row, MyRowToObjectDataSource dataSource) {
			super(headers, row, dataSource);
		}

		@Override
		protected Object mapRowToObject() {
			StringBuilder stringBuilder = new StringBuilder("my result:");
			stringBuilder.append("args = ").append(Lists.newArrayList(getArgs()));
			stringBuilder.append(" - context = ").append(getContext());
			return stringBuilder.toString();
		}
	}

	private static class InnerRowToObjectNotInstanciable extends RowToObject {

		public InnerRowToObjectNotInstanciable(Map<String, Integer> headers, List<String> row, MyRowToObjectDataSource dataSource) {
			super(headers, row, dataSource);
			throw new RuntimeException("stop instance");
		}

		@Override
		protected Object mapRowToObject() {
			return "my result not instanciable";
		}
	}

}
