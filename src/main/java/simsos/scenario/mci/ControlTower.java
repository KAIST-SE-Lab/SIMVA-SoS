package simsos.scenario.mci;

import simsos.simulation.component.Action;
import simsos.simulation.component.Agent;
import simsos.simulation.component.Message;
import simsos.simulation.component.World;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mgjin on 2017-06-29.
 */
public class ControlTower extends Agent {
    private String name;

    private static class RescueProcess {
        private enum Stage {Listed, Waiting, BedSecured, PTSSecured, Complete};

        private String patient;
        private Stage stage = Stage.Listed;
        private Hospital hospital;
        private PTS pts;

        public RescueProcess(String patient) {
            this.patient = patient;
        }
    }

    private ArrayList<RescueProcess> rescueRequestQueue;

    public ControlTower(World world, String name) {
        super(world);

        this.name = name;
        this.reset();
    }

    @Override
    public Action step() {
        for (RescueProcess rp : this.rescueRequestQueue) {
            if (rp.stage == RescueProcess.Stage.Listed) {
                Message msg = new Message(world, Message.Purpose.SuggestReply, "Call is Accepted");
                msg.setSender(this.getName());
                msg.setReceiver(rp.patient);
                world.messageOut(msg);

                rp.stage = RescueProcess.Stage.Waiting;
            }
        }

        return Action.getNullAction(1, "Busy");
    }

    @Override
    public void reset() {
        this.rescueRequestQueue = new ArrayList<RescueProcess>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void messageIn(Message msg) {
        if (msg.purpose == Message.Purpose.Suggest) {
//            if (msg.suggestedAction.equals("Rescue")) {
                rescueRequestQueue.add(new RescueProcess(msg.sender));
//            }
        }
    }

    @Override
    public HashMap<String, Object> getProperties() {
        return new HashMap<String, Object>();
    }
}
