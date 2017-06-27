package simsos.scenario.robot;

import simsos.simulation.component.Scenario;

/**
 * Created by mgjin on 2017-06-22.
 */
public class RobotScenario extends Scenario {
    public RobotScenario(int nRobot) {
        super();

        this.world = new RobotWorld();

        for (int i = 1; i <= nRobot; i++)
            this.world.addAgent(new Robot(this.world, "R" + i));

        this.checker = new DropChecker();
    }
}
