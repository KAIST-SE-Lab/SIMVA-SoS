package new_simvasos.model;

import new_simvasos.model.Abstract.SIMVASoS_Action;
import new_simvasos.model.Enums.EnumActionPriority;

public class CS_Action extends SIMVASoS_Action{
    EnumActionPriority csActionPriority; //
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
