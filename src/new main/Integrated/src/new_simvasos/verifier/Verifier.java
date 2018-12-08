package new_simvasos.verifier;

import new_simvasos.property.PropertyChecker;

/**
 * The type Verifier.
 * A module of SIMVA-SoS to verify the simulation results.
 */
public class Verifier {
  PropertyChecker propertychecker;

  /**
   * Instantiates a new Verifier.
   *
   * @param checker the checker
   */
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
