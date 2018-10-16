package new_simvasos.model;

import new_simvasos.model.Abstract.SIMVASoS_Action;
import new_simvasos.model.Abstract.SIMVASoS_Operation;
import new_simvasos.model.Enums.EnumActionType;
import new_simvasos.model.Enums.EnumCSInterfaceType;

//TODO: Check the details in specification
public abstract class CS_Communication extends CS_Action {
    CommunicationMessage commMsg;

    public CS_Communication() {
    }

    public CS_Communication(float csActionCost, float csActionBenefit) {
        super(csActionCost, csActionBenefit);
    }

    public CS_Communication(String actionId, String actionName) {
        super(actionId, actionName);
    }

    public CS_Communication(String actionId, String actionName, EnumActionType actionType, int actionDuration, float csActionCost, float csActionBenefit) {
        super(actionId, actionName, actionType, actionDuration, csActionCost, csActionBenefit);
    }

    public CS_Communication(CommunicationMessage commMsg) {
        this.commMsg = commMsg;
    }

    public CS_Communication(float csActionCost, float csActionBenefit, CommunicationMessage commMsg) {
        super(csActionCost, csActionBenefit);
        this.commMsg = commMsg;
    }

    public CS_Communication(String actionId, String actionName, CommunicationMessage commMsg) {
        super(actionId, actionName);
        this.commMsg = commMsg;
    }

    public CS_Communication(String actionId, String actionName, EnumActionType actionType, int actionDuration, float csActionCost, float csActionBenefit, CommunicationMessage commMsg) {
        super(actionId, actionName, actionType, actionDuration, csActionCost, csActionBenefit);
        this.commMsg = commMsg;
    }

    /* GETTERS & SETTERS */

    public CommunicationMessage getCommMsg() {
        return commMsg;
    }

    public void setCommMsg(CommunicationMessage commMsg) {
        this.commMsg = commMsg;
    }
}
