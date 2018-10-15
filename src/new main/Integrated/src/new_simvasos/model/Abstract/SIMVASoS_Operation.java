package new_simvasos.model.Abstract;

import new_simvasos.model.Enums.EnumOperationType;

/**
 * @author ymbaek
 *
 * Abstract class to describe behavioral objects in SIMVA-SoS.
 * One or more behavioral objects can be included in a single action.
 *
 * A unit behavior manipulates the value of a certain variable or sends a message.
 * According to the type of behaviorType,
 * behaviorTarget is manipulated with a concrete value by performing the behavior
 */
public abstract class SIMVASoS_Operation {
    EnumOperationType behaviorType;
//    SIMVASoS_Object behaviorTarget;
//    float behaviorValue;


    public SIMVASoS_Operation() {
        behaviorType = null;
//        behaviorTarget = null;
//        behaviorValue = -1;
    }

    /**
     * Operate its specified code-level behaviors and return a log message
     * (This method is abstract, so it should be implemented by child classes)
     * @return operation execution log messages
     */
    abstract String operate();


    /** GETTERS & SETTERS */

    public EnumOperationType getBehaviorType() {
        return behaviorType;
    }

    public void setBehaviorType(EnumOperationType behaviorType) {
        this.behaviorType = behaviorType;
    }
}


