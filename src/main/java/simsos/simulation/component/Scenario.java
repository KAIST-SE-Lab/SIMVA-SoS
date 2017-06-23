package simsos.simulation.component;

import simsos.propcheck.PropertyChecker;
import simsos.simulation.component.World;

/**
 * Created by mgjin on 2017-06-22.
 */
public abstract class Scenario {
    protected World world = new World();
    protected PropertyChecker checker = null;

    public World getWorld() {
        return this.world;
    }
    public PropertyChecker getChecker() {
        return checker;
    }
}
