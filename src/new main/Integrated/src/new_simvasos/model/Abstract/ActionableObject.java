package new_simvasos.model.Abstract;

import java.util.ArrayList;

/**
 * @author ymbaek
 *
 * Abstract class for every actionable object class in SIMVA-SoS.
 * Actionable ojbects can perform its own specific actions during the simulation.
 * The set (list) of possible (executable) actions are stored in actionList.
 */
public abstract class ActionableObject extends SIMVASoS_Object{

    ArrayList<SIMVASoS_Action> actionList;

    public ActionableObject() {
        actionList = new ArrayList<SIMVASoS_Action>();
    }
}
