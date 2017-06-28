package simsos.scenario.mci;

import simsos.simulation.component.Action;
import simsos.simulation.component.Agent;
import simsos.simulation.component.World;

import java.util.HashMap;

/**
 * Created by mgjin on 2017-06-28.
 */
public class Hospital extends Agent {
    private String name;

    public Hospital(World world, String name) {
        super(world);

        this.name = name;
        this.reset();
    }

    @Override
    public Action step() {
        return null;
    }

    @Override
    public void reset() {

    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public HashMap<String, Object> getProperties() {
        return null;
    }
}
