package Abstract;

import Enums.EnumBehaviorType;

public abstract class SIMVASoS_Behavior {
    EnumBehaviorType behaviorType;
    SIMVASoS_Object behaviorTarget;
    float behaviorValue;


    public SIMVASoS_Behavior() {
        behaviorType = null;
        behaviorTarget = null;
        behaviorValue = -1;
    }
}
