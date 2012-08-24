package fixtures.common.transformers;

import static org.hamcrest.core.Is.is;

import java.util.List;

import com.google.common.collect.Lists;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Test;

public class RecursiveToStringStyleTest {
    @Test
    public void test_nominalCase() throws Exception {

        DummyObject dummyObject = new DummyObject(15, "quinze", Lists.<String>newArrayList("one", "two"));
        //
        String toString = ToStringBuilder.reflectionToString(dummyObject, RecursiveToStringStyle.MULTI_LINE_STYLE);
        //
        Assert.assertThat(toString,
                is("RecursiveToStringStyleTest.DummyObject[\n  id=15\n  label=quinze\n  infos=Object[][\n  {one,two}\n]\n  dummyObject=<null>\n]"));
    }

    @Test
    public void test_nominalCase_with_recursive() throws Exception {

        DummyObject dummyObject = new DummyObject(15, "quinze", Lists.<String>newArrayList("one", "two"));
        DummyObject dummyObject02 = new DummyObject(15, "quinze", Lists.<String>newArrayList("one", "two"));
        dummyObject.setDummyObject(dummyObject02);
        //
        String toString = ToStringBuilder.reflectionToString(dummyObject, RecursiveToStringStyle.MULTI_LINE_STYLE);
        //
        Assert.assertThat(toString,
                is("RecursiveToStringStyleTest.DummyObject[\n  id=15\n  label=quinze\n  infos=Object[][\n  {one,two}\n]\n  dummyObject=RecursiveToStringStyleTest.DummyObject[\n  id=15\n  label=quinze\n  infos=Object[][\n  {one,two}\n]\n  dummyObject=<null>\n]\n]"));
    }

    @Test
    public void test_nominalCase_with_recursive_limit() throws Exception {

        DummyObject dummyObject = new DummyObject(15, "quinze", Lists.<String>newArrayList("one", "two"));
        DummyObject dummyObject02 = new DummyObject(16, "seize", Lists.<String>newArrayList("one", "two"));
        DummyObject dummyObject03 = new DummyObject(17, "dix-sept", Lists.<String>newArrayList("one", "two"));
        DummyObject dummyObject04 = new DummyObject(18, "dix-huit", Lists.<String>newArrayList("one", "two"));
        DummyObject dummyObject05 = new DummyObject(19, "dix-neuf", Lists.<String>newArrayList("one", "two"));
        DummyObject dummyObject06 = new DummyObject(20, "vingt", Lists.<String>newArrayList("one", "two"));
        DummyObject dummyObject07 = new DummyObject(21, "vingt et un", Lists.<String>newArrayList("one", "two"));
        dummyObject.setDummyObject(dummyObject02);
        dummyObject02.setDummyObject(dummyObject03);
        dummyObject03.setDummyObject(dummyObject04);
        dummyObject04.setDummyObject(dummyObject05);
        dummyObject05.setDummyObject(dummyObject06);
        dummyObject06.setDummyObject(dummyObject07);
        //
        String toString = ToStringBuilder.reflectionToString(dummyObject, RecursiveToStringStyle.MULTI_LINE_STYLE);
        //
        Assert.assertThat(toString.startsWith(
                "RecursiveToStringStyleTest.DummyObject[\n  id=15\n  label=quinze\n  infos=Object[][\n  {one,two}\n]\n  dummyObject=RecursiveToStringStyleTest.DummyObject[\n  id=16\n  label=seize\n  infos=Object[][\n  {one,two}\n]\n  dummyObject=RecursiveToStringStyleTest.DummyObject[\n  id=17\n  label=dix-sept\n  infos=Object[][\n  {one,two}\n]\n  dummyObject=RecursiveToStringStyleTest.DummyObject[\n  id=18\n  label=dix-huit\n  infos=Object[][\n  {one,two}\n]\n  dummyObject=RecursiveToStringStyleTest.DummyObject[\n  id=19\n  label=dix-neuf\n  infos=Object[][\n  {one,two}\n]\n  dummyObject=RecursiveToStringStyleTest.DummyObject[\n  id=20\n  label=vingt\n  infos=Object[][\n  {one,two}\n]\n  dummyObject=fixtures.common.transformers.RecursiveToStringStyleTest$DummyObject@"),
                is(true));
    }

