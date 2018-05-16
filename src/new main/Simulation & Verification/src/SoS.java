import java.lang.reflect.Array;
import java.util.ArrayList;


public class SoS {
  ArrayList <CS> CSs;
  ArrayList <Integer> environment;
  public SoS( ArrayList <CS> CSs, ArrayList<Integer> environment) {
    this.CSs = CSs;
    this.environment = environment;

  }

  public ArrayList run(int tick) {

    return null;
  }

  // reset all cs's attributes ex) firefighter
  public void reset() {
    for (int i = 0; i < CSs.size(); i++) {
        CSs.get(i).reset();
    }
    this.resetEnvironment();
  }

  public void resetEnvironment() {
      for (int i = 0; i < this.environment.size(); i++) {
        this.environment.set(i, 0);
      }
  }

}
