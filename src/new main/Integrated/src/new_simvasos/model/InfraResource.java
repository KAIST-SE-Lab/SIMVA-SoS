package new_simvasos.model;

import new_simvasos.model.Abstract.NonActionableObject;
import new_simvasos.model.Enums.EnumResType;

/**
 * @author ymbaek
 *
 * InfraResource is a specialized type of NonActionableObject.
 * And InfraResource is a component class of an Infrastructure (Infra has InfraServices and InfraResources).
 * Since InfraResource is not an autonomous object, it is non-actionable.
 */
public class InfraResource extends NonActionableObject{
    EnumResType resType;    //Type of infrastructure resource

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
