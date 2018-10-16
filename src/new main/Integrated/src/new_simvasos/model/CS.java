package new_simvasos.model;

import new_simvasos.model.Abstract.ActionableObject;
import new_simvasos.model.Abstract.NonActionableObject;
import new_simvasos.model.Abstract.ObjectLocation;
import new_simvasos.model.Abstract.SIMVASoS_Object;
import new_simvasos.model.Enums.EnumCSState;
import new_simvasos.model.Enums.EnumCSType;

import java.util.ArrayList;

public abstract class CS extends ActionableObject{
//    String csId;                //id of a CS (already included in SIMVASoS_Object)
//    String csName;              //name of a CS (already included in SIMVASoS_Object)
    EnumCSType csType;          //type of a CS: ADMIN,MANAGER,MIDDLE_MANAGER,MEDIATOR,NORMAL,PASSIVE
    EnumCSState csState;        //state of a CS: IDLE,DEACTIVATED,OCCUPIED
//    ObjectLocation csLocation;//already included in SIMVASoS_Object
//    boolean isCSActivated;    //already included in SIMVASoS_Object

//    ArrayList<CS_Action> csActionList;        //already included in ActionableObject
//    ArrayList<CS_Action> csExecutionList;     //already included in ActionableObject
//    ArrayList<CS_Message> csIncomingReqList;  //name modified
//    ArrayList<CS_Message> csIncomingInfoLIst; //name modified

    ArrayList<CommunicationMessage> csReceivedMessages; //List of received messages
    ArrayList<CS_Knowledge> csKnowledgeList;            //List of CS's local knowledge
    ArrayList<CS_Resource> csResourceList;              //List of CS's local resources
    //ArrayList<Organization> csOrgs;

//    ArrayList<SIMVASoS_Object> SoSEnvironment;    //passed as a parameter


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
