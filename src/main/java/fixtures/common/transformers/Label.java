package fixtures.common.transformers;

import org.apache.commons.lang.StringUtils;

public class Label {

	// Caractère insécable
	public static final String NON_BREAKING_SPACE = "\u00A0";

	private Label(){}
    
    public static String cleanLabel(String label) {
        // le caractère à remplacer est le caractère espace de Format (' ') qui n'est pas un espace
        // classique mais un espace insécable
        return StringUtils.replace(label, NON_BREAKING_SPACE, " ");
    }
}
