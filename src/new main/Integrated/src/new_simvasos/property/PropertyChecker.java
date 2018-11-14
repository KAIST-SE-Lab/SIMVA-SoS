package new_simvasos.property;

import new_simvasos.log.Log;
import new_simvasos.log.SimulationLog;
import new_simvasos.log.Snapshot;

public abstract class PropertyChecker {

/*
  public PropertyChecker() {  //constructor


  }
*/

  // this method is for inherited classes' check call
  protected abstract boolean evaluateState(Snapshot snapshot, Property verificationProperty);
  public abstract boolean check(Log log, Property verificationProperty);
  public abstract boolean check(Log log, Property verificationProperty, int until);
  public abstract boolean check(Log log, Property verificationProperty, double prob, int T);
  public abstract boolean check(Log log, Property verificationProperty, double prob, int t, int T);
  public abstract boolean check(Log log, Property verificationProperty, int t, int T);
}
