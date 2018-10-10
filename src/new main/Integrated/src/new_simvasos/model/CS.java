package new_simvasos.model;

import new_simvasos.model.Abstract.NonActionableObject;
import new_simvasos.model.Abstract.ObjectLocation;
import new_simvasos.model.Abstract.SIMVASoS_Object;
import new_simvasos.model.Enums.EnumCSState;
import new_simvasos.model.Enums.EnumCSType;

import java.util.ArrayList;

public class CS {
    int csId;
    String csName;
    EnumCSType csType;
    EnumCSState csState;
    ObjectLocation csLocation;
    boolean isCSActivated;

    ArrayList<CS_Action> csActionList;
    ArrayList<CS_Action> csExecutionList;
    ArrayList<CS_Message> csIncomingReqList;
    ArrayList<CS_Message> csIncomingInfoLIst;
    ArrayList<NonActionableObject> csKnowledgeList;
    ArrayList<CS_Resource> csResourceList;
    //ArrayList<Organization> csOrgs;

    ArrayList<SIMVASoS_Object> SoSEnvironment;


    /**
     * @param SoSEnvironment    A list of SoS-level environmental factors
     *                          it could be null.
     */
    public CS(ArrayList<SIMVASoS_Object> SoSEnvironment) {
        csId = -1;
        csName = "noName";
        csType = null;
        csState = null;
        csLocation = null;
        isCSActivated = false;

        csIncomingReqList = new ArrayList<CS_Message>();
        csIncomingInfoLIst = new ArrayList<CS_Message>();
        csActionList = new ArrayList<CS_Action>();
        csExecutionList = new ArrayList<CS_Action>();
        csKnowledgeList = new ArrayList<NonActionableObject>();
        csResourceList = new ArrayList<CS_Resource>();

        this.SoSEnvironment = SoSEnvironment;
    }


    public void activate(){
        isCSActivated = true;
    }
    public void deactivate(){
        isCSActivated = false;
    }

    public void joinOrg(Organization org){

    }

    public void leaveOrg(Organization org){

    }

    public void initSystem(){
        initActionList();
        initKnowledgeList();
        initResourceList();
    }

    /**
     * @param tick SIMSoS variable
     */
    public void doAction(int tick){
        checkMessage();
        makeDecision();
        System.out.println("[CS] " + csName + ": doAction() at " + tick);
    }

    private void checkMessage(){

    }

    private void makeDecision(){

    }

    private void initActionList(){

    }

    private void initResourceList(){

    }

    private void initKnowledgeList(){

    }

    //TODO: To consider not to use
    public void updateSystemState(){

    }


}
