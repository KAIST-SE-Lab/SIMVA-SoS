package new_simvasos.property;

import new_simvasos.log.SimulationLog;

public abstract class PropertyChecker {

/*
  public PropertyChecker() {  //constructor


  }
*/

  // this method is for inherited classes' check call
  /*protected abstract boolean evaluateState(Snapshot state, Property verificationProperty);*/
  public abstract boolean check(SimulationLog simLog, Property verificationProperty);
}
