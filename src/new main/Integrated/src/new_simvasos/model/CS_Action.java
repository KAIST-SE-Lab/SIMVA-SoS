package new_simvasos.model;

import new_simvasos.model.Abstract.SIMVASoS_Action;
import new_simvasos.model.Enums.EnumActionPriority;
import new_simvasos.model.Enums.EnumActionType;

public abstract class CS_Action extends SIMVASoS_Action{
    float csActionCost;     //cost to perform this action
    float csActionBenefit;  //benefit from performing this action
    float csActionUtility;  //csActionBenefit - csActionCost

    public CS_Action() {
        super();
        this.csActionCost = -1;
        this.csActionBenefit = -1;
        this.csActionUtility = this.csActionBenefit - this.csActionCost;
    }

    public CS_Action(float csActionCost, float csActionBenefit) {
        this.csActionCost = csActionCost;
        this.csActionBenefit = csActionBenefit;
        this.csActionUtility = this.csActionBenefit - this.csActionCost;
    }

    public CS_Action(String actionId, String actionName) {
        super(actionId, actionName);
    }

    public CS_Action(String actionId, String actionName, EnumActionType actionType, int actionDuration, float csActionCost, float csActionBenefit) {
        super(actionId, actionName, actionType, actionDuration);
        this.csActionCost = csActionCost;
        this.csActionBenefit = csActionBenefit;
        this.csActionUtility = this.csActionBenefit - this.csActionCost;
    }


    /**
     * Calculate the current utility value
     */
    public void calculateUtility() {
        this.csActionUtility = this.csActionBenefit - this.csActionCost;
    }

    public void executeBehavior(){

    }

    public void checkPrecondition(){

    }

    public void updatePriority(){

    }


    /* GETTERS & SETTERS */

    public float getCsActionCost() {
        return csActionCost;
    }

    public void setCsActionCost(float csActionCost) {
        this.csActionCost = csActionCost;
    }

    public float getCsActionBenefit() {
        return csActionBenefit;
    }

    public void setCsActionBenefit(float csActionBenefit) {
        this.csActionBenefit = csActionBenefit;
    }

    public float getCsActionUtility() {
        return csActionUtility;
    }

    public void setCsActionUtility(float csActionUtility) {
        this.csActionUtility = csActionUtility;
    }
}
