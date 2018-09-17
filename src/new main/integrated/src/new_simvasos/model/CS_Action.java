package new_simvasos.model;

import new_simvasos.model.Abstract.SIMVASoS_Action;
import new_simvasos.model.Enums.EnumCSActionPriority;

public class CS_Action extends SIMVASoS_Action{
    EnumCSActionPriority csActionPriority; //
    float csActionCost; //
    float csActionBenefit; //
    float csActionUtility; //

    public CS_Action() {
        super();
        csActionPriority = null;
        csActionCost = -1;
        csActionBenefit = -1;
        csActionUtility = csActionBenefit - csActionCost;
    }

    public void executeBehavior(){

    }

    public void checkPrecondition(){

    }

    public void updatePriority(){

    }
}
