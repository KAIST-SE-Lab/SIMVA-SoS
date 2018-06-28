package new_simvasos.property;

import javafx.util.Pair;
import new_simvasos.log.SimulationLog;

import java.util.ArrayList;

public class MCIPropertyChecker extends PropertyChecker {

  public MCIPropertyChecker() {

    super();
  }

  // In Simulation Log, this property function check the simulation log
  // (rescued people number, patient number)
  public boolean check(SimulationLog simuLog, Property verificationProperty) {

    Pair<ArrayList<Integer>, ArrayList<Integer>> propertyLogs = simuLog.getPropertyCheckLogs();
    ArrayList<Integer> rescued = propertyLogs.getKey();
    ArrayList<Integer> environment = propertyLogs.getValue();
    
    double sumRescued = 0;
    double sumPatients = 0;
    
    for (int i : rescued ) {
      sumRescued += i;
    }
    
    for (int i : environment) {
      sumPatients += i;
    }
    
    sumPatients += sumRescued;
    if((sumRescued / sumPatients) > verificationProperty.getValue()) {
      return true;
    }
    else {
      return false;
    }
  }
}
