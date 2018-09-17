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
}
