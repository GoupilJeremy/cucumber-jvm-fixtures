package fixtures.common.transformers;

import org.apache.commons.lang.StringUtils;

public class Label {
    public static String cleanLabel(String label) {
        // le caractère à remplacer est le caractère espace de Format (' ') qui n'est pas un espace
        // classique mais un espace insécable
        return StringUtils.replace(label, " ", " ");
    }
}
