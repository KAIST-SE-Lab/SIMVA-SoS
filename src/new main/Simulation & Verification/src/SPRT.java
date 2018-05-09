public class SPRT extends Verifier {
  double alpha;
  double beta;
  double delta;
  int minimumSample;
  public SPRT(PropertyChecker checker) {
    super(checker);
    this.alpha = 0.05;
    this.beta = 0.05;
    this.delta = 0.01;
    this.minimumSample = 2;

  }

}
