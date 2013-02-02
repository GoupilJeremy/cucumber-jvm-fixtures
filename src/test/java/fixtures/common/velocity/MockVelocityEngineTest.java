package fixtures.common.velocity;

import com.google.common.base.Charsets;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MockVelocityEngineTest {
    @Test
    public void test_constructor_checkProperties() {
        MockVelocityEngine velocityEngine = new MockVelocityEngine();
        //
        String property01 = (String) velocityEngine.getProperty("resource.loader");
        String property02 = (String) velocityEngine.getProperty("class.resource.loader.class");
        //
        assertThat(property01, is(MockVelocityEngine.CLASS_VALUE));
        assertThat(property02, is(MockVelocityEngine.RESOURCE_LOADER));
    }

    @Test
    public void test_merge_template_no_template_found() {
        MockVelocityEngine velocityEngine = new MockVelocityEngine();
        Writer writer = new StringWriter();
        Context context = new VelocityContext();
        boolean success = velocityEngine.mergeTemplate("unknown.vm", Charsets.ISO_8859_1.name(), context, writer);
        //
        assertThat(success, is(true));
        assertThat(writer.toString(), is(""));
    }

    @Test
    public void test_merge_template_template_with_path() {
        MockVelocityEngine velocityEngine = new MockVelocityEngine();
        Writer writer = new StringWriter();
        Context context = new VelocityContext();
        boolean success = velocityEngine.mergeTemplate("template01.vm", Charsets.ISO_8859_1.name(), context, writer);
        //
        assertThat(success, is(true));
        assertThat(writer.toString(), is("ceci est le template 01"));
    }

    @Test
    public void test_merge_template_template_with_path_but_found_in_mail() {
        MockVelocityEngine velocityEngine = new MockVelocityEngine();
        Writer writer = new StringWriter();
        Context context = new VelocityContext();
        boolean success = velocityEngine.mergeTemplate("template02.vm", Charsets.ISO_8859_1.name(), context, writer);
        //
        assertThat(success, is(true));
        assertThat(writer.toString(), is("ceci est le template 02"));
    }

    @Test
    public void test_merge_template_template_found_but_parse_error_inside() {
        MockVelocityEngine velocityEngine = new MockVelocityEngine();
        Writer writer = new StringWriter();
        Context context = new VelocityContext();
        boolean success = velocityEngine
                .mergeTemplate("template03_parse_error.vm", Charsets.ISO_8859_1.name(), context, writer);
        //
        assertThat(success, is(true));
        assertThat(writer.toString(), is(""));
    }

    @Test
    public void test_merge_deprecated_template_no_template_found() {
        MockVelocityEngine velocityEngine = new MockVelocityEngine();
        Writer writer = new StringWriter();
        Context context = new VelocityContext();
        boolean success = velocityEngine.mergeTemplate("unknown.vm", context, writer);
        //
        assertThat(success, is(true));
        assertThat(writer.toString(), is(""));
    }

    @Test
    public void test_merge_deprecated_template_template_with_path() {
        MockVelocityEngine velocityEngine = new MockVelocityEngine();
        Writer writer = new StringWriter();
        Context context = new VelocityContext();
        boolean success = velocityEngine.mergeTemplate("template01.vm", context, writer);
        //
        assertThat(success, is(true));
        assertThat(writer.toString(), is("ceci est le template 01"));
    }

    @Test
    public void test_merge_template_deprecated_template_with_path_but_found_in_mail() {
        MockVelocityEngine velocityEngine = new MockVelocityEngine();
        Writer writer = new StringWriter();
        Context context = new VelocityContext();
        boolean success = velocityEngine.mergeTemplate("template02.vm", context, writer);
        //
        assertThat(success, is(true));
        assertThat(writer.toString(), is("ceci est le template 02"));
    }

    @Test
    public void test_merge_template_deprecated_template_found_but_parse_error_inside() {
        MockVelocityEngine velocityEngine = new MockVelocityEngine();
        Writer writer = new StringWriter();
        Context context = new VelocityContext();
        boolean success = velocityEngine.mergeTemplate("template03_parse_error.vm", context, writer);
        //
        assertThat(success, is(true));
        assertThat(writer.toString(), is(""));
    }
}
