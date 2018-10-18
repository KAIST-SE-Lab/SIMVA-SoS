package new_simvasos.model;

import new_simvasos.model.Enums.EnumChannelType;

import java.util.ArrayList;

/**
 * @author ymbaek
 *
 * CommunicationChannel is a specialized type of InfraResource.
 * InfraResource is a NonActionableObject, thus CommunicationChannel is basically non-actionable.
 * CommunicationChannel is built on (and managed by) the SoS-level infrastructure,
 * and the channel can be used by multiple CSs for communication purposes.
 */
public class CommunicationChannel extends InfraResource{
    EnumChannelType channelType;                    //Type of channel (media, bus-type, interface, and so on)
    ArrayList<CommunicationMessage> messageQueue;   //Queue to store messages from CSs

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
