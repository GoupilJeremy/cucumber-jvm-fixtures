package fixtures.common.steputils;

public class IllegalStepCallException extends Throwable {
    public IllegalStepCallException(final String stepToCallBefore) {
        super("Ce step est lié au step : \"" + stepToCallBefore + "\" qu'il faut appeler avant!");
    }
}
