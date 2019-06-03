package new_simvasos.adaptation;

import new_simvasos.log.Log;
import new_simvasos.log.Snapshot;
import new_simvasos.property.pattern.AbsenceChecker;
import new_simvasos.property.Property;

import java.util.StringTokenizer;

public class ComfortZoneChecker extends AbsenceChecker {
    public boolean evaluateState(Snapshot snapshot, Property verificationProperty) {
        String[] tokens = snapshot.getSnapshotString().split(" ");
        Boolean comfort = null;

        for(String token: tokens){
            String[] property = token.split(":");
            if(property[0].equals("comfort")){
                comfort = Boolean.parseBoolean(property[1]);
            }
        }
        if(comfort == null){
            System.out.println("CHECKER ERROR");
            return false;
        }
        return comfort;
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
    public boolean check(Log log, Property verificationProperty, double prob, int t, int T) {
        return false;
    }

    @Override
    public boolean check(Log log, Property verificationProperty, int t, int T) {
        return false;
    }
}
