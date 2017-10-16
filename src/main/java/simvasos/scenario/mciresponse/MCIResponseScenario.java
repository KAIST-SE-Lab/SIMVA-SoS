package simvasos.scenario.mciresponse;

import simvasos.scenario.mciresponse.entity.*;
import simvasos.simulation.component.Scenario;
import simvasos.simulation.util.*;

public class MCIResponseScenario extends Scenario {
    public enum SoSType {Virtual, Collaborative, Acknowledged, Directed}

    public MCIResponseScenario(SoSType type, int nPatient, int nFireFighter, int nAmbulance, int nHospital) {
        this.world = new MCIResponseWorld(type, nPatient);

        for (int i = 1; i <= nFireFighter; i++)
            this.world.addAgent(new FireFighter(this.world, "FireFighter" + i));
        for (int i = 1; i <= nAmbulance; i++)
            this.world.addAgent(new Ambulance(this.world, "Ambulance" + i, new Location(MCIResponseWorld.MAP_SIZE.getLeft() / 2, MCIResponseWorld.MAP_SIZE.getRight() / 2)));
        for (int i = 1; i <= nHospital; i++)
            this.world.addAgent(new Hospital(this.world, "Hospital" + i, new Location(MCIResponseWorld.MAP_SIZE.getLeft() / 2, MCIResponseWorld.MAP_SIZE.getRight() / 2), 100));

        if (type != SoSType.Virtual)
            this.world.addAgent(new ControlTower(this.world, "ControlTower"));

        this.checker = new PulloutChecker(2);
    }
}
