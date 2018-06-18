/*public abstract class ExistenceUntilChecker extends ExistenceChecker{
    @Override
    protected abstract boolean evaluateState(Snapshot state, Property verificationProperty);

    @Override
    public boolean check(SimulationLog simLog, Property verificationProperty, int until) {
        int logSize = simLog.getSize(); // 0 ... 10 => size: 11, endTime: 10

        for (int i = 0; i < until && i < logSize; i++) {
            if (evaluateState(simLog.getSnapshot(i), verificationProperty)) {
                return true;
            }
        }
        return false;
    }
}*/
