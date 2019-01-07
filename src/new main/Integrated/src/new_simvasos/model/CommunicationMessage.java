package new_simvasos.model;

import new_simvasos.model.Abstract.NonActionableObject;
import new_simvasos.model.Enums.EnumMessageType;

/**
 * @author ymbaek
 *
 * CommunicationMessage is a specialized type of NonActionableObject.
 * The Message can be created by CSs, and it can be sent to other CSs/Organizations through the simulation engine.
 *
 * There are two Message types supported: (1) SEND_MESSAGE, (2) REQUEST_OCCUPANCY
 * SEND_MESSAGE supports the direct communication between (among) CSs.
 * REQUEST_OCCUPANCY is used to request the occupancy of SoS-level objects such as InfraResource/Service.
 *
 * For SEND_MESSAGE, two ways of direct communication are supported:
 * (1) unicast (CS-to-CS), (2) broadcast (CS-to-Org)
 * One of those two casting methods is determined according to the msgReceiverId.
 * If msgReceiverId is CS's id, then it is unicasted.
 * Otherwise (Org's id), it will be broadcasted to the member CSs of the Org.
 */
public class CommunicationMessage extends NonActionableObject {
    //EnumMessageType msgType;  //TODO: To implement EnumMessageType

    EnumMessageType msgType;    //Type of Message: SEND_MESSAGE, REQUEST_OCCUPANCY
    String msgSenderId;         //Sender's id: it must be a single CS's id
    String msgReceiverId;       //Receiver's id: it could be either CS's id or Org's id
    String msgContents;         //Contents of a Message to be sent

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
