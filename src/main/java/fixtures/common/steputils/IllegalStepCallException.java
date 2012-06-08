package fixtures.common.steputils;

public class IllegalStepCallException extends Throwable {
    public IllegalStepCallException(final String stepToCallBefore) {
        super("Ce step est li� au step : \"" + stepToCallBefore + "\" qu'il faut appeler avant!");
    }
}
