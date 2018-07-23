package Abstract;

import Enums.EnumActionType;

import java.util.ArrayList;

/**
 * @author ymbaek
 *
 * Abstract class to describe action objects in SIMVA-SoS.
 * Actionable objects (i.e., class ActionableObject) or systems (i.e., CS) can execute concrete actions,
 * which are instantiated actions of SIMVASoS_Action.
 *
 * Basically, each action has its ID, Name, and Type.
 * An action can be executed only when the precondition is met (actionPrecondition == true).
 * A single action consists of one or more concrete behaviors.
 * An action has its duration according to the time unit of the simulation engine.
 */
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
