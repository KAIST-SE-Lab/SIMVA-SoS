package new_simvasos.property;

import new_simvasos.log.Snapshot;
import new_simvasos.property.pattern.MinimumDurationChecker;

import java.util.StringTokenizer;

public class MCIMinimumDurationChecker extends MinimumDurationChecker {
    
    @Override
    protected boolean evaluateState(Snapshot snapshot, Property verificationProperty) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        while(st.hasMoreTokens()) {
            if(st.nextToken().equals("RescuedRate:"))
                break;
        }
    
        double rescueRate = Double.parseDouble(st.nextToken());
    
        if(rescueRate >= verificationProperty.getThresholdPatient()){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean check(String fileName, String time, String vehicleNum, String distance) {
        return false;
    }
}
