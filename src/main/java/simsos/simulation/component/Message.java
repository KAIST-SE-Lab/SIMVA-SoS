package simsos.simulation.component;

import java.util.ArrayList;

/**
 * Created by mgjin on 2017-06-29.
 */
public abstract class Message extends Action {
    private enum Purpose {InfoRequest, InfoReply, InfoDelivery, Suggest, SuggestReply, Order}

    private final ArrayList<Agent> receiverCandidates;

    public String sender;
    public ArrayList<Agent> receivers = new ArrayList<Agent>();

    public Purpose purpose;

    public Message(World world, Purpose purpose, String name) {
        super(1);

        this.receiverCandidates = world.getAgents();
        this.purpose = purpose;
        this.name = name;
    }

    public void setSender(String name) {
        this.sender = name;
    }

    public void setReceiver(String name) {
        for (Agent agent : this.receiverCandidates)
            if (agent.getName().equals(name))
                this.receivers.add(agent);
    }

    public void setReceiver(Class agentType) {
        for (Agent agent : this.receiverCandidates)
            if (agentType.isInstance(agent))
                this.receivers.add(agent);
    }

    @Override
    public void execute() {
        for (Agent agent : this.receivers)
            agent.messageIn(this);
    }

    @Override
    public String getName() {
        ArrayList<String> receiverNames = new ArrayList<String>();
        for (Agent agent : this.receivers)
            receiverNames.add(agent.getName());

        return "Message <" + this.name + "> from: " + this.sender + " to: " + String.join(",", receiverNames);
    }
}
