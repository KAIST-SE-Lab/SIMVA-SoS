package new_simvasos.model;

import new_simvasos.model.Abstract.NonActionableObject;
import new_simvasos.model.Enums.EnumMessageType;

public class CommunicationMessage extends NonActionableObject {
    //EnumMessageType msgType;  //TODO: To implement EnumMessageType

    EnumMessageType msgType;
    String msgSenderId;
    String msgReceiverId;
    String msgContents;

    public CommunicationMessage() {
        msgType = null;
        msgSenderId = "noId";
        msgReceiverId = "noId";
        msgContents = null;
    }

    public CommunicationMessage(EnumMessageType msgType) {
        this.msgType = msgType;
    }

    public CommunicationMessage(EnumMessageType msgType, String msgSenderId, String msgReceiverId, String msgContents) {
        this.msgType = msgType;
        this.msgSenderId = msgSenderId;
        this.msgReceiverId = msgReceiverId;
        this.msgContents = msgContents;
    }

    /* GETTERS & SETTERS */

    public EnumMessageType getMsgType() {
        return msgType;
    }

    public void setMsgType(EnumMessageType msgType) {
        this.msgType = msgType;
    }

    public String getMsgSenderId() {
        return msgSenderId;
    }

    public void setMsgSenderId(String msgSenderId) {
        this.msgSenderId = msgSenderId;
    }

    public String getMsgReceiverId() {
        return msgReceiverId;
    }

    public void setMsgReceiverId(String msgReceiverId) {
        this.msgReceiverId = msgReceiverId;
    }

    public String getMsgContents() {
        return msgContents;
    }

    public void setMsgContents(String msgContents) {
        this.msgContents = msgContents;
    }
}
