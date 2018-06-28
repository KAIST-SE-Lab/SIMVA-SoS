package new_simvasos.not_decided;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;


public class SoS {
  public ArrayList <CS> CSs;
  ArrayList <Integer> environment;

  public SoS( ArrayList <CS> CSs, ArrayList<Integer> environment) {
    this.CSs = CSs;
    this.environment = environment;
  }

  public String run(int tick) {

    String logs = "";
    Collections.shuffle(CSs);

    for(CS cs : CSs) {
      String result = cs.act(tick, this.environment);

      if(result != null && !result.isEmpty()) {
        logs += result;
        logs += " ";
      }
    }
    //logs.add(String (environment));

    logs += this.environment.toString();
    logs += " ";
    logs += "RescuedRate: " + getRescuedRate();

    return logs;
  }

  // reset all cs's attributes ex) firefighter
  public void reset() {
    for (int i = 0; i < CSs.size(); i++) {
        CSs.get(i).reset();
    }
    this.resetEnvironment();
  }

  private void resetEnvironment() {
      for (int i = 0; i < this.environment.size(); i++) {
        this.environment.set(i, 0);
      }
  }

  private double getRescuedRate(){
    int rescued = 0;
    int notRescued = 0;

    for(CS cs : CSs){
      rescued += cs.getRescued();
    }
    for(int i : environment){
      notRescued += i;
    }
    return (double)rescued/((double)rescued + (double)notRescued);
  }

}
