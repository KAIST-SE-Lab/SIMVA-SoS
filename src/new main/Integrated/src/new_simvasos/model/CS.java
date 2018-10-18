package new_simvasos.model;

import new_simvasos.model.Abstract.ActionableObject;
import new_simvasos.model.Abstract.NonActionableObject;
import new_simvasos.model.Abstract.ObjectLocation;
import new_simvasos.model.Abstract.SIMVASoS_Object;
import new_simvasos.model.Enums.EnumCSState;
import new_simvasos.model.Enums.EnumCSType;

import java.util.ArrayList;

/**
 * @author ymbaek
 *
 * CS is a major component system of an organization of an SoS.
 * Basically, A CS is an ActionableObject, which means that a CS can perform its autonomous behaviors.
 * By extending ActionableObject, a CS has its own ActionList[] with selectActions() and doActions() methods.
 *
 * For the communication, Each CS has a message queue to receive messages from other CSs.
 *
 * Additionally, to represent CS's capabilities and independencies,
 * A CS has its own local knowledge base (csKnowledgeList) and resource base (csResourceList).
 */
public abstract class CS extends ActionableObject{
    EnumCSType csType;          //type of a CS: ADMIN,MANAGER,MIDDLE_MANAGER,MEDIATOR,NORMAL,PASSIVE
    EnumCSState csState;        //state of a CS: IDLE,DEACTIVATED,OCCUPIED

    ArrayList<CommunicationMessage> csReceivedMessages; //List of received messages
    ArrayList<CS_Knowledge> csKnowledgeList;            //List of CS's local knowledge
    ArrayList<CS_Resource> csResourceList;              //List of CS's local resources


    public CS() {
        csType = null;
        csState = null;
        csReceivedMessages = new ArrayList<>();
        csKnowledgeList = new ArrayList<>();
        csResourceList = new ArrayList<>();
    }

    public CS(EnumCSType csType) {
        this.csType = csType;
        csState = null;
        csReceivedMessages = new ArrayList<>();
        csKnowledgeList = new ArrayList<>();
        csResourceList = new ArrayList<>();
    }

    public CS(ArrayList<CS_Knowledge> csKnowledgeList, ArrayList<CS_Resource> csResourceList) {
        csType = null;
        csState = null;
        csReceivedMessages = new ArrayList<>();
        this.csKnowledgeList = csKnowledgeList;
        this.csResourceList = csResourceList;
    }

    public CS(EnumCSType csType, ArrayList<CS_Knowledge> csKnowledgeList, ArrayList<CS_Resource> csResourceList) {
        this.csType = csType;
        csState = null;
        csReceivedMessages = new ArrayList<>();
        this.csKnowledgeList = csKnowledgeList;
        this.csResourceList = csResourceList;
    }


    /**
     * Before selecting actions, A CS should check messages from other CSs.
     * Detailed message checking mechanism (interpretation & processing) should be implemented in child classes
     * (This method is abstract, so it should be implemented by child classes)
     *
     * @return execution logs of checking received messages
     */
    public abstract String checkMessage();

    //selectActions() also should be implemented in child classes.
    //doActions() also should be implemented in child classes.

    /**
     * runCS(...) should include:
     * (1) checkMessage()
     * (2) selectActions()
     * (3) doActions()
     * (This method is abstract, so it should be implemented by child classes)
     *
     * @param tick              current tick of simulation
     * @param SoSEnvironment    SoS-level EnvironmentElements (received from Org (from SoS))
     * @return                  Execution log after running a single tick of CS's behaviors
     */
    public abstract String runCS(int tick, ArrayList<SIMVASoS_Object> SoSEnvironment);


    /* INITIALIZERS */

    public void initSystem(){
        initActionList();       //Add (initial) CS_Actions into its actionList[] as CS's capabilities
        initKnowledgeList();    //Add initial CS_Knowledge(s) into its csKnowledgeList[]
        initResourceList();     //Add initial CS_Resource(s) into its csResourceList[]
        initReceivedMessages(); //Make the message queue empty
    }


    private void initActionList(){
        //TODO: how to initialize the action list?
    }

    private void initResourceList(){
        //TODO: how to initialize the resource list?
    }

    private void initKnowledgeList(){
        //TODO: how to initialize the knowledge list?
    }

    private void initReceivedMessages() {
        //TODO: how to intialize the message queue?
    }


    /* ADDERS */

    /**
     * Add a newly received message into the CS's message queue
     * @param receivedMessage   a received message from other CSs/Organizations
     */
    public void addReceivedMessage(CommunicationMessage receivedMessage) {
        csReceivedMessages.add(receivedMessage);
    }

    /**
     * Add a new knowledge item into csKnowledgeList
     * @param knowledge     new knowledge item
     */
    public void addKnowledge(CS_Knowledge knowledge) {
        csKnowledgeList.add(knowledge);
    }

    /**
     * Add a new resource item into csResourceList
     * @param resource      new resource item
     */
    public void addResource(CS_Resource resource) {
        csResourceList.add(resource);
    }


    /* GETTERS & SETTERS */

    public EnumCSType getCsType() {
        return csType;
    }

    public EnumCSState getCsState() {
        return csState;
    }

    public ArrayList<CS_Knowledge> getCsKnowledgeList() {
        return csKnowledgeList;
    }

    public ArrayList<CS_Resource> getCsResourceList() {
        return csResourceList;
    }


}
