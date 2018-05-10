import java.util.Random;
public class UniformDistributionTimeBound extends TimeBound {

  int start;
  int end;
  int tick;
  public UniformDistributionTimeBound(int start, int end) {
    this.start = start;
    this.end = end;
    this.tick = 0;    // None?? what???
  }

  public int getTick() {
    Random random = new Random();

    // Generate random number in Uniform Distribution
    return random.nextInt(end - start);
  }
}
