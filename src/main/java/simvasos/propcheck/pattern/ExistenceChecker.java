package simvasos.propcheck.pattern;

import simvasos.propcheck.PropertyChecker;
import simvasos.simulation.analysis.Snapshot;

import java.util.ArrayList;

/**
 * Created by mgjin on 2017-06-21.
 */
public class ExistenceChecker extends PropertyChecker {
    @Override
    protected boolean evaluate(Snapshot snapshot) {
        // Override me to evaluate whatever you want exist

        return false;
    }

    @Override
    public boolean isSatisfied(ArrayList<Snapshot> simulationLog) {
        int endTime = simulationLog.size() - 1; // 0 ... 10 => size: 11, endTime: 10

        boolean sat = false;
        for (int i = 0; i <= endTime; i++) {
            if (evaluate(simulationLog.get(i))) {
                sat = true;
                break;
            }
        }

        return sat;
    }
}
