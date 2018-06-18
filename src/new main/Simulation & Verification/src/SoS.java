import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;


public class SoS {
  ArrayList <CS> CSs;
  ArrayList <Integer> environment;

  public SoS( ArrayList <CS> CSs, ArrayList<Integer> environment) {
    this.CSs = CSs;
    this.environment = environment;
  }

  public Pair<ArrayList<String>, ArrayList<Integer>> run(int tick) {

    Pair<ArrayList<String>, ArrayList<Integer>> ret;
    ArrayList<String> logs = new ArrayList<>();
    Collections.shuffle(CSs);

    for(CS cs : CSs) {
      String result = cs.act(tick, this.environment);

      if(result != null && !result.isEmpty()) {
        logs.add(result);
      }
    }
    //logs.add(String (environment));

    ret = new Pair<>(logs, this.environment);

    return ret;
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
