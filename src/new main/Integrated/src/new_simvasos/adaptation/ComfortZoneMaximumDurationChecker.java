package new_simvasos.adaptation;

import new_simvasos.log.Snapshot;
import new_simvasos.property.Property;
import new_simvasos.property.pattern.MaximumDurationChecker;

/*
* checks the longest duration when it is not comfort is not longer than the given maximum duration.
*
* */
public class ComfortZoneMaximumDurationChecker extends MaximumDurationChecker {
    @Override
    protected boolean evaluateState(Snapshot snapshot, Property verificationProperty) {
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
        return !comfort;
    }
}
