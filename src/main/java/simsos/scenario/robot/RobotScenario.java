package simsos.scenario.robot;

import simsos.simulation.component.Scenario;
import simsos.simulation.component.World;

/**
 * Created by mgjin on 2017-06-22.
 */
public class RobotScenario extends Scenario {
    public RobotScenario(int nRobot) {
        super();

        for (int i = 1; i <= nRobot; i++)
            this.world.addAgent(new Robot(this.world, "R" + i));

        this.checker = new DropChecker();
    }
}
