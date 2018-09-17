package new_simvasos.verifier;

import new_simvasos.log.SimulationLog;
import new_simvasos.property.Property;
import new_simvasos.property.PropertyChecker;

import java.util.ArrayList;

public class Verifier {
  PropertyChecker propertychecker;

  public Verifier(PropertyChecker checker) {
    this.propertychecker = checker;
  }
  
  /*
  private boolean verify(ArrayList<Log> simulationLogs, Property verificationProperty) {
    boolean check = true;
    
    for(SimulationLog log: simulationLogs) {
      if (this.propertychecker.check(log, verificationProperty) == false) {
        check = false;
      }
    }
    
    return check;
  }
  */
}
