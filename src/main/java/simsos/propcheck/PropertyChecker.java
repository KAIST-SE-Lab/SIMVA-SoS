package simsos.propcheck;

import simsos.simulation.component.Snapshot;
import simsos.simulation.component.World;

import java.util.ArrayList;

/**
 * Created by mgjin on 2017-06-21.
 */
public abstract class PropertyChecker {
    protected abstract boolean evaluate(Snapshot snapshot);
    public abstract boolean isSatisfied(ArrayList<Snapshot> simulationLog);
}
