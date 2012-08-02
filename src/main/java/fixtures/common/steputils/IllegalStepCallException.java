package fixtures.common.steputils;

import com.google.common.base.Strings;

public class IllegalStepCallException extends Exception {
    public IllegalStepCallException(final String stepToCallBefore) {
        super("Ce step est lié au step : \"" + Strings.nullToEmpty(stepToCallBefore) + "\" qu'il faut appeler avant!");
    }
}
