package new_simvasos.model.Abstract;

import new_simvasos.model.Enums.EnumActionPriority;
import new_simvasos.model.Enums.EnumActionStatus;
import new_simvasos.model.Enums.EnumActionType;

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
    int actionId;                                   //id of an action
    String actionName;                              //name of an action
    EnumActionPriority actionPriority;              //priority of an action (MANDATORY ~ EXCLUDED)
    EnumActionType actionType;                      //type of an action (DO_TASK, COMMUNICATE, DO_NOTHING)
    boolean actionPrecondition;                     //Whether precondition of an action is met or not
    ArrayList<SIMVASoS_Operation> actionOperations; //Included list of operations to be executed by a single action
    int actionDuration;                             //Duration of an action
    int actionProgress;                             //Progress of an action (depends on its duration)
    EnumActionStatus actionStatus;                  //Current state of an action

    public SIMVASoS_Action() {
        actionId = -1;
        actionName = "noName";
        actionPriority = null;
        actionType = null;
        actionPrecondition = false;
        actionOperations = new ArrayList<SIMVASoS_Operation>();
        actionDuration = -1;
        actionProgress = 0;
        actionStatus = null;
    }

    public SIMVASoS_Action(int actionId, String actionName) {
        this.actionId = actionId;
        this.actionName = actionName;
        actionPriority = null;
        actionType = null;
        actionPrecondition = false;
        actionOperations = new ArrayList<SIMVASoS_Operation>();
        actionDuration = -1;
        actionProgress = 0;
        actionStatus = null;
    }

    public SIMVASoS_Action(int actionId, String actionName, EnumActionType actionType, int actionDuration) {
        this.actionId = actionId;
        this.actionName = actionName;
        this.actionType = actionType;
        this.actionDuration = actionDuration;
        actionPriority = null;
        actionPrecondition = false;
        actionOperations = new ArrayList<SIMVASoS_Operation>();
        actionProgress = 0;
        actionStatus = null;
    }


    /**
     * Add a new SIMVASoS_Operation into actionOperations[]
     * @param actionOperation   an operation to be added into actionOperations[]
     */
    public void addActionOperation(SIMVASoS_Operation actionOperation) {
        actionOperations.add(actionOperation);
    }

    /**
     * Removes all of the operations from actionOperations[]
     */
    public void resetOperations() {
        actionOperations.clear();
    }

    /**
     * There are two ways to perform an action
     * i) Run operations in actionOperations[]
     * ii) Run the code written in runOperations without any operation definitions
     *
     * (This method is abstract, so it should be implemented by child classes)
     * @return operations execution log messages
     */
    abstract String runOperations();

    /**
     * Check precondition of an action.
     * The precondition can be related to (associated with) external objects,
     * and this method checks the values of other objects and updates the value of 'actionPrecondition'
     *
     * (This method is abstract, so it should be implemented by child classes)
     */
    abstract void checkPrecondition();


    /**
     * Reset the progress value to zero
     */
    public void resetActionProgress() {
        actionProgress = 0;
    }

    /** GETTERS & SETTERS */

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public EnumActionPriority getActionPriority() {
        return actionPriority;
    }

    public void setActionPriority(EnumActionPriority actionPriority) {
        this.actionPriority = actionPriority;
    }

    public EnumActionType getActionType() {
        return actionType;
    }

    public void setActionType(EnumActionType actionType) {
        this.actionType = actionType;
    }

    public ArrayList<SIMVASoS_Operation> getActionOperations() {
        return actionOperations;
    }

    public void setActionOperations(ArrayList<SIMVASoS_Operation> actionOperations) {
        this.actionOperations = actionOperations;
    }

    public int getActionDuration() {
        return actionDuration;
    }

    public void setActionDuration(int actionDuration) {
        this.actionDuration = actionDuration;
    }

    public int getActionProgress() {
        return actionProgress;
    }

    public void setActionProgress(int actionProgress) {
        this.actionProgress = actionProgress;
    }

    public EnumActionStatus getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(EnumActionStatus actionStatus) {
        this.actionStatus = actionStatus;
    }
}
