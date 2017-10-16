package simvasos.scenario.mciresponse;

import simvasos.simulation.component.Action;
import simvasos.simulation.component.Message;

public class SendMessage extends Action {

    private Message message;

    public SendMessage(Message message) {
        super(0); // Needs to be fixed

        this.message = message;
    }

    @Override
    public void execute() {

    }

    @Override
    public String getName() {
        return "Send Message" +
                " from " + message.sender +
                " to " + message.receiver +
                ": " + message.getName();
    }
}
