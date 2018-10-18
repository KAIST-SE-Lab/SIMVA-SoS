package new_simvasos.model;

import new_simvasos.model.Abstract.NonActionableObject;
import new_simvasos.model.Enums.EnumResType;

public class CS_Resource extends NonActionableObject{
    EnumResType csResType;

    public CS_Resource() {
        super();
        csResType = null;
    }

    public CS_Resource(EnumResType csResType) {
        this.csResType = csResType;
    }

    /**
     * TODO: not yet decided
     */
    public void initializeResource(){

    }

    /**
     * TODO: not yet decided
     */
    public void updateResource(){

    }

}
