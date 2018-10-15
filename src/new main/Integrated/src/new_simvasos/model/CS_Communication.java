package new_simvasos.model;

import new_simvasos.model.Abstract.SIMVASoS_Operation;
import new_simvasos.model.Enums.EnumCSInterfaceType;

public class CS_Communication extends SIMVASoS_Operation {
    CS_Message csBehaviorMessage;
    EnumCSInterfaceType csInterfaceType;
    int csMessageSenderId;
    int csMessageReceiverId;


    public CS_Communication() {
        super();
        csBehaviorMessage = null;
        csInterfaceType = null;
        csMessageSenderId = -1;
        csMessageReceiverId = -1;
    }
}
