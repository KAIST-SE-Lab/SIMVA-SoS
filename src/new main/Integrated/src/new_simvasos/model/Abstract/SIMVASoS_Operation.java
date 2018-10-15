package new_simvasos.model.Abstract;

import new_simvasos.model.Enums.EnumBehaviorType;

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
    EnumBehaviorType behaviorType;
    SIMVASoS_Object behaviorTarget;
    float behaviorValue;


    public SIMVASoS_Operation() {
        behaviorType = null;
        behaviorTarget = null;
        behaviorValue = -1;
    }
}
