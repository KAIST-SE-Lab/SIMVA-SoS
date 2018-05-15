import java.util.ArrayList;

public class Verifier {
  PropertyChecker propertychecker;

  public Verifier(PropertyChecker checker) {
    this.propertychecker = checker;
  }
  
  //
  public boolean verify(ArrayList<SimulationLog> simulationLogs, Property verificationProperty) {
    boolean check = true;
    
    for(SimulationLog log: simulationLogs) {
      if (this.propertychecker.check(log, verificationProperty) == false) {
        check = false;
      }
    }
    
    return check;
  }
}