    @Test
    public void test_nominalCase_with_recursive_limit_() throws Exception {

        DummyObject dummyObject = new DummyObject(15, "quinze", Lists.<String>newArrayList("one", "two"));
        DummyObject dummyObject02 = new DummyObject(16, "seize", Lists.<String>newArrayList("one", "two"));
        DummyObject dummyObject03 = new DummyObject(17, "dix-sept", Lists.<String>newArrayList("one", "two"));
        DummyObject dummyObject04 = new DummyObject(18, "dix-huit", Lists.<String>newArrayList("one", "two"));
        DummyObject dummyObject05 = new DummyObject(19, "dix-neuf", Lists.<String>newArrayList("one", "two"));
        DummyObject dummyObject06 = new DummyObject(20, "vingt", Lists.<String>newArrayList("one", "two"));
        DummyObject dummyObject07 = new DummyObject(21, "vingt et un", Lists.<String>newArrayList("one", "two"));
        dummyObject.setDummyObject(dummyObject02);
        dummyObject02.setDummyObject(dummyObject03);
        dummyObject03.setDummyObject(dummyObject04);
        dummyObject04.setDummyObject(dummyObject05);
        dummyObject05.setDummyObject(dummyObject06);
        dummyObject06.setDummyObject(dummyObject07);
        //
        String toString = ToStringBuilder
                .reflectionToString(dummyObject, RecursiveToStringStyle.MULTI_LINE_STYLE_FULLY_RECURSIVE);
        //
        Assert.assertThat(toString,
                is("RecursiveToStringStyleTest.DummyObject[\n  id=15\n  label=quinze\n  infos=Object[][\n  {one,two}\n]\n  dummyObject=RecursiveToStringStyleTest.DummyObject[\n  id=16\n  label=seize\n  infos=Object[][\n  {one,two}\n]\n  dummyObject=RecursiveToStringStyleTest.DummyObject[\n  id=17\n  label=dix-sept\n  infos=Object[][\n  {one,two}\n]\n  dummyObject=RecursiveToStringStyleTest.DummyObject[\n  id=18\n  label=dix-huit\n  infos=Object[][\n  {one,two}\n]\n  dummyObject=RecursiveToStringStyleTest.DummyObject[\n  id=19\n  label=dix-neuf\n  infos=Object[][\n  {one,two}\n]\n  dummyObject=RecursiveToStringStyleTest.DummyObject[\n  id=20\n  label=vingt\n  infos=Object[][\n  {one,two}\n]\n  dummyObject=RecursiveToStringStyleTest.DummyObject[\n  id=21\n  label=vingt et un\n  infos=Object[][\n  {one,two}\n]\n  dummyObject=<null>\n]\n]\n]\n]\n]\n]\n]"));
    }

    // =============================================================================================
    // class for test
    // =============================================================================================

    private static class DummyObject {
        private Integer id;

        public String label;

        public List<String> infos;

        public DummyObject dummyObject;

        private DummyObject(final Integer id, final String label, List<String> infos) {
            this.id = id;
            this.label = label;
            this.infos = infos;
        }

        public Integer getId() {
            return id;
        }

        public void setId(final Integer id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(final String label) {
            this.label = label;
        }

        public DummyObject getDummyObject() {
            return dummyObject;
        }

        public void setDummyObject(final DummyObject dummyObject) {
            this.dummyObject = dummyObject;
        }

        public List<String> getInfos() {
            return infos;
        }

        public void setInfos(final List<String> infos) {
            this.infos = infos;
        }
    }
}
