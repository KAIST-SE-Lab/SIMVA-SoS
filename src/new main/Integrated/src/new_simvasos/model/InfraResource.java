package new_simvasos.model;

import new_simvasos.model.Abstract.NonActionableObject;
import new_simvasos.model.Enums.EnumResType;

public class InfraResource extends NonActionableObject{
    EnumResType resType;

    public InfraResource() {
        resType = null;
    }

    public InfraResource(EnumResType resType) {
        this.resType = resType;
    }

    /* GETTERS & SETTERS */

    public EnumResType getResType() {
        return resType;
    }

    public void setResType(EnumResType resType) {
        this.resType = resType;
    }
}
