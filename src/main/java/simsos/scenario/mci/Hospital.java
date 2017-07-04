package simsos.scenario.mci;

import simsos.simulation.component.Action;
import simsos.simulation.component.Agent;
import simsos.simulation.component.Message;
import simsos.simulation.component.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by mgjin on 2017-06-28.
 */
public class Hospital extends Agent {
    private String name;
    private Location location;

    private int capacity;
    private ArrayList<Patient> inpatients;

    private Action treatment;

    public Hospital(World world, String name) {
        super(world);

        this.name = name;
        this.reset();
    }

    @Override
    public Action step() {
        if (this.inpatients.size() > 0)
            return this.treatment;
        else
            return Action.getNullAction(1, this.getName() + ": No treatment");
    }

    @Override
    public void reset() {
        Random rd = new Random();

        this.location = new Location(4, 4);

        this.capacity = 30 + (rd.nextInt(20) - 10); // 30 +- 10
        this.inpatients = new ArrayList<Patient>();

        this.treatment = new Action(1) {

            @Override
            public void execute() {
                for (Patient patient: inpatients) {
                    //give a treatment message to patient
                }
            }

            @Override
            public String getName() {
                return Hospital.this.getName() + ": Treatment";
            }
        };
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void messageIn(Message msg) {
        if (msg.purpose == Message.Purpose.InfoRequest) {
            if (msg.sender.equals("Control Tower")) {
                Message outMsg = new Message(world, Message.Purpose.InfoReply, "Reply Hospital Information");
                outMsg.setSender(this.getName());
                outMsg.setReceiver(msg.sender);
                outMsg.payload = msg.payload;
                outMsg.payload.put("Location", this.location);
                outMsg.payload.put("BedCapacity", this.capacity);
                world.messageOut(outMsg);
            }
        } else if (msg.purpose == Message.Purpose.Order) {
            if (msg.sender.equals("Control Tower"))
                ; // Secure a Bed
            else if (msg.sender.startsWith("PTS")) {
                String patientName = (String) msg.payload.get("PatientName");
//                inpatients.add()

                Message outMsg = new Message(world, Message.Purpose.Order, "Order to Complete Rescue");
                outMsg.setSender(this.getName());
                outMsg.setReceiver(patientName);
                outMsg.payload.put("PatientName", patientName);
                world.messageOut(outMsg);

                outMsg = new Message(world, Message.Purpose.Order, "Order to Relieve");
                outMsg.setSender(this.getName());
                outMsg.setReceiver(patientName);
                outMsg.payload.put("HospitalLocation", this.location);
                world.messageOut(outMsg);
            }
        }
    }

    @Override
    public HashMap<String, Object> getProperties() {
        return new HashMap<String, Object>();
    }
}
