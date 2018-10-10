package new_simvasos.timebound;

public class TimeBound {


  public TimeBound() {
  }

  // Change getValue() to getTick()
  public int getTick() {
      return -1;
  }

  // this method for inherited classes' reset call from Event.java
  // for the details of implementation, go to ConstantTimeBound.java or
  // UniformDistributionTimeBound.java or NormalDistribution~~.java
  public void reset() { }

}
