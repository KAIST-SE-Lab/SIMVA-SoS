package new_simvasos.model;

import new_simvasos.model.Abstract.SIMVASoS_Action;
import new_simvasos.model.Enums.EnumActionPriority;
import new_simvasos.model.Enums.EnumActionType;

/**
 * @author ymbaek
 *
 * CS_Action is a unit of behavior execution of a CS,
 * which has its own operations, durations, progress, and status information.
 *
 * CS_Action basically inherits SIMVASoS_Action, thus it has all attributes and methods of SIMVASoS_Action.
 * CS_Action is also an abstract class, and it has CS-specific attributes such as cost and benefit information.
 *
 * From SIMVASoS_Action, two abstract methods should be implemented for instantiation of CS_Action:
 * (1) runOperations(): detailed code-level behaviors of an action. It can consist of a series of CS_Operations
 * (2) checkPrecondition(): to update value of actionPrecondition (true/false)
 *
 * In CS_Action, the concept 'utility' is used to describe the value of an action with respect to SoS-level goal.
 * Thus, calculateUtility() should be implemented when CS_Action is instantiated while defining a CS.
 * (calculateUtility() can do static calculation using a constant value)
 */
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
     * This method updates the value of csActionUtility.
     */
    public abstract void calculateUtility();
//    public void calculateUtility() {
//        this.csActionUtility = this.csActionBenefit - this.csActionCost;
//    }

//    public void executeBehavior(){
//
//    }
//
//    public void checkPrecondition(){
//
//    }
//
//    public void updatePriority(){
//
//    }


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
