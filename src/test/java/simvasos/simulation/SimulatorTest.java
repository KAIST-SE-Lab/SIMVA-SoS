package simvasos.simulation;

import org.junit.Test;
import simvasos.scenario.mciresponse.MCIResponseScenario;
import simvasos.scenario.mciresponse.MCIResponseWorld;
import simvasos.scenario.robot.RobotScenario;
import simvasos.simulation.analysis.PropertyValue;
import simvasos.simulation.analysis.Snapshot;
import simvasos.simulation.component.Scenario;
import simvasos.simulation.component.World;
import simvasos.simulation.util.Location;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SimulatorTest {
    int randomSeed = 1;

    int nRobot = 3;
    int robotEndTick = 11;
    Scenario robotScenario = null;
    World robotWorld = null;

    int nPatient = 100;
    int nFirefighter = 2;
    int mciResponseEndTick = 200;
    Scenario mciResponseScenario = null;
    MCIResponseWorld mciResponseWorld = null;

    public SimulatorTest() {
        this.robotScenario = new RobotScenario(3);
        this.robotWorld = this.robotScenario.getWorld();
        this.robotWorld.setSeed(this.randomSeed);
    }

    @Test
    public void executionSanityTest() throws Exception {
        ArrayList<Snapshot> simulationLog = Simulator.execute(this.robotWorld, this.robotEndTick);

        assertNotNull(simulationLog);
        assertEquals(this.robotEndTick + 1, simulationLog.size());
    }

    @Test
    public void robotScenarioTest() throws Exception {
        ArrayList<Snapshot> simulationLog = Simulator.execute(this.robotWorld, this.robotEndTick);

        ArrayList<PropertyValue> finalProperties = simulationLog.get(this.robotEndTick).getProperties();

        assertEquals(nRobot * 2 + 1, finalProperties.size());

        assertEquals(this.robotEndTick, (int) finalProperties.get(0).value); // World time

        assertEquals(21, (int) finalProperties.get(1).value); // Robot1's xpos
        assertEquals(21, (int) finalProperties.get(3).value); // Robot2's xpos
        assertEquals(21, (int) finalProperties.get(5).value); // Robot3's xpos

        assertTrue((boolean) finalProperties.get(2).value); // Robot1's token
        assertTrue((boolean) finalProperties.get(4).value); // Robot2's token
        assertTrue((boolean) finalProperties.get(6).value); // Robot3's token
    }

    @Test
    public void mciResponseSoSTypeTest() throws Exception {
        MCIResponseScenario.SoSType[] targetTypeArray = {MCIResponseScenario.SoSType.Virtual, MCIResponseScenario.SoSType.Collaborative, MCIResponseScenario.SoSType.Acknowledged, MCIResponseScenario.SoSType.Directed};

        for (MCIResponseScenario.SoSType sostype : targetTypeArray) {
            this.mciResponseScenario = new MCIResponseScenario(sostype, this.nPatient, this.nFirefighter, 0, 0);
            this.mciResponseWorld = (MCIResponseWorld) this.mciResponseScenario.getWorld();

            assertEquals(sostype, this.mciResponseWorld.type);
        }
    }

    @Test
    public void mciResponseSccenarioTest() throws Exception {
        MCIResponseScenario.SoSType sostype = MCIResponseScenario.SoSType.Acknowledged;

        this.mciResponseScenario = new MCIResponseScenario(sostype, this.nPatient, this.nFirefighter, 1, 1);
        this.mciResponseWorld = (MCIResponseWorld) this.mciResponseScenario.getWorld();
        this.mciResponseWorld.setSeed(this.randomSeed);
        this.mciResponseWorld.setSoSType(sostype);

        ArrayList<Snapshot> simulationLog = Simulator.execute(this.mciResponseWorld, this.mciResponseEndTick);

        ArrayList<PropertyValue> finalProperties = simulationLog.get(this.mciResponseEndTick).getProperties();

        assertEquals(9, finalProperties.size()); // 1 + this.nFirefighter + 1 + 2 + 1 + 2

        assertEquals(this.mciResponseEndTick, finalProperties.get(0).value); // World time

        assertEquals(new Location(21, 26), finalProperties.get(1).value); // Firefighter1's location
        assertEquals(new Location(25, 21), finalProperties.get(2).value); // Firefighter2's location

        assertEquals(new Location(19, 21), finalProperties.get(3).value); // Ambulance's location

        assertEquals(95, (int) finalProperties.get(4).value); // Hospital's capacity
        assertEquals(new Location(24, 24), finalProperties.get(5).value); // Hospital's location

//        assertNotNull(finalProperties.get(6).value); // Control Tower's pullout belief map

//        assertEquals(11, (int) finalProperties.get(7).value); // The number of pulled out people
//        assertEquals(2010, (int) finalProperties.get(8).value); // The number of message counts
    }
}