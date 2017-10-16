package simvasos.simulation.component;

import simvasos.propcheck.PropertyChecker;

/**
 * Created by mgjin on 2017-06-22.
 */
public abstract class Scenario {
    protected World world = null;
    protected PropertyChecker checker = null;

    public World getWorld() {
        return this.world;
    }
    public PropertyChecker getChecker() {
        return checker;
    }
}
