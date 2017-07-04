package simsos.scenario.mci;

import simsos.simulation.component.Action;
import simsos.simulation.component.Agent;
import simsos.simulation.component.Message;
import simsos.simulation.component.World;

import java.util.*;

/**
 * Created by mgjin on 2017-06-29.
 */
public class ControlTower extends Agent {
    private String name;
    private int bedCapacity = 0;

    private static class RescueProcess {
        private enum Stage {Listed, BedSecured, UtilityCollected, PTSSecured, Complete};

        private String patient;
        private Patient.Severity patientSeverity;
        private Location patientLocation;
        private Stage stage = Stage.Listed;

        private String hospital;
        private Location hospitalLocation;
        private String pts;

        private ArrayList<Pair<String, Integer>> utilities = new ArrayList<Pair<String, Integer>>();

        public RescueProcess(String patient) {
            this.patient = patient;
        }
    }

    private HashMap<String, RescueProcess> rescueRequestQueue;

    public ControlTower(World world, String name) {
        super(world);

        this.name = name;
        this.reset();
    }

    @Override
    public Action step() {
        if (world.getTime() == 0) {
            Message msg = new Message(world, Message.Purpose.InfoRequest, "Request Hospital Information");
            msg.setSender(this.getName());
            msg.setReceiver(Hospital.class);
            msg.payload.put("Location", null);
            msg.payload.put("BedCapacity", null);
            world.messageOut(msg);
        }

        for (RescueProcess rp : this.rescueRequestQueue.values()) {
            if (rp.stage == RescueProcess.Stage.Listed) {
                if (this.bedCapacity > 0) {
                    this.bedCapacity--;

                    Message msg = new Message(world, Message.Purpose.Order, "Secure a Bed");
                    msg.setSender(this.getName());
                    msg.setReceiver("Central Hospital");
                    world.messageOut(msg);

                    rp.hospital = "Central Hospital";
                    rp.hospitalLocation = new Location(4, 4); // FIX ME
                    rp.stage = RescueProcess.Stage.BedSecured;
                }
            }

            if (rp.stage == RescueProcess.Stage.UtilityCollected) {
                if (rp.utilities.size() > 0) {
                    Collections.sort(rp.utilities,
                            (Pair<String, Integer> o1, Pair<String, Integer> o2) -> o1.getRight().compareTo(o2.getRight()));

                    Message msg = new Message(world, Message.Purpose.Suggest, "Reqeust to Rescue the Patient");
                    msg.setSender(this.getName());
                    msg.setReceiver(rp.utilities.get(0).getLeft());
                    msg.payload.put("PatientName", rp.patient);
                    msg.payload.put("PatientLocation", rp.patientLocation);
                    msg.payload.put("HospitalName", rp.hospital);
                    msg.payload.put("HospitalLocation", rp.hospitalLocation);
                    msg.payload.put("SuggestReply", null);
                    world.messageOut(msg);
                } else {
                    rp.stage = RescueProcess.Stage.BedSecured;
                }

                rp.utilities.clear();
            }

            if (rp.stage == RescueProcess.Stage.BedSecured) {
                Message msg = new Message(world, Message.Purpose.InfoRequest, "Request Utility");
                msg.setSender(this.getName());
                msg.setReceiver(PTS.class);
                msg.payload.put("PatientName", rp.patient);
                msg.payload.put("PatientSeverity", rp.patientSeverity);
                msg.payload.put("PatientLocation", rp.patientLocation);
                msg.payload.put("HospitalName", rp.hospital);
                msg.payload.put("HospitalLocation", rp.hospitalLocation);
                msg.payload.put("Utility", null);
                world.messageOut(msg);

                rp.stage = RescueProcess.Stage.UtilityCollected;
            }
        }

        return Action.getNullAction(1, this.getName() + ": Busy");
    }

    @Override
    public void reset() {
        this.rescueRequestQueue = new HashMap<String, RescueProcess>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void messageIn(Message msg) {
        if (msg.purpose == Message.Purpose.InfoReply) {
            if (msg.sender.equals("Central Hospital")) {
                this.bedCapacity = (int) msg.payload.get("BedCapacity");
                System.out.println(">>>>> Capacity: " + this.bedCapacity);
            } else if (msg.sender.startsWith("PTS")) {
                String patient = (String) msg.payload.get("PatientName");
                int utility = (int) msg.payload.get("Utility");
                System.out.println(">>>>> Utility: " + utility + " for " + msg.payload.get("PatientLocation"));

                RescueProcess rp = this.rescueRequestQueue.get(patient);
                rp.utilities.add(new Pair(msg.sender, utility));
            }
        } else if (msg.purpose == Message.Purpose.InfoDelivery) {
            if (msg.sender.startsWith("Patient")) {
                RescueProcess rp = rescueRequestQueue.get(msg.sender);
                // Dead
                rp.stage = RescueProcess.Stage.Complete;
            }
        } else if (msg.purpose == Message.Purpose.Suggest) {
            if (msg.sender.startsWith("Patient")) {
                RescueProcess rp = new RescueProcess(msg.sender);
                rp.patientSeverity = (Patient.Severity) msg.payload.get("PatientSeverity");
                rp.patientLocation = (Location) msg.payload.get("PatientLocation");
                rp.stage = RescueProcess.Stage.Listed;

                rescueRequestQueue.put(msg.sender, rp);
            }
        } else if (msg.purpose == Message.Purpose.SuggestReply) {
            if (msg.sender.startsWith("PTS")) {
                String patient = (String) msg.payload.get("PatientName");
                boolean SuggestReply = (boolean) msg.payload.get("SuggestReply");

                RescueProcess rp = this.rescueRequestQueue.get(patient);

                if (SuggestReply) {
                    rp.pts = msg.sender;
                    rp.stage = RescueProcess.Stage.PTSSecured;

                    msg = new Message(world, Message.Purpose.SuggestReply, "Call is Accepted");
                    msg.setSender(this.getName());
                    msg.setReceiver(patient);
                    msg.payload.put("SuggestReply", true);
                    world.messageOut(msg);
                } else {
                    if (rp.stage != RescueProcess.Stage.PTSSecured)
                        rp.stage = RescueProcess.Stage.BedSecured;
                }
            }
        } else if (msg.purpose == Message.Purpose.Order) {
            if (msg.sender.equals("Central Hospital")) {
                String patient = (String) msg.payload.get("PatientName");

                RescueProcess rp = this.rescueRequestQueue.get(patient);
                rp.pts = msg.sender;
                rp.stage = RescueProcess.Stage.Complete;
            }
        }

    }

    @Override
    public HashMap<String, Object> getProperties() {
        return new HashMap<String, Object>();
    }
}
