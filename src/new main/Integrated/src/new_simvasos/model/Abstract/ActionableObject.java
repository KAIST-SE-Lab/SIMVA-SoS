package new_simvasos.model.Abstract;

import java.util.ArrayList;

/**
 * @author ymbaek
 *
 * Abstract class for every actionable object class in SIMVA-SoS.
 * Actionable ojbects can perform its own specific actions during the simulation.
 * The set (list) of possible (executable) actions are stored in actionList.
 *
 * ActionableObject is mainly inhereted by systems (CSs, serviczed systems).
 */
public abstract class ActionableObject extends SIMVASoS_Object{

    ArrayList<SIMVASoS_Action> actionList;
    ArrayList<SIMVASoS_Action> selectedActionList;

    public ActionableObject() {
        actionList = new ArrayList<SIMVASoS_Action>();
    }


    /**
     *  Add one action into actionList[]
     *  @param action   an action to be into actionList[]
     */
    public void addAction(SIMVASoS_Action action) {
        actionList.add(action);
    }

    /**
     * Removes all of the actions from this actionList[].
     */
    public void resetActionList() {
        actionList.clear();
    }

    /**
     * selectActions() performs an action selection mechanism,
     * such as precondition checking or decision making to decide what to perform
     * by updating selectedActionList[] ArrayList.
     * (This method is abstract, so it should be implemented by child classes)
     *
     * @return action selection log messages
     */
    abstract String selectActions();


    /**
     * doActions() executes actions stored in selectedActionList[]
     * (This method is abstract, so it should be implemented by child classes)
     *
     * @return action execution log messages
     */
    abstract String doActions();


    public ArrayList<SIMVASoS_Action> getActionList() {
        return actionList;
    }

    public ArrayList<SIMVASoS_Action> getSelectedActionList() {
        return selectedActionList;
    }

    public void setActionList(ArrayList<SIMVASoS_Action> actionList) {
        this.actionList = actionList;
    }
}
