public class ConstantTimeBound extends TimeBound {
  double value;
  public ConstantTimeBound(double value) {
    this.value = value;
  }

  public double getValue() {
    return value;
  }

}
