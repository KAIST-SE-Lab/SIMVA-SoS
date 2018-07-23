import Abstract.NonActionableObject;
import Enums.EnumKnowledgeType;

public class CS_Knowledge extends NonActionableObject{
    EnumKnowledgeType csKnowledgeType;
    String csKnowledgeInfo;


    public CS_Knowledge() {
        super();
        csKnowledgeInfo = "noInfo";
        csKnowledgeType = null;
    }
}
