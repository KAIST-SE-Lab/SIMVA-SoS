package new_simvasos.model.Enums;

/**
 * Enumeration for describing status of an action
 */
public enum EnumActionStatus {
    UNAVAILABLE,    //An action is unavailable
    NOT_STARTED,    //An action is not yet started
    EXECUTING,      //An action is being executed
    PAUSED,         //An action is paused
    PENDED,         //An action is pended
    COMPLETED,      //An action is completed
    ERROR           //An action has an error
}
