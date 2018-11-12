package new_simvasos.property;

import new_simvasos.log.Log;
import new_simvasos.log.Snapshot;
import new_simvasos.property.pattern.SteadyStateProbabilityChecker;

import java.util.StringTokenizer;

public class MCISteadyStateProbabilityChecker extends SteadyStateProbabilityChecker {
    
    @Override
    protected boolean evaluateState(Snapshot snapshot, Property verificationProperty) {
        StringTokenizer st = new StringTokenizer(snapshot.getSnapshotString(), " ");
        while(st.hasMoreTokens()) {
            if(st.nextToken().equals("RescuedRate:"))
                break;
        }
        
        double rescueRate = Double.parseDouble(st.nextToken());
        
        if(rescueRate > verificationProperty.getThresholdPatient()){
            return true;
        }
        else{
            return false;
        }
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
    public boolean check(Log log, Property verificationProperty, double prob, int t, int T) {
        return false;
    }
}
