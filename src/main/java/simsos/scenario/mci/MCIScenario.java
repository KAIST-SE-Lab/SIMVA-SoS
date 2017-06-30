package simsos.scenario.mci;

import simsos.simulation.component.Scenario;

/**
 * Created by mgjin on 2017-06-28.
 */
public class MCIScenario extends Scenario {
    public MCIScenario() {
        this.world = new MCIWorld(2);

        this.world.addAgent(new ControlTower(this.world, "Control Tower"));
        this.world.addAgent(new Hospital(this.world, "Central Hospital"));
        this.world.addAgent(new PTS(this.world, "PTS1"));
        this.world.addAgent(new PTS(this.world, "PTS2"));

        this.checker = null;
    }
}
