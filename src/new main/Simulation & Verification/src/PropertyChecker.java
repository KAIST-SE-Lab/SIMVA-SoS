import java.util.ArrayList;
public abstract class PropertyChecker {

/*
  public PropertyChecker() {  //constructor


  }
*/

  // this method is for inherited classes' check call
  protected abstract boolean evaluateState(Snapshot state, Property verificationProperty);
  public abstract boolean check(SimulationLog simLog, Property verificationProperty);
}
