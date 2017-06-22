package simsos.simulation.component;

/**
 * Created by mgjin on 2017-06-21.
 */
public abstract class Agent {
    private World world = null;

    public Agent(World world) {
        this.world = world;
    }

    public abstract Action step();
    public abstract void reset();
    public abstract String getName();
}
