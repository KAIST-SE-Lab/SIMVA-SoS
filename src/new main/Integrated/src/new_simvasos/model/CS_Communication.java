package new_simvasos.model;

import new_simvasos.model.Abstract.SIMVASoS_Action;
import new_simvasos.model.Enums.EnumActionType;

/**
 * @author ymbaek
 *
 * CS_Communication is a specialized type of SIMVASoS_Action for communication purpose.
 * When selecting actions from actionList[] of a CS, CS_Communication can be selected (or created).
 * Because this action is for the communication, it necessarily requires a message (CommunicationMessage).
 *
 * //TODO: Check this part (abstract)
 * Since CS_Communication is also an abstract class inheriting CS_Action (from SIMVASoS_Action),
 * runOperations() and checkPrecondition() should be implemented for the instantiation.
 */
//TODO: Check the details in specification
public abstract class CS_Communication extends SIMVASoS_Action {
    CommunicationMessage commMsg;
    CommunicationChannel commChannel;

    public CS_Communication() {
        commMsg = null;
    }

    public CS_Communication(CommunicationMessage commMsg, CommunicationChannel commChannel) {
        this.commMsg = commMsg;
        this.commChannel = commChannel;
    }

    public CS_Communication(String actionId, String actionName) {
        super(actionId, actionName);
    }

    public CS_Communication(String actionId, String actionName, CommunicationMessage commMsg, CommunicationChannel commChannel) {
        super(actionId, actionName);
        this.commMsg = commMsg;
        this.commChannel = commChannel;
    }

    public CS_Communication(String actionId, String actionName, EnumActionType actionType, int actionDuration) {
        super(actionId, actionName, actionType, actionDuration);
    }

    public CS_Communication(String actionId, String actionName, EnumActionType actionType, int actionDuration, CommunicationMessage commMsg, CommunicationChannel commChannel) {
        super(actionId, actionName, actionType, actionDuration);
        this.commMsg = commMsg;
        this.commChannel = commChannel;
    }


//    /**
//     * There are two ways to perform an action
//     * i) Run operations in actionOperations[]
//     * ii) Run the code written in runOperations without any operation definitions
//     * <p>
//     * In runOperations(), progress should be updated according to its total duration required
//     * <p>
//     * (This method is abstract, so it should be implemented by child classes)
//     *
//     * @return operations execution log messages
//     */
//    @Override
//    protected String runOperations(int tick, ArrayList<SIMVASoS_Object> SoSEnvironment) {
//        //TODO: Add a message into the message queue of the Simulation Engine
//        return null;
//    }

//    /**
//     * Check precondition of an action.
//     * The precondition can be related to (associated with) external objects,
//     * and this method checks the values of other objects and updates the value of 'actionPrecondition'
//     * <p>
//     * (This method is abstract, so it should be implemented by child classes)
//     */
//    @Override
//    protected void checkPrecondition() {
//        //TODO: Check the precondition using if(commChannel.getObjOccupyingIds()) statement
//    }


    /* GETTERS & SETTERS */

    public CommunicationMessage getCommMsg() {
        return commMsg;
    }

    public void setCommMsg(CommunicationMessage commMsg) {
        this.commMsg = commMsg;
    }
}
