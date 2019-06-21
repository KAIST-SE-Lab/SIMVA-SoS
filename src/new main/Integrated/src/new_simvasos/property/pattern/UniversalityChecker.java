package new_simvasos.property.pattern;

import new_simvasos.log.Log;
import new_simvasos.log.Snapshot;
import new_simvasos.property.Property;
import new_simvasos.property.PropertyChecker;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The type Universality checker.
 * checks whether a property is satisfied for all logs
 */
public abstract class UniversalityChecker extends PropertyChecker {
    @Override
    protected abstract boolean evaluateState(Snapshot state, Property verificationProperty);

    @Override
    public boolean check(Log log, Property verificationProperty) {
        HashMap<Integer, Snapshot> snapshots = log.getSnapshotMap();
        ArrayList keySet = new ArrayList(snapshots.keySet());
        int logSize = snapshots.size(); // 0 ... 10 => size: 11, endTime: 10
        for (int i = 0; i < logSize; i++) {
            if (!evaluateState(snapshots.get(keySet.get(i)), verificationProperty)) {
                return false;
            }
        }
        return true;
    }
}
