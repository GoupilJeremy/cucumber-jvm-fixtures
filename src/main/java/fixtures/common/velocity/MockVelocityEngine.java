package fixtures.common.velocity;

import java.io.Writer;
import java.net.URL;

import com.google.common.base.Charsets;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * recherche un template velocity suivant le chemin donné ou dans le répertoire /mails.
 * Si aucun template n'est trouvé, on en renvoie pas d'erreur car on suppose que l'on ne veu pas
 * tester le template donc on le shunte et on continue le traitement.
 */
public class MockVelocityEngine extends VelocityEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(MockVelocityEngine.class);

    public static final String CLASS_VALUE = "class";

    public static final String RESOURCE_LOADER = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";

    public MockVelocityEngine() {
        super();
        // nécessaire pour charger les templates
        addProperty("resource.loader", CLASS_VALUE);
        addProperty("class.resource.loader.class", RESOURCE_LOADER);
    }

    @Override
    public boolean mergeTemplate(String templateName, Context context, Writer writer) {
        return mergeTemplate(templateName, Charsets.ISO_8859_1.name(), context, writer);
    }

    @Override
    public boolean mergeTemplate(final String templateName, final String encoding, final Context context,
            final Writer writer) {
        if (!checkAndMergeTemplate(templateName, encoding, context, writer)) {
            checkAndMergeTemplate("mails/" + templateName, encoding, context, writer);
        }
        return true;
    }

    private boolean checkAndMergeTemplate(final String templateName, final String encoding, final Context context,
            final Writer writer) {
        if (isExist(templateName)) {
            try {
                return super.mergeTemplate(templateName, encoding, context, writer);
            } catch (Exception e) {
                LOGGER.warn("Problème avec le template {}", templateName, e);
            }
        }
        return false;
    }

    /**
     * on regarde si le template existe
     */
    private boolean isExist(final String template) {
        URL url = getClass().getResource("/" + template);
        LOGGER.info("template [{}] exists = {} => url = {}", new Object[]{template, (url != null), url});
        return (url != null);
    }
}
