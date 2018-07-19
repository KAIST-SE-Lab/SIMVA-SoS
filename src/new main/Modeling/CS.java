import Abstract.NonActionableObject;
import Enums.EnumCSState;
import Enums.EnumCSType;

import java.util.ArrayList;

public class CS {
    int csId;
    String csName;
    EnumCSType csType;
    EnumCSState csState;
    ArrayList<CS_Action> csActionList;
    ArrayList<CS_Action> csExecutionList;
    ArrayList<CS_Message> csIncomingReqList;
    ArrayList<CS_Message> csIncomingInfoLIst;
    ArrayList<NonActionableObject> csKnowledgeList;
    //ArrayList<Organization> csOrgs;


    public CS() {
        csId = -1;
        csName = "noName";
        csType = null;
        csState = null;
        csIncomingReqList = new ArrayList<CS_Message>();
        csIncomingInfoLIst = new ArrayList<CS_Message>();
        csActionList = new ArrayList<CS_Action>();
        csExecutionList = new ArrayList<CS_Action>();
        csKnowledgeList = new ArrayList<NonActionableObject>();
    }
}
