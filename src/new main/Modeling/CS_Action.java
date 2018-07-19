import Abstract.SIMVASoS_Action;
import Abstract.SIMVASoS_Behavior;
import Enums.EnumCSActionPriority;

import java.util.ArrayList;

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
}
