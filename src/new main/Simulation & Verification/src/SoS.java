import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;


public class SoS {
  ArrayList <CS> CSs;
  public ArrayList <Integer> environment;
  public SoS( ArrayList <CS> CSs, ArrayList<Integer> environment) {
    this.CSs = CSs;
    this.environment = environment;

  }

  public ArrayList run(int tick) {

    ArrayList<String> logs = new ArrayList<>();
    Collections.shuffle(CSs);

    for(CS cs : CSs) {
      String result = cs.act(tick, environment);

      if(result != null && !result.isEmpty()) {
        logs.add(result);
      }
    }
    //logs.add(String (environment));

    return logs;
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
