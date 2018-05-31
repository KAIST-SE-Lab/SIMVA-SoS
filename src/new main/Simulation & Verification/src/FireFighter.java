import java.util.ArrayList;
import java.util.Random;

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

  @Override
  public String act(int tick, ArrayList<Integer> environment) {
    String ret = "CS operated: ";
    Random random = new Random();
    
    if (random.nextFloat() < this.probability) {
      if (this.location == -1) {
        this.location = random.nextInt(environment.size());
      }
      else {
        this.location = (this.location + 1) % environment.size();
      }
      
      ret = ret + this.name + ", Current Location: " + this.location;
      //System.out.println(ret);
      if(environment.get(this.location) > 0) {
        this.rescued += 1;
        environment.set(this.location, environment.get(this.location) - 1);
        ret = ret + ", Rescued Patient :" + environment.toString();

      }
    }
    return ret;
  }

  public void reset() {
    this.location = -1;
    this.rescued = 0;
  }

  // get resuced number for simulation log
  public int getRescued() { return this.rescued; }
}
