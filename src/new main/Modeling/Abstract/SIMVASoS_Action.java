package Abstract;

import Enums.EnumActionType;

import java.util.ArrayList;

public abstract class SIMVASoS_Action {
    int actionId;
    String actionName;
    EnumActionType actionType;
    boolean actionPrecondition;
    ArrayList<SIMVASoS_Behavior> actionBehaviors;
    int actionDuration;

    public SIMVASoS_Action() {
        actionId = -1;
        actionName = "noName";
        actionType = null;
        actionPrecondition = false;
        actionBehaviors = new ArrayList<SIMVASoS_Behavior>();
        actionDuration = -1;
    }
}
