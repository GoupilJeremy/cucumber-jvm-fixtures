package fixtures.common.transformers;

import org.apache.commons.lang.StringUtils;

public class Label {
    public static String cleanLabel(String label) {
        // le caract�re � remplacer est le caract�re espace de Format ('�') qui n'est pas un espace
        // classique mais un espace ins�cable
        return StringUtils.replace(label, "�", " ");
    }
}
