package simsos;

import simsos.propcheck.PropertyChecker;
import simsos.scenario.robot.Robot;
import simsos.sa.method.SPRT;
import simsos.scenario.robot.RobotScenario;
import simsos.simulation.Simulator;
import simsos.simulation.component.Agent;
import simsos.simulation.component.Scenario;
import simsos.simulation.component.Snapshot;
import simsos.simulation.component.World;

import java.util.ArrayList;

/**
 * Created by mgjin on 2017-06-12.
 */
public class SIMSoS {
    public static void main(String[] args) {
        Scenario scenario = new RobotScenario(3);
        World world = scenario.getWorld();
        PropertyChecker checker = scenario.getChecker();

        SPRT sprt = new SPRT();

        for (int i = 1; i < 100; i++) {
            sprt.reset(0.05, 0.05, 0.01, 0.01 * i);

            while (sprt.isSampleNeeded()) {
                ArrayList<Snapshot> simulationLog = Simulator.execute(world, 11);
                sprt.addSample(checker.isSatisfied(simulationLog));
            }

            System.out.println("Theta: " + (0.01 * i) + ", Sample Size: " + sprt.getSampleSize() + ", Decision: " + sprt.getDecision());
        }
    }
}
