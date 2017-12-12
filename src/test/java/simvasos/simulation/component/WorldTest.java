package simvasos.simulation.component;

import org.junit.Test;
import simvasos.scenario.robot.Robot;
import simvasos.scenario.robot.RobotScenario;
import simvasos.scenario.robot.RobotWorld;
import simvasos.simulation.Simulator;
import simvasos.simulation.analysis.PropertyValue;
import simvasos.simulation.analysis.Snapshot;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class WorldTest {
    @Test
    public void addAgentTest() throws Exception {
        World world = new RobotWorld();

        assertEquals(0, world.getAgents().size());
        for (int i = 1; i <= 3; i++) {
            world.addAgent(new Robot(world, "R" + i));
            assertEquals(i, world.getAgents().size());
        }
    }

    @Test
    public void resetTest() throws Exception {
        int endTick = 1;
        int nRobot = 3;

        Scenario scenario = new RobotScenario(nRobot);
        World world = scenario.getWorld();
        world.setSeed(1);

        Simulator.execute(world, endTick); // Discard the first execution
        ArrayList<Snapshot> simulationLog = Simulator.execute(world, endTick);

        ArrayList<PropertyValue> initialProperties = simulationLog.get(0).getProperties();

        assertEquals(0, (int) initialProperties.get(0).value); // World time

        assertEquals(10, (int) initialProperties.get(1).value); // Robot1's xpos
        assertEquals(10, (int) initialProperties.get(3).value); // Robot2's xpos
        assertEquals(10, (int) initialProperties.get(5).value); // Robot3's xpos

        assertTrue((boolean) initialProperties.get(2).value); // Robot1's token
        assertTrue((boolean) initialProperties.get(4).value); // Robot2's token
        assertTrue((boolean) initialProperties.get(6).value); // Robot3's token
    }
}