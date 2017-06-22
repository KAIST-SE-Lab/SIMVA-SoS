package simsos;

import simsos.modelparsing.modeling.Robot;
import simsos.sa.method.SPRT;
import simsos.simulation.Simulator;
import simsos.simulation.component.Agent;
import simsos.simulation.component.World;

/**
 * Created by mgjin on 2017-06-12.
 */
public class SIMSoS {
    public static void main(String[] args) {
        World world = new World();
        world.addAgent(new Robot(world, "R1"));
        world.addAgent(new Robot(world, "R2"));
        world.addAgent(new Robot(world, "R3"));

        SPRT sprt = new SPRT();

        for (int i = 1; i < 100; i++) {
            sprt.reset(0.05, 0.05, 0.01, 0.01 * i);

            while (sprt.isSampleNeeded()) {
                Simulator.execute(world, 11);

                boolean res = true;

                for (Agent agent : world.getAgents()) {
                    Robot r = (Robot) agent;
//                    System.out.println(r.getName() + ", " + r.xpos + ", " + r.token);
                    res = res && r.token;
                }
//                System.out.println("---");
                // True: All have
                // False: At least one dropped

                // True: At least one dropped
                // False: All have
                sprt.addSample(!res);
            }

            System.out.println("Theta: " + (0.01 * i) + ", Sample Size: " + sprt.getSampleSize() + ", Decision: " + sprt.getDecision());
        }
    }
}
