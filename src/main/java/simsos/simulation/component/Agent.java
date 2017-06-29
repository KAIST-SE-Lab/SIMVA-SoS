package simsos.simulation.component;

import java.util.HashMap;

/**
 * Created by mgjin on 2017-06-21.
 */
public abstract class Agent {
    protected World world = null;

    public Agent(World world) {
        this.world = world;
    }

    public abstract Action step();
    public abstract void reset();
    public abstract String getName();

    public abstract void messageIn(Message msg);

    public abstract HashMap<String, Object> getProperties();
}
