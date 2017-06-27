package simsos.scenario.robot;

import simsos.simulation.component.Action;
import simsos.simulation.component.World;

import java.util.ArrayList;

/**
 * Created by mgjin on 2017-06-27.
 */
public class RobotWorld extends World {
    @Override
    public ArrayList<Action> generateExogenousActions() {
        return new ArrayList<Action>();
    }
}
