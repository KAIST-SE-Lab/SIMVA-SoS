package simsos.simulation.component;

import java.util.ArrayList;

/**
 * Created by mgjin on 2017-06-29.
 */
public class Message extends Action {
    public enum Purpose {InfoRequest, InfoReply, InfoDelivery, Suggest, SuggestReply, Order}

    private final ArrayList<Agent> receiverCandidates;

    public String sender;
    public ArrayList<Agent> receivers = new ArrayList<Agent>();

    public Purpose purpose;

    public Message(World world, Purpose purpose) {
        super(1);

        this.receiverCandidates = world.getAgents();
        this.purpose = purpose;
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
        return "Message";
    }
}
