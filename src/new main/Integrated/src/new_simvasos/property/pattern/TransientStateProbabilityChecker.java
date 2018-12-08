package new_simvasos.property.pattern;

import new_simvasos.log.Log;
import new_simvasos.log.Snapshot;
import new_simvasos.property.Property;
import new_simvasos.property.PropertyChecker;

import java.util.HashMap;

/**
 * The type Transient state probability checker.
 * checks ...
 */
public abstract class TransientStateProbabilityChecker extends PropertyChecker {
    @Override
    protected abstract boolean evaluateState(Snapshot state, Property verificationProperty);

    @Override
    public boolean check(Log log, Property verificationProperty, double prob, int t, int T) {
        HashMap<Integer, Snapshot> snapshots = log.getSnapshotMap();
        int logSize = snapshots.size(); // 0 ... 10 => size: 11, endTime: 10
        int satisfiedCount = 0;

        for (int i = t; i < logSize; i++) {
            if (evaluateState(snapshots.get(i), verificationProperty)) {
                satisfiedCount++;
            }
        }

        if ((double)satisfiedCount/(double)(T-t) >= prob){
            return true;
        }
        return false;
    }
}
