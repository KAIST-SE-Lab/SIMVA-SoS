public class NormalDistributionTimeBound extends TimeBound {

    float mu;
    float sigma;
  public NormalDistributionTimeBound() {

  }

  public int getValue() {

    return -1;    // int(random.normalvariate(self.mu, self.sigma)
  }
}
