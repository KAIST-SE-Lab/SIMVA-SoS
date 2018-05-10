import java.util.Random;

public class NormalDistributionTimeBound extends TimeBound {

  double mu;
  double sigma;
  int tick;

  public NormalDistributionTimeBound(double mu, double sigma) {
    this.mu = mu;
    this.sigma = sigma;
    this.tick = 0;    // None?? what??
  }

  public int getTick() {
      Random random = new Random();

      // random.nextGaussian() provide Normal(Gaussian) Distribution way of
      // generating  random number. But this function's mu(mean) and sigma(deviation)
      // is set by 0 and 1. So for changing mu, add mu and for changing sigma, add sigma
      // Math.round provides the rounding off(반올림) of floating numbers
      return (int) Math.round(random.nextGaussian() * sigma + mu);
  }
}
