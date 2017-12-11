package simvasos.scenario.mciresponse;

import org.junit.Test;
import simvasos.simulation.Simulator;
import simvasos.simulation.analysis.Snapshot;
import simvasos.simulation.component.Scenario;
import simvasos.simulation.component.World;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class MCIResponseScenarioTest {

    @Test
    public void test() {
        MCIResponseScenario.SoSType[] targetTypeArray = {MCIResponseScenario.SoSType.Virtual, MCIResponseScenario.SoSType.Collaborative, MCIResponseScenario.SoSType.Acknowledged, MCIResponseScenario.SoSType.Directed};

        int endTick = 70; // 8000
        int minTrial = 1;
        int maxTrial = 10;

        ArrayList<Snapshot> trace;

        for (MCIResponseScenario.SoSType sostype : targetTypeArray) {
            Scenario scenario = new MCIResponseScenario(sostype, 50, 2, 0, 0);
            World world = scenario.getWorld();

            for (int i = minTrial - 1; i <= maxTrial; i++) {
                world.setSeed(new Random().nextLong());
                ((MCIResponseWorld) world).setSoSType(sostype);

                trace = Simulator.execute(world, endTick);

                assertNotNull(trace);
            }
        }

    }

}