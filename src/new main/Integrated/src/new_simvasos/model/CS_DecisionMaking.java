package new_simvasos.model;

import new_simvasos.model.Abstract.SIMVASoS_Action;

import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class CS_DecisionMaking {
    String mechanismId;
    String mechanismName;

    public CS_DecisionMaking() {
        mechanismId = "noId";
        mechanismName = "noName";
    }

    public CS_DecisionMaking(String mechanismId, String mechanismName) {
        this.mechanismId = mechanismId;
        this.mechanismName = mechanismName;
    }

    /**
     * To select actions (selectActions()),
     * ActionableObjects or CSs may need to perform a specific decision making mechanism.
     * This method includes specific mechanism to select actions from a set of candidate actions.
     *
     * (This method is abstract, so it should be implemented by child classes)
     *
     * @param actionList    input list of candidate actions
     * @return  list of selected actions after the decision making
     */
    abstract ArrayList<SIMVASoS_Action> makeDecision(ArrayList<SIMVASoS_Action> actionList);

    /* GETTERS & SETTERS */

    public String getMechanismId() {
        return mechanismId;
    }

    public void setMechanismId(String mechanismId) {
        this.mechanismId = mechanismId;
    }

    public String getMechanismName() {
        return mechanismName;
    }

    public void setMechanismName(String mechanismName) {
        this.mechanismName = mechanismName;
    }
}
