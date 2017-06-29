package simsos.scenario.mci;

import simsos.simulation.component.Action;
import simsos.simulation.component.Agent;
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
            return Action.getNullAction(1, "No treatment");
    }

    @Override
    public void reset() {
        Random rd = new Random();

        this.location = new Location(rd.nextInt(9), rd.nextInt(9));

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
                return "Treatment";
            }
        };
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public HashMap<String, Object> getProperties() {
        return new HashMap<String, Object>();
    }
}
