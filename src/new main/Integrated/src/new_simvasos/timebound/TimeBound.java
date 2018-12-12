package new_simvasos.timebound;

public class TimeBound {


  /**
   * Instantiates a new Time bound.
   */
  public TimeBound() {
  }

  /**
   * Gets tick.
   *
   * @return the tick
   */
// Change getValue() to getTick()
  public int getTick() {
      return -1;
  }

  /**
   * Reset the time bound
   */
// this method for inherited classes' reset call from Event.java
  // for the details of implementation, go to ConstantTimeBound.java or
  // UniformDistributionTimeBound.java or NormalDistribution~~.java
  public void reset() { }

}
