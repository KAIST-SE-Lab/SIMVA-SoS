package simsos.scenario.mci;

import simsos.simulation.component.Scenario;

/**
 * Created by mgjin on 2017-06-28.
 */
public class MCIScenario extends Scenario {
    public MCIScenario() {
        this.world = new MCIWorld(100);

        this.world.addAgent(new ControlTower(this.world, "Control Tower"));
//        this.world.addAgent(Hospitals);
//        this.world.addAgent(PTSs);

        this.checker = null;
    }
}
