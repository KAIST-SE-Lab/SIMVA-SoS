package simvasos.propcheck.pattern;

import simvasos.propcheck.PropertyChecker;
import simvasos.simulation.analysis.Snapshot;

import java.util.ArrayList;

/**
 * Created by mgjin on 2017-06-21.
 */
public class TransientStateProbabilityChecker extends PropertyChecker {
    @Override
    protected boolean evaluate(Snapshot snapshot) {
        return false;
    }

    @Override
    public boolean isSatisfied(ArrayList<Snapshot> simulationLog) {
        return false;
    }
}
