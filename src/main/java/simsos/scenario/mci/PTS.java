package simsos.scenario.mci;

import simsos.simulation.component.Action;
import simsos.simulation.component.Agent;
import simsos.simulation.component.Message;
import simsos.simulation.component.World;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by mgjin on 2017-06-28.
 */
public class PTS extends Agent {
    private enum Status {Waiting, GoingToPatient, GoingToHospital}

    private String name;
    private Status status;
    private Location location;

    private String patientName;
    private Location patientLocation;

    private String hospitalName;
    private Location hospitalLocation;

    public PTS(World world, String name) {
        super(world);

        this.name = name;
        this.reset();
    }

    @Override
    public Action step() {
        if (this.status == Status.GoingToPatient) {
            if (!this.location.equals(patientLocation)) {
                return new Action(1) {

                    @Override
                    public void execute() {
                        if (PTS.this.location.getX() < PTS.this.patientLocation.getX())
                            PTS.this.location.moveX(1);
                        else if (PTS.this.location.getX() > PTS.this.patientLocation.getX())
                            PTS.this.location.moveX(-1);
                        else if (PTS.this.location.getY() < PTS.this.patientLocation.getY())
                            PTS.this.location.moveY(1);
                        else if (PTS.this.location.getY() > PTS.this.patientLocation.getY())
                            PTS.this.location.moveY(-1);
                    }

                    @Override
                    public String getName() {
                        return PTS.this.getName() + ": Goes to Patient";
                    }
                };
            } else {
                Message outMsg = new Message(world, Message.Purpose.Order, "Order to Get In");
                outMsg.setSender(this.getName());
                outMsg.setReceiver(this.patientName);
                outMsg.payload.put("PTSLocation", this.location);
                world.messageOut(outMsg);

                this.patientLocation = null;

                this.status = Status.GoingToHospital;
            }
        }

        if (this.status == Status.GoingToHospital) {
            if (!this.location.equals(hospitalLocation)) {
                return new Action(1) {

                    @Override
                    public void execute() {
                        if (PTS.this.location.getX() < PTS.this.hospitalLocation.getX())
                            PTS.this.location.moveX(1);
                        else if (PTS.this.location.getX() > PTS.this.hospitalLocation.getX())
                            PTS.this.location.moveX(-1);
                        else if (PTS.this.location.getY() < PTS.this.hospitalLocation.getY())
                            PTS.this.location.moveY(1);
                        else if (PTS.this.location.getY() > PTS.this.hospitalLocation.getY())
                            PTS.this.location.moveY(-1);
                    }

                    @Override
                    public String getName() {
                        return PTS.this.getName() + ": Goes to Hospital";
                    }
                };
            } else {
                Message outMsg = new Message(world, Message.Purpose.Order, "Order to Take the Patient");
                outMsg.setSender(this.getName());
                outMsg.setReceiver(this.hospitalName);
                outMsg.payload.put("PatientName", this.patientName);
                world.messageOut(outMsg);

                this.patientName = null;
                this.hospitalName = null;
                this.hospitalLocation = null;

                this.status = Status.Waiting;
            }
        }

//        if (this.status == Status.Waiting) {
        return Action.getNullAction(1, this.getName() + ": Waiting");
//        }
    }

    @Override
    public void reset() {
        Random rd = new Random();

        this.status = Status.Waiting;
        this.location = new Location(MCIWorld.MAP_SIZE.getLeft() / 2, MCIWorld.MAP_SIZE.getRight() / 2);
    }

    private int getUtility(Patient.Severity severity, Location patientLocation, Location hospitalLocation) {
        return this.location.distanceTo(patientLocation) + patientLocation.distanceTo(hospitalLocation);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void messageIn(Message msg) {
        if (msg.purpose == Message.Purpose.InfoRequest) {
            if (msg.sender.equals("Control Tower")) {
                if (this.status == Status.Waiting) {
                    Patient.Severity patientSeverity = (Patient.Severity) msg.payload.get("PatientSeverity");
                    Location patientLocation = (Location) msg.payload.get("PatientLocation");
                    Location hospitalLocation = (Location) msg.payload.get("HospitalLocation");

                    Message outMsg = new Message(world, Message.Purpose.InfoReply, "Reply Utility");
                    outMsg.setSender(this.getName());
                    outMsg.setReceiver(msg.sender);
                    outMsg.payload = msg.payload;
                    outMsg.payload.put("Utility", this.getUtility(patientSeverity, patientLocation, hospitalLocation));
                    world.messageOut(outMsg);
                }
            }
        } else if (msg.purpose == Message.Purpose.Suggest) {
            if (msg.sender.equals("Control Tower")) {
                if (this.status == Status.Waiting) {
                    this.patientName = (String) msg.payload.get("PatientName");
                    this.patientLocation = (Location) msg.payload.get("PatientLocation");
                    this.hospitalName = (String) msg.payload.get("HospitalName");
                    this.hospitalLocation = (Location) msg.payload.get("HospitalLocation");

                    Message outMsg = new Message(world, Message.Purpose.SuggestReply, "Reply Rescue");
                    outMsg.setSender(this.getName());
                    outMsg.setReceiver(msg.sender);
                    outMsg.payload = msg.payload;
                    outMsg.payload.put("SuggestReply", true);
                    world.messageOut(outMsg);

                    this.status = Status.GoingToPatient;
                } else { // if (this.status == Status.GoingToPatient || this.status == Status.GoingToHospital) {
                    Message outMsg = new Message(world, Message.Purpose.SuggestReply, "Reply Rescue");
                    outMsg.setSender(this.getName());
                    outMsg.setReceiver(msg.sender);
                    outMsg.payload = msg.payload;
                    outMsg.payload.put("SuggestReply", false);
                    world.messageOut(outMsg);
                }
            }
        }
    }

    @Override
    public HashMap<String, Object> getProperties() {
        return new HashMap<String, Object>();
    }
}
