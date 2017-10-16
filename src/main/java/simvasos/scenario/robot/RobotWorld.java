package simvasos.scenario.robot;

import simvasos.simulation.component.Action;
import simvasos.simulation.component.World;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mgjin on 2017-06-27.
 */
public class RobotWorld extends World {
    public RobotWorld() {
        super(1);
    }

    @Override
    public HashMap<String, Object> getResources() {
        return null;
    }

    @Override
    public ArrayList<Action> generateExogenousActions() {
        return new ArrayList<Action>();
    }
}
