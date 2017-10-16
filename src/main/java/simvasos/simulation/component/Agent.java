package simvasos.simulation.component;

import simvasos.simulation.analysis.HasName;

import java.util.HashMap;

/**
 * Created by mgjin on 2017-06-21.
 */
public abstract class Agent implements HasName {
    protected World world = null;
    protected String name = null;

    public Agent(World world) {
        this.world = world;
    }

    public abstract Action step();
    public abstract void reset();
    public abstract String getName();

    public abstract HashMap<String, Object> getProperties();
}
