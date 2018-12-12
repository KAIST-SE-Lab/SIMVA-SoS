package new_simvasos.model;

import new_simvasos.model.Abstract.NonActionableObject;
import new_simvasos.model.Enums.EnumResType;

/**
 * @author ymbaek
 *
 * CS_Resource is a specialized type of NonActionableObject.
 * To represent a specific resource, it has its resource type.
 */
public class CS_Resource extends NonActionableObject{
    EnumResType csResType;  //Type of CS-level resource

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
