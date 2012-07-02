package fixtures.common.transformers;

import org.apache.commons.lang.StringUtils;

public class Label {

	// Caract�re ins�cable
	public static final String NON_BREAKING_SPACE = "\u00A0";

	private Label(){}
    
    public static String cleanLabel(String label) {
        // le caract�re � remplacer est le caract�re espace de Format ('�') qui n'est pas un espace
        // classique mais un espace ins�cable
        return StringUtils.replace(label, NON_BREAKING_SPACE, " ");
    }
}
