package new_simvasos.model;

import new_simvasos.model.Enums.EnumChannelType;

import java.util.ArrayList;

public class CommunicationChannel extends InfraResource{
    EnumChannelType channelType;

    ArrayList<CommunicationMessage> messageQueue;

    public CommunicationChannel() {
        channelType = null;
        messageQueue = new ArrayList<>();
    }

    public CommunicationChannel(EnumChannelType channelType) {
        this.channelType = channelType;
        messageQueue = new ArrayList<>();
    }

    /* ADDER */

    void addMessage(CommunicationMessage message) {
        messageQueue.add(message);
    }

    /* RESETTER **/

    void resetMessageQueue() {
        messageQueue.clear();
    }

    /* GETTERS & SETTERS */

    public EnumChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(EnumChannelType channelType) {
        this.channelType = channelType;
    }
}
