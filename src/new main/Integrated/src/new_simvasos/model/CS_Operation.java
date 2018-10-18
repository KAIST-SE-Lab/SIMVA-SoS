package new_simvasos.model;

import new_simvasos.model.Abstract.SIMVASoS_Operation;

/**
 * @author ymbaek
 *
 * CS_operation is a specialized type of SIMVASoS_Operation.
 * Thus, multiple CS_Operation can be included in a single SIMVASoS_Action (CS_Action).
 * Definition of CS_Operations is not mandatory, but it enables more systematic action definition.
 *
 * SIMVASoS_Operation has its operationType and abstract method operate()
 */
public abstract class CS_Operation extends SIMVASoS_Operation {
    String csBehaviorLog;

    public CS_Operation() {
        super();
        csBehaviorLog = "noLog";
    }

    //TODO: Find usage of CS_Operation (also, SIMVASoS_Operation) and its attributes/operations
}
