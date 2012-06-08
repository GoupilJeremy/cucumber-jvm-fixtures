//package steps.rest;
//
//import static org.hamcrest.core.Is.is;
//import static org.junit.Assert.assertThat;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//
//import org.junit.Test;
//
//public class RestWebappTest {
//    @Test
//    public void transformToListOfIds() throws Exception {
//        RestWebapp restWebapp = new RestWebapp();
//        List<LinkedHashMap<String, Object>> programExpected = new ArrayList<LinkedHashMap<String, Object>>();
//        LinkedHashMap<String, Object> program1 = new LinkedHashMap<String, Object>();
//        program1.put("id", "141");
//        program1.put("description", "ttttthouuuuuuuuuu!");
//        programExpected.add(program1);
//        LinkedHashMap<String, Object> program2 = new LinkedHashMap<String, Object>();
//        program2.put("id", "142");
//        program2.put("description", "iiiiiiiiiiiiihhh!");
//        programExpected.add(program2);
//
//        final List<String> ids = restWebapp.transformToListOfIds(programExpected);
//
//        assertThat(ids.toString().contains("141"), is(true));
//        assertThat(ids.toString().contains("142"), is(true));
//    }
//}
