package new_simvasos.model.Abstract;

/**
 * @author ymbaek
 *
 * Abstract class to describe the lowest-level of behavioral objects in SIMVA-SoS.
 * One or more operations can be included in a single action.
 * A unit behavior manipulates the value or state of certain variables.
 *
 * To make an operation executable, operate() method should be specifically implemented at code-level.
 */
public abstract class SIMVASoS_Operation {

    public SIMVASoS_Operation() {
    }

    /**
     * Operate its specified code-level behaviors and return a log message
     * (This method is abstract, so it should be implemented by child classes)
     * @return operation execution log messages
     */
    abstract String operate();


}


