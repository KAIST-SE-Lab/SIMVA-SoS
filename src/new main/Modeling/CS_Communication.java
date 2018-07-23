import Abstract.SIMVASoS_Behavior;
import Enums.EnumCSInterfaceType;

public class CS_Communication extends SIMVASoS_Behavior {
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
