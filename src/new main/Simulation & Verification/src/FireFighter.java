public class FireFighter extends CS {

  double probability;
  int location;
  int rescue;

  public FireFighter(String name, double prob) {  //constructor

    super(name);
    this.probability = prob;
    this.location = -1;
    this.rescue = 0;

  }

  public String act() {

    return "todo";
  }

  public int reset() {

    return -1;
  }
}
