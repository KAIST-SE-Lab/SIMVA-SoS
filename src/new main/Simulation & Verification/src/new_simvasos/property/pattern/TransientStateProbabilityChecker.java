/*public abstract class TransientStateProbabilityChecker extends PropertyChecker {
    @Override
    protected abstract boolean evaluateState(Snapshot state, Property verificationProperty);

    @Override
    public boolean check(SimulationLog simLog, Property verificationProperty, float prob, int t, int T) {
        int logSize = simLog.getSize(); // 0 ... 10 => size: 11, endTime: 10
        int satisfiedCount = 0;

        for (int i = t; i < logSize; i++) {
            if (evaluateState(simLog.getSnapshot(i), verificationProperty)) {
                satisfiedCount++;
            }
        }

        if (satisfiedCount/T-t >= prob){
            return true;
        }
        return false;
    }
}*/
