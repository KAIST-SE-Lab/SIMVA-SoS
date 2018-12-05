package simvasos;

import mci.Main;
import simvasos.propcheck.PropertyChecker;
import simvasos.sa.method.SPRT;
import simvasos.scenario.robot.RobotScenario;
import simvasos.simulation.Simulator;
import simvasos.simulation.analysis.Snapshot;
import simvasos.simulation.component.Scenario;
import simvasos.simulation.component.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mgjin on 2017-06-12.
 */
public class SIMVASoS {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("old")) {
            String[] passedArgs = Arrays.copyOfRange(args, 1, args.length);
            try {
                Main.experimentMain(passedArgs);
            } catch (IOException e) {
                System.out.println("Error: The old version is not runnable");
                e.printStackTrace();
            } finally {
                System.exit(0);
            }
        }

        Scenario scenario = new RobotScenario(3);
        //initializing world
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

//        int endTick = 50;
//        int nPatient = 100;
//        int nFireFighter = 3;
//        SoSType sostype = SoSType.Virtual;
//
//        Scenario scenario = new MCIResponseScenario(sostype, nPatient, nFireFighter, 0, 0);
//        World world = scenario.getWorld();
//        PropertyChecker checker = scenario.getChecker();
//
//        SPRT sprt = new SPRT();
//
//        for (int i = 1; i < 100; i++) {
//            sprt.reset(0.05, 0.05, 0.01, 0.01 * i);
//
//            while (sprt.isSampleNeeded()) {
//                world.setSeed(new Random().nextLong());
//                ArrayList<Snapshot> simulationLog = Simulator.execute(world, endTick);
//                sprt.addSample(checker.isSatisfied(simulationLog));
//            }
//
//            System.out.println("Theta: " + (0.01 * i) + ", Sample Size: " + sprt.getSampleSize() + ", Decision: " + sprt.getDecision());
//        }
    }
}
