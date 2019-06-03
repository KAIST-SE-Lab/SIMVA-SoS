package new_simvasos.adaptation;

import new_simvasos.log.Snapshot;
import new_simvasos.property.Property;
import new_simvasos.property.pattern.MinimumDurationChecker;

import java.util.StringTokenizer;

public class ComfortZoneMinimumDurationChecker extends MinimumDurationChecker {
    
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
