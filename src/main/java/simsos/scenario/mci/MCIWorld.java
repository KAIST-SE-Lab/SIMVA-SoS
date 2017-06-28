package simsos.scenario.mci;

import org.apache.commons.math3.distribution.NormalDistribution;
import simsos.simulation.component.Action;
import simsos.simulation.component.World;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mgjin on 2017-06-28.
 */
public class MCIWorld extends World {
    // Geographical Map
    private int[] patientRaisePlan;
    private int patientNumbering = 0;

    public MCIWorld(int nPatient) {
        ArrayList<Integer> raiseTime = new ArrayList<>();
        NormalDistribution nd = new NormalDistribution(30, 15);

        for(int i=0; i< nPatient; i++)
            raiseTime.add(new Double(nd.sample()).intValue());

        Collections.sort(raiseTime);
        for(int i =0; i< raiseTime.size(); i++)
            if (raiseTime.get(i) < 0)
                raiseTime.set(i, 0);

        this.patientRaisePlan = raiseTime.stream().mapToInt(i -> i).toArray();
    }

    @Override
    public void reset() {
        super.reset();

        this.patientNumbering = 0;
        this.agents.removeIf(a -> a instanceof Patient);
    }

    @Override
    public ArrayList<Action> generateExogenousActions() {
        ArrayList<Action> patients = new ArrayList<Action>();
        World world = this;

        for (int raiseTime : patientRaisePlan)
            if (raiseTime == this.time)
                patients.add(new Action(0) {

                    @Override
                    public void execute() {
                        patientNumbering++;
                        Patient p = new Patient(world, "Patient" + patientNumbering);
                        agents.add(p);
                        System.out.println(p.getName() + " " + p.getLocation());
                    }

                    @Override
                    public String getName() {
                        return "Generate a Patient";
                    }
                });

        return patients;
    }
}
