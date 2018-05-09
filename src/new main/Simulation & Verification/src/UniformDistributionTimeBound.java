import java.util.Random;
public class UniformDistributionTimeBound extends TimeBound {

  int start;
  int end;
  double value;
  public UniformDistributionTimeBound(int start, int end) {
    this.start = start;
    this.end = end;
    this.value = 0.0;    // None?? what???

  }

  public int getValue() {
    Random random = new Random();
    int num = random.nextInt(end - start);
    return num;
  }
}
