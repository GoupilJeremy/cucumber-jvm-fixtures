package fixtures.common.transformers.model;

public enum EmailPropertyEnum {

    SUJET_HEADER("sujet"),
    MESSAGE_HEADER("message"),
    REPONDRE_A_HEADER("répondre à"),
    DE_HEADER("de"),
    A_HEADER("à"),
    COPIE_CACHEE_HEADER("copie cachée"),
    PIECE_JOINTE_HEADER("piece jointe");

    private String label;

    EmailPropertyEnum(String label) {
        this.label = label;
    }

    public static EmailPropertyEnum getEmailPropertyFromLabel(String value) {
        for (int i = 0; i < EmailPropertyEnum.values().length; i++) {
            if (EmailPropertyEnum.values()[i].getLabel().equals(value)) {
                return EmailPropertyEnum.values()[i];
            }
        }
        return null;
    }

    public String getLabel() {
        return label;
    }
}
