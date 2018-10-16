package new_simvasos.model;

import new_simvasos.model.Abstract.SIMVASoS_Operation;

public abstract class CS_Operation extends SIMVASoS_Operation {
    String csBehaviorLog;


    public CS_Operation() {
        super();
        csBehaviorLog = "noLog";
    }
}
