package new_simvasos.model;

import new_simvasos.model.Abstract.NonActionableObject;
import new_simvasos.model.Enums.EnumKnowledgeType;

/**
 * @author ymbaek
 *
 * CS_Knowledge is a specialized type of NonActionableObject.
 * Based on attributes and operations of NonActionableObject, CS_Knowledge has its own knowledge type and info.
 */
public class CS_Knowledge extends NonActionableObject{
    EnumKnowledgeType csKnowledgeType;  //Type of knowledge or information
    String csKnowledgeInfo;             //Information of knowledge (i.e., contents)


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
