public class NormalDistributionTimeBound extends TimeBound {

  double mu;
  double sigma;
  double value;

  public NormalDistributionTimeBound(double mu, double sigma) {
    this.mu = mu;
    this.sigma = sigma;
    this.value = 0.0;    // None?? what??
  }

  public int getValue() {

    return -1;    // int(random.normalvariate(self.mu, self.sigma)
  }
}
