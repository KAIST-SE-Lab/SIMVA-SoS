package simvasos;

import org.junit.Test;
import simvasos.propcheck.PropertyChecker;
import simvasos.sa.method.SPRT;
import simvasos.scenario.robot.RobotScenario;
import simvasos.simulation.Simulator;
import simvasos.simulation.analysis.Snapshot;
import simvasos.simulation.component.Scenario;
import simvasos.simulation.component.World;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by donghwan on 2017-12-11.
 */
public class SIMVASoSTest {

    @Test
    public void mainTest() {
        Scenario scenario = new RobotScenario(3);
        World world = scenario.getWorld();
        PropertyChecker checker = scenario.getChecker();

        SPRT sprt = new SPRT();
        sprt.reset(0.05, 0.05, 0.01, 0.2);

        while (sprt.isSampleNeeded()) {
            ArrayList<Snapshot> simulationLog = Simulator.execute(world, 11);
            sprt.addSample(checker.isSatisfied(simulationLog));
        }

        assertEquals(false, sprt.getDecision());
    }

}