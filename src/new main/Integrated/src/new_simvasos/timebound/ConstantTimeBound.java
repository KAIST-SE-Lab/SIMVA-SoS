package new_simvasos.timebound;

public class ConstantTimeBound extends TimeBound {
  int tick;

  /**
   * Instantiates a new Constant time bound.
   *
   * @param value the value
   */
  public ConstantTimeBound(int value) {
    this.tick = value;
  }

  public int getTick() {
    return tick;
  }

}
