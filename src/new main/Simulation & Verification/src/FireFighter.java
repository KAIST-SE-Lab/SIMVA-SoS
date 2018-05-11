public class FireFighter extends CS {

  double probability;
  int location;
  int rescued;

  public FireFighter(String name, double prob) {  //constructor

    super(name);
    this.probability = prob;
    this.location = -1;
    this.rescued = 0;

  }

  public String act() {

    return "todo";
  }

  public void reset() {
    this.location = -1;
    this.rescued = 0;
  }
}
