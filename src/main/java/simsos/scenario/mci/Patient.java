package simsos.scenario.mci;

import simsos.sa.StatisticalAnalyzer;
import simsos.simulation.component.Action;
import simsos.simulation.component.Agent;
import simsos.simulation.component.Message;
import simsos.simulation.component.World;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by mgjin on 2017-06-28.
 */
public class Patient extends Agent {
    private enum Status {Initial, Waiting, Healing, Dead}
    public enum Severity {Delayed, Immediate}

    private String name;
    private Status status;
    private Severity severity;
    private int lifePoint;
    private Location location;

    private Action bleed;

    public Patient(World world, String name) {
        super(world);

        this.name = name;
        this.reset();
    }

    @Deprecated
    public Location getLocation() {
        return this.location;
    }

    @Override
    public Action step() {
        if (this.status == Status.Initial) {
            status = Status.Waiting;
            Message msg = new Message(world, Message.Purpose.Suggest, "Call For Rescue");
            msg.setSender(Patient.this.getName());
            msg.setReceiver("Control Tower");
            msg.payload.put("PatientSeverity", this.severity);
            msg.payload.put("PatientLocation", this.location);
            msg.payload.put("SuggestReply", null);

            world.messageOut(msg);
        }

        if (this.status == Status.Healing)
            return Action.getNullAction(1, Patient.this.getName() + ": Stable");
        else if (this.status == Status.Dead)
            return Action.getNullAction(1, Patient.this.getName() + ": Dead");
        else // if (this.status == Status.Waiting)
            return this.bleed;
    }

    @Override
    public void reset() {
        Random rd = new Random();

        this.status = Status.Initial;
        if (rd.nextInt(10) < 7)
            this.severity = Severity.Delayed;
        else
            this.severity = Severity.Immediate;
        this.lifePoint = 30 + (rd.nextInt(20) - 10); // 30 +- 10
        this.location = new Location(rd.nextInt(MCIWorld.MAP_SIZE.getLeft()), rd.nextInt(MCIWorld.MAP_SIZE.getRight()));

        this.bleed = new Action(1) {

            @Override
            public void execute() {
                if (lifePoint > 0)
                    lifePoint--;
                else {
                    Message msg = new Message(world, Message.Purpose.InfoDelivery, "Call For Rescue");
                    msg.setSender(Patient.this.getName());
                    msg.setReceiver("Control Tower");
                    msg.payload.put("Status", Patient.this.status);

                    world.messageOut(msg);

                    Patient.this.status = Status.Dead;
                }
            }

            @Override
            public String getName() {
                return Patient.this.getName() + ": Bleed";
            }
        };
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void messageIn(Message msg) {
        if (msg.purpose == Message.Purpose.Order) {
            if (msg.sender.startsWith("PTS")) {
                Location ptsLocation = (Location) msg.payload.get("PTSLocation");
                this.location = ptsLocation;
            } else if (msg.sender.equals("Central Hospital")) {
                Location hospitalLocation = (Location) msg.payload.get("HospitalLocation");
                this.location = hospitalLocation;

                this.status = Status.Healing;
            }
        } else if (msg.purpose == Message.Purpose.SuggestReply)
            ; // this.status = Status.Healing;
    }

    @Override
    public HashMap<String, Object> getProperties() {
        return new HashMap<String, Object>();
    }
}
