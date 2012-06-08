package fixtures.common.velocity;

import java.io.Writer;
import java.net.URL;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockVelocityEngine extends VelocityEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(MockVelocityEngine.class);

    public MockVelocityEngine() {
        super();
        // nécessaire pour charger les templates
        addProperty("resource.loader", "class");
        addProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
    }

    @Override
    public boolean mergeTemplate(String templateName, Context context, Writer writer) {
        if (isExist(templateName)) {
            try {
                return super.mergeTemplate(templateName, context, writer);
            } catch (Exception e) {
                // do nothing
            }
        } else if (isExist("mails/" + templateName)) {
            try {
                return super.mergeTemplate("mails/" + templateName, context, writer);
            } catch (Exception e) {
                // do nothing
            }
        }
        return true;
    }

    @Override
    public boolean mergeTemplate(final String templateName, final String encoding, final Context context,
            final Writer writer) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        if (isExist(templateName)) {
            try {
                return super.mergeTemplate(templateName, encoding, context, writer);
            } catch (Exception e) {
                // do nothing
            }
        } else if (isExist("mails/" + templateName)) {
            try {
                return super.mergeTemplate("mails/" + templateName, encoding, context, writer);
            } catch (Exception e) {
                // do nothing
            }
        }
        return true;
    }

    /**
     * on regarde si le template existe
     */
    private boolean isExist(final String template) {
        URL url = getClass().getResource("/" + template);
        LOGGER.info("template [" + template + "] exists = " + (url != null) + " => url = " + url);
        return (url != null);
    }
}
