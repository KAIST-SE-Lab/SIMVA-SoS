package new_simvasos.model;

import new_simvasos.model.Abstract.NonActionableObject;
import new_simvasos.model.Enums.EnumKnowledgeType;

public class CS_Knowledge extends NonActionableObject{
    EnumKnowledgeType csKnowledgeType;
    String csKnowledgeInfo;


    public CS_Knowledge() {
        super();
        csKnowledgeInfo = "noInfo";
        csKnowledgeType = null;
    }

    public CS_Knowledge(EnumKnowledgeType csKnowledgeType, String csKnowledgeInfo) {
        this.csKnowledgeType = csKnowledgeType;
        this.csKnowledgeInfo = csKnowledgeInfo;
    }


    /* GETTERS & SETTERS */

    public EnumKnowledgeType getCsKnowledgeType() {
        return csKnowledgeType;
    }

    public void setCsKnowledgeType(EnumKnowledgeType csKnowledgeType) {
        this.csKnowledgeType = csKnowledgeType;
    }

    public String getCsKnowledgeInfo() {
        return csKnowledgeInfo;
    }

    public void setCsKnowledgeInfo(String csKnowledgeInfo) {
        this.csKnowledgeInfo = csKnowledgeInfo;
    }
}
