import java.util.Random;
public class UniformDistributionTimeBound extends TimeBound {

  int start;
  int end;
  public UniformDistributionTimeBound() {

  }

  public int getValue() {
    Random random = new Random();
    int num = random.nextInt(end - start);
    return num;
  }
}
