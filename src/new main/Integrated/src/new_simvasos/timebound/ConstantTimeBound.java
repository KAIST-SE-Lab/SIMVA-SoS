package new_simvasos.timebound;

public class ConstantTimeBound extends TimeBound {
  int tick;

  public ConstantTimeBound(int value) {
    this.tick = value;
  }

  public int getTick() {
    return tick;
  }

}
