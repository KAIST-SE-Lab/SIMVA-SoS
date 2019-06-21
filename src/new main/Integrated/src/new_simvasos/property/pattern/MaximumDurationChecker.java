package new_simvasos.property.pattern;

import new_simvasos.log.Log;
import new_simvasos.log.Snapshot;
import new_simvasos.property.Property;
import new_simvasos.property.PropertyChecker;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The type Maximum duration checker.
 * checks whether a length of a property satisfaction in the log is shorter than the maximum duration requirements
 * writer: Yong-Jun Shin
 */

public abstract class MaximumDurationChecker extends PropertyChecker {
    @Override
    protected abstract boolean evaluateState(Snapshot snapshot, Property verificationProperty);

    public boolean check(Log log, Property verificationProperty, int t, int T) {
        HashMap<Integer, Snapshot> snapshots = log.getSnapshotMap();
        ArrayList keySet = new ArrayList(snapshots.keySet());
        int logSize = snapshots.size(); // 0 ... 10 => size: 11, endTime: 10

        int maxSatisfiedCount = 0;
        int satisfiedCount = 0;
        for (int i = t; i < logSize; i++) {
            if (evaluateState(snapshots.get(keySet.get(i)), verificationProperty)) {
                satisfiedCount++;
            }
            else{
                if(satisfiedCount > maxSatisfiedCount){
                    maxSatisfiedCount = satisfiedCount;
                }
                satisfiedCount = 0;
            }
        }

        if (maxSatisfiedCount <= T){
            return true;
        }
        return false;
    }

    @Override
    public boolean check(Log log, Property verificationProperty) {
        return false;
    }

    @Override
    public boolean check(Log log, Property verificationProperty, int until) {
        return false;
    }

    @Override
    public boolean check(Log log, Property verificationProperty, double prob, int T) {
        return false;
    }

    @Override
    public boolean check(Log log, Property verificationProperty, double prob, int t, int T) { return false;}
}
